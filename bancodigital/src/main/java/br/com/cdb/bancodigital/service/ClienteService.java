package br.com.cdb.bancodigital.service;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.dao.*;
import br.com.cdb.bancodigital.dto.ClienteAtualizadoDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.dto.ClienteDTO;
import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.EnderecoCliente;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.exceptions.ErrorMessages;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dto.response.ClienteResponse;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteDAO clienteDAO;
    private final EnderecoClienteDAO enderecoClienteDAO;
    private final ContaDAO contaDAO;
    private final CartaoDAO cartaoDAO;
    private final SeguroDAO seguroDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxaDAO;
    private final ReceitaService receitaService;
    private final SecurityService securityService;
    private final BrasilApiService brasilApiService;

    // Cadastrar cliente
    public ClienteResponse cadastrarCliente(ClienteDTO dto, Usuario usuario) {
        // Buscar dados do CEP na BrasilAPI
        CEP2 cepInfo = brasilApiService.buscarEnderecoPorCep(dto.getCep());

        // Criar cliente
        Cliente cliente = dto.transformaClienteParaObjeto();
        cliente.setCategoria(CategoriaCliente.COMUM);
        cliente.setUsuario(usuario);

        // Validações
        if (!receitaService.isCpfValidoEAtivo(cliente.getCpf()))
            throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");
        validarCpfUnico(cliente.getCpf());
        validarMaiorIdade(cliente);

        // Salvar cliente no banco e obter com ID preenchido
        Cliente clienteSalvo = clienteDAO.salvar(cliente);

        // Criar endereço
        EnderecoCliente enderecoCliente = dto.transformaEnderecoParaObjeto();
        enderecoCliente.setCliente(clienteSalvo);
        enderecoCliente.setBairro(cepInfo.getNeighborhood());
        enderecoCliente.setCidade(cepInfo.getCity());
        enderecoCliente.setEstado(cepInfo.getState());
        enderecoCliente.setRua(cepInfo.getStreet());

        // Salvar endereço no banco
        enderecoClienteDAO.salvar(enderecoCliente);

        return toResponse(clienteSalvo);
    }

    // Ver cliente(s)
    public List<ClienteResponse> getClientes() throws AccessDeniedException { //só admin
        List<Cliente> clientes = clienteDAO.buscarTodosClientes();
        return clientes.stream().map(this::toResponse).toList();
    }

    public Cliente getClienteById(Long id_cliente, Usuario usuarioLogado) {
        Cliente cliente = verificarClienteExistente(id_cliente);
        securityService.validateAccess(usuarioLogado, cliente);
        return cliente;
    }

    public ClienteResponse buscarClienteDoUsuario(Usuario usuario) {
        Cliente cliente = clienteDAO.buscarClienteporUsuario(usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o usuário logado."));
        return toResponse(cliente);
    }

    // deletar cadastro de cliente
    @Transactional
    public void deleteCliente(Long id_cliente) throws AccessDeniedException { //só admin
        Cliente cliente = verificarClienteExistente(id_cliente);

        boolean temContas = contaDAO.existsByClienteId(id_cliente);
        boolean temCartoes = cartaoDAO.existsByContaClienteId(id_cliente);
        boolean temSeguros = seguroDAO.existsByCartaoCreditoContaClienteId(id_cliente);

        if (temContas || temCartoes || temSeguros) throw new ValidationException(
                "Cliente possui vínculos com contas, cartões ou seguros e não pode ser deletado.");

        clienteDAO.deletarClientePorId(cliente.getId());
        log.info("Cliente ID {} deletado com sucesso", id_cliente);
    }

    // Atualizações de cliente
    @Transactional
    public ClienteResponse atualizarCliente(Long id_cliente, ClienteAtualizadoDTO dto, Usuario usuarioLogado) {
        // Validações
        Cliente cliente = verificarClienteExistente(id_cliente);
        securityService.validateAccess(usuarioLogado, cliente);

        // Atualizar dados pessoais
        if (dto.getNome() != null) {
            cliente.setNome(dto.getNome());
        }
        if (dto.getCpf() != null && !dto.getCpf().equals(cliente.getCpf())) {
            if (!receitaService.isCpfValidoEAtivo(dto.getCpf())) {
                throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");
            }
            validarCpfUnico(dto.getCpf());
            cliente.setCpf(dto.getCpf());
        }
        if (dto.getDataNascimento() != null) {
            cliente.setDataNascimento(dto.getDataNascimento());
        }

        validarMaiorIdade(cliente);
        clienteDAO.salvar(cliente);

        // Atualizar ou criar endereço
        if (possuiDadosDeEndereco(dto)) {
            EnderecoCliente enderecoExistente = enderecoClienteDAO.buscarEnderecoporCliente(cliente).orElse(null);
            if (enderecoExistente == null) {
                EnderecoCliente novoEndereco = construirEndereco(dto, cliente);
                enderecoClienteDAO.salvar(novoEndereco);
            } else {
                atualizarEnderecoExistente(enderecoExistente, dto);
                enderecoClienteDAO.atualizarEndereco(enderecoExistente);
            }
        }

        EnderecoCliente enderecoAtual = enderecoClienteDAO.buscarEnderecoporCliente(cliente).orElse(null);

        return toResponse(cliente);
    }

    @Transactional
    public ClienteResponse updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) throws AccessDeniedException { // só admin
        Cliente cliente = verificarClienteExistente(id_cliente);
        if (cliente.getCategoria().equals(novaCategoria))
            throw new InvalidInputParameterException("Cliente ID " + id_cliente + " já está na categoria "
                    + novaCategoria + ". Nenhuma atualização necessária.");

        cliente.setCategoria(novaCategoria);

        atualizarTaxasDoCliente(id_cliente, novaCategoria);

        return toResponse(cliente);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void atualizarTaxasDoCliente(Long id_cliente, CategoriaCliente novaCategoria) {
        try {
            PoliticaDeTaxas parametros = verificarPolitiaExitente(novaCategoria);

            atualizarTaxasDasContas(id_cliente, parametros);
            atualizarTaxasDosCartoes(id_cliente, parametros);
            atualizarTaxasDosSeguros(id_cliente, parametros);

        } catch (Exception e) {
            log.error("Falha ao atualizar Política de Taxas do cliente ID {}", id_cliente, e);
            throw e;
        }
    }

    // M
    public ClienteResponse toResponse(Cliente cliente) {
        EnderecoCliente endereco = enderecoClienteDAO.buscarEnderecoporClienteOuErro(cliente);
        return new ClienteResponse(cliente, endereco);
    }

    private void validarCpfUnico(String cpf) {
        Optional<Cliente> clienteExistente = clienteDAO.buscarClienteporCPF(cpf);
        if (clienteExistente.isPresent())
            throw new ResourceAlreadyExistsException("CPF já cadastrado no sistema.");
    }

    private void validarMaiorIdade(Cliente cliente) {
        if (!cliente.isMaiorDeIdade())
            throw new ValidationException("Cliente deve ser maior de 18 anos para se cadastrar.");
    }

    public void atualizarTaxasDasContas(Long id_cliente, PoliticaDeTaxas parametros) {
        List<Conta> contas = contaDAO.buscarPorClienteId(id_cliente);

        if (contas.isEmpty()) {
            log.info("Cliente ID {} não possui contas.", id_cliente);
            return;
        } else {
            contas.forEach(conta -> {
                switch (conta.getTipoConta()) {
                    case CORRENTE -> {
                        conta.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
                    }
                    case POUPANCA -> {
                        conta.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
                    }
                    case INTERNACIONAL -> {
                        conta.setTarifaManutencao(parametros.getTarifaManutencaoContaInternacional());
                    }
                }
            });
            contaDAO.saveAll(contas);
        }
    }

    public void atualizarTaxasDosCartoes(Long id_cliente, PoliticaDeTaxas parametros) {
        List<Cartao> cartoes = cartaoDAO.findByContaClienteId(id_cliente);

        if (cartoes.isEmpty()) {
            log.info("Cliente Id {} não possui cartões.", id_cliente);
            return;
        } else {
            cartoes.forEach(cartao -> {
                switch (cartao.getTipoCartao()) {
                    case CREDITO -> {
                        cartao.setLimite(parametros.getLimiteCartaoCredito());
                    }
                    case DEBITO -> {
                        cartao.setLimite(parametros.getLimiteDiarioDebito());
                    }
                }
            });
            cartaoDAO.saveAll(cartoes);
        }
    }

    public void atualizarTaxasDosSeguros(Long id_cliente, PoliticaDeTaxas parametros) {
        List<Seguro> seguros = seguroDAO.findByClienteId(id_cliente);

        if (seguros.isEmpty()) {
            log.info("Cliente Id {} não possui seguros.", id_cliente);
            return;
        } else {
            seguros.forEach(seguro -> {
                switch (seguro.getTipoSeguro()) {
                    case FRAUDE -> {
                        seguro.setPremioApolice(parametros.getTarifaSeguroFraude());
                    }
                    case VIAGEM -> {
                        seguro.setPremioApolice(parametros.getTarifaSeguroViagem());
                    }
                }

            });
            seguroDAO.saveAll(seguros);
        }
    }

    public PoliticaDeTaxas verificarPolitiaExitente(CategoriaCliente categoria) {
        PoliticaDeTaxas parametros = politicaDeTaxaDAO.findByCategoria(categoria)
                .orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
        return parametros;
    }

    public Cliente verificarClienteExistente(Long id_cliente) {
        Cliente cliente = clienteDAO.buscarClienteporId(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
        return cliente;
    }
    private boolean possuiDadosDeEndereco(ClienteAtualizadoDTO dto) {
        return dto.getRua() != null || dto.getNumero() != null || dto.getComplemento() != null ||
                dto.getBairro() != null || dto.getCidade() != null || dto.getEstado() != null || dto.getCep() != null;
    }

    private EnderecoCliente construirEndereco(ClienteAtualizadoDTO dto, Cliente cliente) {
        EnderecoCliente endereco = new EnderecoCliente();
        endereco.setCliente(cliente);
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());

        // opcionalmente preencher bairro/cidade/estado via BrasilAPI se cep válido
        if (dto.getCep() != null) {
            CEP2 cepInfo = brasilApiService.buscarEnderecoPorCep(dto.getCep());
            endereco.setBairro(cepInfo.getNeighborhood());
            endereco.setCidade(cepInfo.getCity());
            endereco.setEstado(cepInfo.getState());
            endereco.setRua(cepInfo.getStreet());
        }

        return endereco;
    }

    private void atualizarEnderecoExistente(EnderecoCliente endereco, ClienteAtualizadoDTO dto) {
        if (dto.getRua() != null) endereco.setRua(dto.getRua());
        if (dto.getNumero() != null) endereco.setNumero(dto.getNumero());
        if (dto.getComplemento() != null) endereco.setComplemento(dto.getComplemento());
        if (dto.getBairro() != null) endereco.setBairro(dto.getBairro());
        if (dto.getCidade() != null) endereco.setCidade(dto.getCidade());
        if (dto.getEstado() != null) endereco.setEstado(dto.getEstado());
        if (dto.getCep() != null) endereco.setCep(dto.getCep());
    }


}

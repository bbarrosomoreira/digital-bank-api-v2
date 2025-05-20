package br.com.cdb.bancodigital.service;

import java.util.List;

import br.com.cdb.bancodigital.dao.*;
import br.com.cdb.bancodigital.dto.ClienteAtualizadoDTO;
import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.resttemplate.BrasilApiRestTemplate;
import br.com.cdb.bancodigital.resttemplate.ReceitaFederalRestTemplate;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
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
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dto.response.ClienteResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Service
@AllArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteDAO clienteDAO;
    private final EnderecoClienteDAO enderecoClienteDAO;
    private final ContaDAO contaDAO;
    private final CartaoDAO cartaoDAO;
    private final SeguroDAO seguroDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxasDAO;
    private final ReceitaFederalRestTemplate receitaService;
    private final SecurityService securityService;
    private final BrasilApiRestTemplate brasilApiRestTemplate;

    // Cadastrar cliente
    @Transactional
    public ClienteResponse cadastrarCliente(ClienteDTO dto, Usuario usuario) {
        log.info(ConstantUtils.INICIO_CADASTRO_CLIENTE);

        CEP2 cepInfo = buscarEnderecoPorCep(dto.getCep());
        Cliente cliente = criarCliente(dto, usuario);
        log.info(ConstantUtils.CLIENTE_CRIADO);

        validarCliente(cliente);
        log.info(ConstantUtils.VALIDACAO_CLIENTE_CONCLUIDA);

        try {
            salvarCliente(cliente);
            salvarEndereco(dto, cliente, cepInfo);
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_SALVAR_CLIENTE_BANCO, cliente.getId(), e);
            throw new SystemException(ConstantUtils.ERRO_SALVAR_CLIENTE_BANCO + cliente.getId());
        }
        log.info(ConstantUtils.SUCESSO_CADASTRO_CLIENTE, cliente.getId());
        return toResponse(cliente);
    }

    private CEP2 buscarEnderecoPorCep(String cep) {
        try {
            CEP2 cepInfo = brasilApiRestTemplate.buscarEnderecoPorCep(cep);
            log.info(ConstantUtils.DADOS_CEP_SUCESSO);
            return cepInfo;
        } catch (HttpClientErrorException | ResourceAccessException e) {
            log.error(ConstantUtils.ERRO_BUSCAR_CEP_BRASILAPI, e.getMessage(), e);
            throw new SystemException(ConstantUtils.ERRO_BUSCAR_CEP_MENSAGEM_EXCEPTION);
        }
    }

    // Ver cliente(s)
    public List<ClienteResponse> getClientes() throws AccessDeniedException { //só admin
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
        List<Cliente> clientes = clienteDAO.buscarTodosClientes();
        return clientes.stream().map(this::toResponse).toList();
    }

    public Cliente getClienteById(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return cliente;
    }

    public ClienteResponse buscarClienteDoUsuario(Usuario usuario) {
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
        Cliente cliente = clienteDAO.buscarClienteporUsuario(usuario)
                .orElseThrow(() -> new ResourceNotFoundException(ConstantUtils.ERRO_CLIENTE_NAO_ENCONTRADO_USUARIO_LOGADO));
        return toResponse(cliente);
    }

    // deletar cadastro de cliente
    @Transactional
    public void deleteCliente(Long id_cliente) {
        log.info(ConstantUtils.INICIO_DELETE_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);

        validarVinculosCliente(id_cliente);

        try {
            clienteDAO.deletarClientePorId(cliente.getId());
            log.info(ConstantUtils.SUCESSO_DELETE_CLIENTE, id_cliente);
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_DELETE_CLIENTE, id_cliente, e);
            throw new SystemException(ConstantUtils.ERRO_DELETE_CLIENTE + id_cliente);
        }
    }

    private void validarVinculosCliente(Long id_cliente) {
        boolean temContas = contaDAO.existsByClienteId(id_cliente);
        boolean temCartoes = cartaoDAO.existsByContaClienteId(id_cliente);
        boolean temSeguros = seguroDAO.existsByCartaoContaClienteId(id_cliente);

        if (temContas || temCartoes || temSeguros) {
            log.warn(ConstantUtils.CLIENTE_POSSUI_VINCULOS, id_cliente);
            throw new ValidationException(ConstantUtils.ERRO_CLIENTE_POSSUI_VINCULOS);
        }
    }

    // Atualizações de cliente
    @Transactional
    public ClienteResponse atualizarCliente(Long id_cliente, ClienteAtualizadoDTO dto, Usuario usuarioLogado) {
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        securityService.validateAccess(usuarioLogado, cliente);

        atualizarDadosCliente(cliente, dto);

        try {
            clienteDAO.salvar(cliente);
            atualizarEnderecoSeNecessario(cliente, dto);
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_UPDATE_CLIENTE, id_cliente, e);
            throw new SystemException(ConstantUtils.ERRO_UPDATE_CLIENTE + id_cliente);
        }

        return toResponse(cliente);
    }

    private void atualizarDadosCliente(Cliente cliente, ClienteAtualizadoDTO dto) {
        if (dto.getNome() != null) {
            cliente.setNome(dto.getNome());
        }
        if (dto.getCpf() != null && !dto.getCpf().equals(cliente.getCpf())) {
            if (receitaService.isCpfInvalidoOuInativo(dto.getCpf())) {
                throw new InvalidInputParameterException(ConstantUtils.CPF_INVALIDO_RECEITA_FEDERAL);
            }
            Validator.validarCpfUnico(clienteDAO, dto.getCpf());
            cliente.setCpf(dto.getCpf());
        }
        if (dto.getDataNascimento() != null) {
            cliente.setDataNascimento(dto.getDataNascimento());
        }
        Validator.validarMaiorIdade(cliente);
    }

    private void atualizarEnderecoSeNecessario(Cliente cliente, ClienteAtualizadoDTO dto) {
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
    }

    @Transactional
    public ClienteResponse updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) throws AccessDeniedException { // só admin
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        if (cliente.getCategoria().equals(novaCategoria))
            throw new InvalidInputParameterException(ConstantUtils.ERRO_CLIENTE_JA_NA_CATEGORIA + id_cliente + novaCategoria);

        cliente.setCategoria(novaCategoria);

        atualizarTaxasDoCliente(id_cliente, novaCategoria);

        return toResponse(cliente);
    }

    @Transactional
    public void atualizarTaxasDoCliente(Long id_cliente, CategoriaCliente novaCategoria) {
        try {
            PoliticaDeTaxas parametros = Validator.verificarPoliticaExitente(politicaDeTaxasDAO, novaCategoria);

            atualizarTaxasDasContas(id_cliente, parametros);
            atualizarTaxasDosCartoes(id_cliente, parametros);
            atualizarTaxasDosSeguros(id_cliente, parametros);

        } catch (InvalidInputParameterException e) {
            log.warn(ConstantUtils.ERRO_VALIDACAO_ATUALIZACAO_CATEGORIA, id_cliente, e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_BANCO_DADOS_ATUALIZACAO_CATEGORIA, id_cliente, e.getMessage(), e);
            throw new SystemException(ConstantUtils.ERRO_INTERNO_ATUALIZACAO_CATEGORIA);
        }
    }

    // M
    public ClienteResponse toResponse(Cliente cliente) {
        EnderecoCliente endereco = enderecoClienteDAO.buscarEnderecoporClienteOuErro(cliente);
        return new ClienteResponse(cliente, endereco);
    }
    @Transactional
    public void atualizarTaxasDasContas(Long id_cliente, PoliticaDeTaxas parametros) {
        List<Conta> contas = contaDAO.buscarContaPorClienteId(id_cliente);

        if (contas.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_CONTAS, id_cliente);
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
                contaDAO.salvar(conta);
            });
        }
    }
    @Transactional
    public void atualizarTaxasDosCartoes(Long id_cliente, PoliticaDeTaxas parametros) {
        List<Cartao> cartoes = cartaoDAO.findByContaClienteId(id_cliente);

        if (cartoes.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_CARTOES, id_cliente);
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
                cartaoDAO.salvar(cartao);
            });
        }
    }
    @Transactional
    public void atualizarTaxasDosSeguros(Long id_cliente, PoliticaDeTaxas parametros) {
        List<Seguro> seguros = seguroDAO.findSegurosByClienteId(id_cliente);

        if (seguros.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_SEGUROS, id_cliente);
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

                seguroDAO.salvar(seguro);
            });
        }
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
            CEP2 cepInfo = brasilApiRestTemplate.buscarEnderecoPorCep(dto.getCep());
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

    private Cliente criarCliente(ClienteDTO dto, Usuario usuario) {
        log.info(ConstantUtils.CRIANDO_CLIENTE);
        Cliente cliente = dto.transformaClienteParaObjeto();
        cliente.setCategoria(CategoriaCliente.COMUM);
        cliente.setUsuario(usuario);

        return cliente;
    }

    private void salvarCliente(Cliente cliente) {
        log.info(ConstantUtils.SALVANDO_CLIENTE_BANCO);
        clienteDAO.salvar(cliente);
    }

    private void salvarEndereco(ClienteDTO dto, Cliente cliente, CEP2 cepInfo) {
        log.info(ConstantUtils.SALVANDO_ENDERECO_CLIENTE);
        EnderecoCliente enderecoCliente = dto.transformaEnderecoParaObjeto();
        enderecoCliente.setCliente(cliente);
        enderecoCliente.setBairro(cepInfo.getNeighborhood());
        enderecoCliente.setCidade(cepInfo.getCity());
        enderecoCliente.setEstado(cepInfo.getState());
        enderecoCliente.setRua(cepInfo.getStreet());
        enderecoClienteDAO.salvar(enderecoCliente);
    }

    public void validarCliente(Cliente cliente) {
        if (receitaService.isCpfInvalidoOuInativo(cliente.getCpf())) {
            log.error(ConstantUtils.CPF_INVALIDO_RECEITA_FEDERAL);
            throw new InvalidInputParameterException(ConstantUtils.CPF_INVALIDO_RECEITA_FEDERAL);
        }
        Validator.validarCpfUnico(clienteDAO, cliente.getCpf());
        log.info(ConstantUtils.CPF_VALIDO_UNICO);

        Validator.validarMaiorIdade(cliente);
        log.info(ConstantUtils.CLIENTE_MAIOR_IDADE);
    }


}

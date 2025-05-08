package br.com.cdb.bancodigital.service;

import java.util.List;

import br.com.cdb.bancodigital.dao.*;
import br.com.cdb.bancodigital.dto.ClienteAtualizadoDTO;
import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.resttemplate.BrasilApiRestTemplate;
import br.com.cdb.bancodigital.resttemplate.ReceitaFederalRestTemplate;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@Service
@AllArgsConstructor
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteDAO clienteDAO;
    private final EnderecoClienteDAO enderecoClienteDAO;
    private final ContaDAO contaDAO;
    private final CartaoDAO cartaoDAO;
    private final SeguroDAO seguroDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxaDAO;
    private final ReceitaFederalRestTemplate receitaService;
    private final SecurityService securityService;
    private final BrasilApiRestTemplate brasilApiRestTemplate;

    // Cadastrar cliente
    public ClienteResponse cadastrarCliente(ClienteDTO dto, Usuario usuario) {
        log.info("Iniciando cadastro de cliente");

        CEP2 cepInfo = brasilApiRestTemplate.buscarEnderecoPorCep(dto.getCep());
        log.info("Dados do CEP encontrados com sucesso");

        Cliente cliente = criarCliente(dto, usuario);
        log.info("Cliente criado: ID {}", cliente.getId());

        validarCliente(cliente);
        log.info("Validação do cliente concluída");

        try {
            salvarCliente(cliente);
            log.info("Cliente salvo no banco de dados: ID {}", cliente.getId());

            salvarEndereco(dto, cliente, cepInfo);
            log.info("Endereço salvo no banco de dados para cliente ID {}", cliente.getId());
        } catch (DataAccessException e) {
            log.error("Erro ao salvar cliente no banco: {}", e.getMessage(), e);
            throw new SystemException("Erro interno ao salvar o cliente. Tente novamente mais tarde.");
        }
        log.info("Cadastro de cliente realizado com sucesso");
        return toResponse(cliente);
    }

    // Ver cliente(s)
    public List<ClienteResponse> getClientes() throws AccessDeniedException { //só admin
        List<Cliente> clientes = clienteDAO.buscarTodosClientes();
        return clientes.stream().map(this::toResponse).toList();
    }

    public Cliente getClienteById(Long id_cliente, Usuario usuarioLogado) {
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
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
    public void deleteCliente(Long id_cliente) throws AccessDeniedException {
        log.info("Deletando cliente ID {}", id_cliente);
        try {
            Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
            log.info("Cliente encontrado: ID {}", cliente.getId());

            boolean temContas = contaDAO.existsByClienteId(id_cliente);
            boolean temCartoes = cartaoDAO.existsByContaClienteId(id_cliente);
            boolean temSeguros = seguroDAO.existsByCartaoContaClienteId(id_cliente);

            if (temContas || temCartoes || temSeguros) throw new ValidationException(
                    "Cliente possui vínculos com contas, cartões ou seguros e não pode ser deletado."); {
                        log.warn("Cliente ID {} possui vínculos com contas, cartões ou seguros", id_cliente);
            }

            log.info("Cliente sem vínculos e pronto para ser deletado.");

            clienteDAO.deletarClientePorId(cliente.getId());
            log.info("Cliente ID {} deletado com sucesso", id_cliente);
        } catch (ValidationException e) {
            log.warn("Erro de validação ao deletar cliente ID {}: {}", id_cliente, e.getMessage());
            throw new ValidationException("Erro de validação ao deletar cliente");
        } catch (DataAccessException e) {
            log.error("Erro ao acessar o banco de dados ao deletar cliente ID {}: {}", id_cliente, e.getMessage(), e);
            throw new SystemException("Erro interno ao deletar o cliente. Tente novamente mais tarde.");
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar cliente ID {}: {}", id_cliente, e.getMessage(), e);
            throw new SystemException("Erro inesperado ao deletar o cliente.");
        }
    }

    // Atualizações de cliente
    @Transactional
    public ClienteResponse atualizarCliente(Long id_cliente, ClienteAtualizadoDTO dto, Usuario usuarioLogado) {
        try {
            Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
            securityService.validateAccess(usuarioLogado, cliente);

            if (dto.getNome() != null) {
                cliente.setNome(dto.getNome());
            }
            if (dto.getCpf() != null && !dto.getCpf().equals(cliente.getCpf())) {
                if (receitaService.isCpfInvalidoOuInativo(dto.getCpf())) {
                    throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");
                }
                Validator.validarCpfUnico(clienteDAO, dto.getCpf());
                cliente.setCpf(dto.getCpf());
            }
            if (dto.getDataNascimento() != null) {
                cliente.setDataNascimento(dto.getDataNascimento());
            }

            Validator.validarMaiorIdade(cliente);
            clienteDAO.salvar(cliente);

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

            return toResponse(cliente);
        } catch (InvalidInputParameterException e) {
            log.warn("Erro de validação ao atualizar cliente ID {}: {}", id_cliente, e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            log.error("Erro ao acessar o banco de dados ao atualizar cliente ID {}: {}", id_cliente, e.getMessage(), e);
            throw new SystemException("Erro interno ao atualizar o cliente. Tente novamente mais tarde.");
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar cliente ID {}: {}", id_cliente, e.getMessage(), e);
            throw new SystemException("Erro inesperado ao atualizar o cliente.");
        }
    }

    @Transactional
    public ClienteResponse updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) throws AccessDeniedException { // só admin
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        if (cliente.getCategoria().equals(novaCategoria))
            throw new InvalidInputParameterException("Cliente ID " + id_cliente + " já está na categoria "
                    + novaCategoria + ". Nenhuma atualização necessária.");

        cliente.setCategoria(novaCategoria);

        atualizarTaxasDoCliente(id_cliente, novaCategoria);

        return toResponse(cliente);
    }

    @Transactional
    public void atualizarTaxasDoCliente(Long id_cliente, CategoriaCliente novaCategoria) {
        try {
            PoliticaDeTaxas parametros = verificarPolitiaExitente(novaCategoria);

            atualizarTaxasDasContas(id_cliente, parametros);
            atualizarTaxasDosCartoes(id_cliente, parametros);
            atualizarTaxasDosSeguros(id_cliente, parametros);

        } catch (InvalidInputParameterException e) {
            log.warn("Erro de validação ao ao atualizar Política de Taxas do cliente ID {}: {}", id_cliente, e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            log.error("Erro ao acessar o banco de dados ao ao atualizar Política de Taxas do cliente ID {}: {}", id_cliente, e.getMessage(), e);
            throw new SystemException("Erro interno ao ao atualizar Política de Taxas do cliente. Tente novamente mais tarde.");
        } catch (Exception e) {
            log.error("Falha ao atualizar Política de Taxas do cliente ID {}", id_cliente, e);
            throw new SystemException("Falha ao atualizar Política de Taxas do cliente");
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
            log.info("Cliente ID {} não possui contas.", id_cliente);
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
            log.info("Cliente Id {} não possui cartões.", id_cliente);
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
            log.info("Cliente Id {} não possui seguros.", id_cliente);
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

    public PoliticaDeTaxas verificarPolitiaExitente(CategoriaCliente categoria) {
        return politicaDeTaxaDAO.findByCategoria(categoria)
                .orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
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
        log.info("Criando cliente");
        Cliente cliente = dto.transformaClienteParaObjeto();
        cliente.setCategoria(CategoriaCliente.COMUM);
        cliente.setUsuario(usuario);

        return cliente;
    }

    private void salvarCliente(Cliente cliente) {
        log.info("Salvando cliente no banco de dados");
        clienteDAO.salvar(cliente);
    }

    private void salvarEndereco(ClienteDTO dto, Cliente cliente, CEP2 cepInfo) {
        log.info("Salvando endereço para o cliente");
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
            log.error("CPF inválido ou inativo na Receita Federal");
            throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");
        }
        Validator.validarCpfUnico(clienteDAO, cliente.getCpf());
        log.info("Cpf válido e único");

        Validator.validarMaiorIdade(cliente);
        log.info("Cliente maior de idade");
    }


}

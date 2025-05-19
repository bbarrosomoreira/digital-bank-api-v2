package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.dao.*;
import br.com.cdb.bancodigital.exceptions.custom.*;
import br.com.cdb.bancodigital.model.*;
import br.com.cdb.bancodigital.resttemplate.BrasilApiRestTemplate;
import br.com.cdb.bancodigital.resttemplate.ReceitaFederalRestTemplate;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoCartao;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.dto.response.ContaResponse;
import br.com.cdb.bancodigital.dto.response.SeguroResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
@AllArgsConstructor
@Slf4j
public class AdminService {

    private final UsuarioDAO usuarioDAO;
    private final ClienteDAO clienteDAO;
    private final EnderecoClienteDAO enderecoClienteDAO;
    private final ContaDAO contaDAO;
    private final CartaoDAO cartaoDAO;
    private final SeguroDAO seguroDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxasDAO;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final ReceitaFederalRestTemplate receitaFederalRestTemplate;
    private final BrasilApiRestTemplate brasilApiRestTemplate;
    private final ConversorMoedasService conversorMoedasService;


    // Cadastrar cliente
    @Transactional
    public ClienteResponse cadastrarCliente(ClienteUsuarioDTO dto) {
        log.info("Iniciando cadastro de cliente");

        CEP2 cepInfo = buscarEnderecoPorCep(dto.getCep());

        Usuario usuario = criarUsuario(dto);
        log.info("Usuário criado: ID {}", usuario.getId());

        Cliente cliente = criarCliente(dto, usuario);
        log.info("Cliente criado");

        validarCliente(cliente);
        log.info("Validação do cliente concluída");

        try {
            salvarCliente(cliente);
            salvarEndereco(dto, cliente, cepInfo);
        } catch (DataAccessException e) {
            log.error("Erro ao salvar cliente no banco: ID {}", cliente.getId(), e);
            throw new SystemException("Erro interno ao salvar o cliente com ID " + cliente.getId());
        }
        log.info("Cadastro de cliente realizado com sucesso: ID {}", cliente.getId());
        return toClienteResponse(cliente);
    }

    private CEP2 buscarEnderecoPorCep(String cep) {
        try {
            CEP2 cepInfo = brasilApiRestTemplate.buscarEnderecoPorCep(cep);
            log.info("Dados do CEP encontrados com sucesso");
            return cepInfo;
        } catch (HttpClientErrorException | ResourceAccessException e) {
            log.error("Erro ao buscar CEP na BrasilAPI: {}", e.getMessage(), e);
            throw new SystemException("Erro ao consultar CEP. Tente novamente mais tarde.");
        }
    }

    // Adicionar conta
    @Transactional
    public ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {
        log.info("Iniciando abertura de conta");

        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info("Cliente encontrado: ID {}", cliente.getId());

        securityService.validateAccess(usuarioLogado, cliente);
        log.info("Acesso validado");

        Conta contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
        log.info("Conta criada: ID {}", contaNova.getId());

        try {
            contaDAO.salvar(contaNova);
            log.info("Conta salva no banco de dados: ID {}", contaNova.getId());
        } catch (Exception e) {
            log.error("Erro ao abrir conta: {}", e.getMessage());
            throw new SystemException("Erro interno ao criar a conta. Tente novamente mais tarde.");
        }
        log.info("Abertura de conta realizada com sucesso");
        return toContaResponse(contaNova);
    }

    // Emitir cartao
    @Transactional
    public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
        log.info("Iniciando emissão de cartão para conta ID {}", id_conta);

        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info("Conta encontrada: ID {}", conta.getId());

        securityService.validateAccess(usuarioLogado, conta.getCliente());
        log.info("Acesso validado para usuário ID {}", usuarioLogado.getId());

        Cartao cartaoNovo = criarCartaoPorTipo(tipo, conta, senha);
        log.info("Cartão criado: ID {}", cartaoNovo.getId());

        try {
            cartaoDAO.salvar(cartaoNovo);
            log.info("Cartão salvo no banco de dados: ID {}", cartaoNovo.getId());
        } catch (DataAccessException e) {
            log.error("Erro ao salvar o cartão no banco de dados", e);
            throw new SystemException("Erro interno ao salvar o cartão. Tente novamente mais tarde.");
        }
        log.info("Emissão de cartão realizada com sucesso");
        return toCartaoResponse(cartaoNovo);
    }

    // Contratar seguro
    @Transactional
    public SeguroResponse contratarSeguro(Long id_cartao, Usuario usuarioLogado, TipoSeguro tipo) {
        log.info("Iniciando contratação de seguro");

        Cartao ccr = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        log.info("Cartão encontrado: ID {}", ccr.getId());

        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
        log.info("Acesso validado");

        Seguro seguroNovo = contratarSeguroPorTipo(tipo, ccr);
        log.info("Seguro criado: ID {}", seguroNovo.getId());

        try {
            seguroDAO.salvar(seguroNovo);
            log.info("Seguro salvo no banco de dados: ID {}", seguroNovo.getId());
        } catch (Exception e) {
            log.error("Erro ao contratar seguro: {}", e.getMessage());
            throw new SystemException("Erro interno ao contratar o seguro. Tente novamente mais tarde.");
        }
        log.info("Contratação de seguro realizada com sucesso");
        return toSeguroResponse(seguroNovo);
    }

    public Cliente getClienteById(Long id_cliente, Usuario usuarioLogado) {
        log.info("Buscando cliente por ID: {}", id_cliente);

        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info("Cliente encontrado: ID {}", cliente.getId());

        securityService.validateAccess(usuarioLogado, cliente);
        log.info("Acesso validado");

        return cliente;
    }

    private Usuario criarUsuario(ClienteUsuarioDTO dto) {
        log.info("Criando usuário");
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        return usuarioDAO.criarUsuario(dto.getEmail(), senhaCriptografada, dto.getRole());
    }

    private Cliente criarCliente(ClienteUsuarioDTO dto, Usuario usuario) {
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

    private void salvarEndereco(ClienteUsuarioDTO dto, Cliente cliente, CEP2 cepInfo) {
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
        if (receitaFederalRestTemplate.isCpfInvalidoOuInativo(cliente.getCpf())) {
            log.error("CPF inválido ou inativo na Receita Federal");
            throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");
        }
        Validator.validarCpfUnico(clienteDAO, cliente.getCpf());
        log.info("Cpf válido e único");

        Validator.validarMaiorIdade(cliente);
        log.info("Cliente maior de idade");
    }

    private Conta criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
        log.info("Verificando política de taxas");

        PoliticaDeTaxas parametros = Validator.verificarPolitiaExitente(politicaDeTaxasDAO, cliente.getCategoria());
        log.info("Política de taxas encontrada");

        return switch (tipo) {
            case CORRENTE -> {
                yield criarContaCorrente(cliente, tipo, moeda, valorDeposito, parametros);
            }
            case POUPANCA -> {
                yield criarContaPoupanca(cliente, tipo, moeda, valorDeposito, parametros);
            }
            case INTERNACIONAL -> {
                yield criarContaInternacional(cliente, tipo, moeda, valorDeposito, parametros);
            }
        };
    }

    private Conta criarContaCorrente(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta cc = new Conta(cliente, tipo);
        cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
        cc.setMoeda(moeda);
        cc.setSaldo(valorDeposito);

        return cc;
    }

    private Conta criarContaPoupanca(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta cp = new Conta(cliente, tipo);
        cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
        cp.setMoeda(moeda);
        cp.setSaldo(valorDeposito);

        return cp;
    }

    private Conta criarContaInternacional(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta ci = new Conta(cliente, tipo);
        ci.setTarifaManutencao(parametros.getTarifaManutencaoContaInternacional());
        ci.setMoeda(moeda);
        ci.setSaldoEmReais(valorDeposito);
        BigDecimal saldoMoedaExtrangeira = conversorMoedasService.converterDeBrl(ci.getMoeda(), ci.getSaldoEmReais());
        ci.setSaldo(saldoMoedaExtrangeira);

        return ci;
    }

    public Cartao criarCartaoPorTipo(TipoCartao tipo, Conta conta, String senha) {
        log.info("Verificando política de taxas");

        CategoriaCliente categoria = conta.getCliente().getCategoria();
        PoliticaDeTaxas parametros = Validator.verificarPolitiaExitente(politicaDeTaxasDAO, categoria);
        log.info("Política de taxas encontrada");

        return switch (tipo) {
            case CREDITO -> {
                Cartao ccr = new Cartao(conta, senha, tipo);
                ccr.setLimite(parametros.getLimiteCartaoCredito());
                ccr.setLimiteAtual(ccr.getLimite());
                ccr.setTotalFatura(BigDecimal.ZERO);
                yield ccr;
            }
            case DEBITO -> {
                Cartao cdb = new Cartao(conta, senha, tipo);
                cdb.setLimite(parametros.getLimiteDiarioDebito());
                cdb.setLimiteAtual(cdb.getLimite());
                yield cdb;
            }
        };

    }

    public Seguro contratarSeguroPorTipo(TipoSeguro tipo, Cartao ccr) {
        log.info("Verificando política de taxas");

        CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();
        PoliticaDeTaxas parametros = Validator.verificarPolitiaExitente(politicaDeTaxasDAO, categoria);
        log.info("Política de taxas encontrada");

        return switch (tipo) {
            case FRAUDE -> {
                Seguro sf = new Seguro(ccr);
                sf.setValorApolice(parametros.getValorApoliceFraude());
                sf.setPremioApolice(parametros.getTarifaSeguroFraude());
                sf.setDescricaoCondicoes(TipoSeguro.FRAUDE.getDescricao());
                yield sf;
            }
            case VIAGEM -> {
                Seguro sv = new Seguro(ccr);
                sv.setValorApolice(parametros.getValorApoliceViagem());
                sv.setPremioApolice(parametros.getTarifaSeguroViagem());
                sv.setDescricaoCondicoes(TipoSeguro.VIAGEM.getDescricao());
                yield sv;
            }
        };
    }

    public ClienteResponse toClienteResponse(Cliente cliente) {
        EnderecoCliente endereco = enderecoClienteDAO.buscarEnderecoporClienteOuErro(cliente);
        log.info("Endereço encontrado: ID {}", endereco.getId());

        return new ClienteResponse(cliente, endereco);
    }

    public ContaResponse toContaResponse(Conta conta) {
        BigDecimal tarifa;
        switch (conta.getTipoConta()) {
            case CORRENTE, INTERNACIONAL -> {
                tarifa = conta.getTarifaManutencao();
            }
            case POUPANCA -> {
                tarifa = conta.getTaxaRendimento();
            }
            default -> throw new IllegalStateException("Unexpected value: " + conta.getTipoConta());
        }
        ContaResponse response = new ContaResponse(conta.getId(), conta.getNumeroConta(), conta.getTipoConta(),
                conta.getMoeda(), conta.getSaldo(), conta.getDataCriacao(),
                tarifa);
        if (conta.getTipoConta().equals(TipoConta.INTERNACIONAL)) {
            response.setSaldoEmReais(conta.getSaldoEmReais());
        }
        return response;
    }

    public CartaoResponse toCartaoResponse(Cartao cartao) {
        return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
                cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(), cartao.getLimite());
    }

    public SeguroResponse toSeguroResponse(Seguro seguro) {
        return SeguroResponse.toSeguroResponse(seguro);
    }
}

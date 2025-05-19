package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.dao.*;
import br.com.cdb.bancodigital.exceptions.custom.*;
import br.com.cdb.bancodigital.model.*;
import br.com.cdb.bancodigital.resttemplate.BrasilApiRestTemplate;
import br.com.cdb.bancodigital.resttemplate.ReceitaFederalRestTemplate;
import br.com.cdb.bancodigital.utils.ConstantUtils;
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
        log.info(ConstantUtils.INICIO_CADASTRO_CLIENTE);

        CEP2 cepInfo = buscarEnderecoPorCep(dto.getCep());

        Usuario usuario = criarUsuario(dto);
        log.info(ConstantUtils.USUARIO_CRIADO_SUCESSO, usuario.getId());

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
        return toClienteResponse(cliente);
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

    // Adicionar conta
    @Transactional
    public ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {
        log.info(ConstantUtils.INICIO_ABERTURA_CONTA);

        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());

        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);

        Conta contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
        log.info(ConstantUtils.CONTA_CRIADA, contaNova.getId());

        try {
            contaDAO.salvar(contaNova);
            log.info(ConstantUtils.CONTA_SALVA_BANCO, contaNova.getId());
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_ABRIR_CONTA, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_ABRIR_CONTA_MENSAGEM_EXCEPTION);
        }
        log.info(ConstantUtils.SUCESSO_ABERTURA_CONTA);
        return toContaResponse(contaNova);
    }

    // Emitir cartao
    @Transactional
    public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
        log.info(ConstantUtils.INICIO_EMISSAO_CARTAO, id_conta);

        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA, conta.getId());

        securityService.validateAccess(usuarioLogado, conta.getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO_USUARIO, usuarioLogado.getId());

        Cartao cartaoNovo = criarCartaoPorTipo(tipo, conta, senha);
        log.info(ConstantUtils.CARTAO_CRIADO, cartaoNovo.getId());

        try {
            cartaoDAO.salvar(cartaoNovo);
            log.info(ConstantUtils.CARTAO_SALVO_BANCO, cartaoNovo.getId());
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_SALVAR_CARTAO_BANCO, e);
            throw new SystemException(ConstantUtils.ERRO_SALVAR_CARTAO_BANCO_MENSAGEM_EXCEPTION);
        }
        log.info(ConstantUtils.SUCESSO_EMISSAO_CARTAO);
        return toCartaoResponse(cartaoNovo);
    }

    // Contratar seguro
    @Transactional
    public SeguroResponse contratarSeguro(Long id_cartao, Usuario usuarioLogado, TipoSeguro tipo) {
        log.info(ConstantUtils.INICIO_CONTRATACAO_SEGURO);

        Cartao ccr = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, ccr.getId());

        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);

        Seguro seguroNovo = contratarSeguroPorTipo(tipo, ccr);
        log.info(ConstantUtils.SEGURO_CRIADO, seguroNovo.getId());

        try {
            seguroDAO.salvar(seguroNovo);
            log.info(ConstantUtils.SEGURO_SALVO_BANCO, seguroNovo.getId());
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_CONTRATAR_SEGURO, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_CONTRATAR_SEGURO_MENSAGEM_EXCEPTION);
        }
        log.info(ConstantUtils.SUCESSO_CONTRATACAO_SEGURO);
        return toSeguroResponse(seguroNovo);
    }

    public Cliente getClienteById(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.BUSCANDO_CLIENTE_ID, id_cliente);

        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());

        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);

        return cliente;
    }

    private Usuario criarUsuario(ClienteUsuarioDTO dto) {
        log.info(ConstantUtils.CRIANDO_USUARIO);
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        return usuarioDAO.criarUsuario(dto.getEmail(), senhaCriptografada, dto.getRole());
    }

    private Cliente criarCliente(ClienteUsuarioDTO dto, Usuario usuario) {
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

    private void salvarEndereco(ClienteUsuarioDTO dto, Cliente cliente, CEP2 cepInfo) {
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
        if (receitaFederalRestTemplate.isCpfInvalidoOuInativo(cliente.getCpf())) {
            log.error(ConstantUtils.CPF_INVALIDO_RECEITA_FEDERAL);
            throw new InvalidInputParameterException(ConstantUtils.CPF_INVALIDO_RECEITA_FEDERAL);
        }
        Validator.validarCpfUnico(clienteDAO, cliente.getCpf());
        log.info(ConstantUtils.CPF_VALIDO_UNICO);

        Validator.validarMaiorIdade(cliente);
        log.info(ConstantUtils.CLIENTE_MAIOR_IDADE);
    }

    private Conta criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);

        PoliticaDeTaxas parametros = Validator.verificarPolitiaExitente(politicaDeTaxasDAO, cliente.getCategoria());
        log.info(ConstantUtils.POLITICA_TAXAS_ENCONTRADA);

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
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);

        CategoriaCliente categoria = conta.getCliente().getCategoria();
        PoliticaDeTaxas parametros = Validator.verificarPolitiaExitente(politicaDeTaxasDAO, categoria);
        log.info(ConstantUtils.POLITICA_TAXAS_ENCONTRADA);

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
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);

        CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();
        PoliticaDeTaxas parametros = Validator.verificarPolitiaExitente(politicaDeTaxasDAO, categoria);
        log.info(ConstantUtils.POLITICA_TAXAS_ENCONTRADA);

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
        log.info(ConstantUtils.ENDERECO_ENCONTRADO, endereco.getId());

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
            default -> throw new IllegalStateException(ConstantUtils.VALOR_INESPERADO + conta.getTipoConta());
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

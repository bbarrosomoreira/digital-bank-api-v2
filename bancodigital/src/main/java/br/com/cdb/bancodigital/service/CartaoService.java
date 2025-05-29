package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.cdb.bancodigital.dto.response.*;
import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Status;
import br.com.cdb.bancodigital.model.enums.TipoCartao;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dao.CartaoDAO;
import br.com.cdb.bancodigital.dao.ClienteDAO;
import br.com.cdb.bancodigital.dao.ContaDAO;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Service
@AllArgsConstructor
@Slf4j
public class CartaoService {

    private final CartaoDAO cartaoDAO;
    private final ContaDAO contaDAO;
    private final ClienteDAO clienteDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxasDAO;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;

    // add cartao
    @Transactional
    public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
        log.info(ConstantUtils.INICIO_EMISSAO_CARTAO);

        // Busca e validações
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA, conta.getId());

        securityService.validateAccess(usuarioLogado, conta.getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO_USUARIO, usuarioLogado.getId());

        // Lógica de criação
        String senhaCriptografada = passwordEncoder.encode(senha);
        Cartao cartaoNovo = criarCartaoPorTipo(tipo, conta, senhaCriptografada);
        log.info(ConstantUtils.CARTAO_CRIADO, cartaoNovo.getId());

        try {
            cartaoDAO.salvar(cartaoNovo);
            log.info(ConstantUtils.CARTAO_SALVO_BANCO, cartaoNovo.getId());
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_SALVAR_CARTAO_BANCO, e);
            throw new SystemException(ConstantUtils.ERRO_SALVAR_CARTAO_BANCO_MENSAGEM_EXCEPTION);
        }

        log.info(ConstantUtils.SUCESSO_EMISSAO_CARTAO);
        return toResponse(cartaoNovo);
    }

    public Cartao criarCartaoPorTipo(TipoCartao tipo, Conta conta, String senha) {
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);
        CategoriaCliente categoria = conta.getCliente().getCategoria();
        PoliticaDeTaxas parametros = Validator.verificarPoliticaExitente(politicaDeTaxasDAO, categoria);
        log.info(ConstantUtils.POLITICA_TAXAS_ENCONTRADA);

        return switch (tipo) {
            case CREDITO -> {
                Cartao ccr = new Cartao(conta, senha, tipo);
                ccr.setLimite(parametros.getLimiteCartaoCredito());
                ccr.setLimiteAtual(ccr.getLimite());
                ccr.setTotalFatura(BigDecimal.ZERO);
                ccr.setTotalFaturaPaga(BigDecimal.ZERO);
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

    // get cartoes
    public List<CartaoResponse> getCartoes() {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO);
        List<Cartao> cartoes = cartaoDAO.buscarTodosCartoes();
        return cartoes.stream().map(this::toResponse).toList();
    }

    // get cartoes por conta
    public List<CartaoResponse> listarPorConta(Long id_conta, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_CONTA, id_conta);
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA, conta.getId());
        securityService.validateAccess(usuarioLogado, conta.getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        log.info(ConstantUtils.BUSCANDO_CARTOES_VINCULADOS_CONTA, id_conta);
        List<Cartao> cartoes = cartaoDAO.findByContaId(id_conta);
        return cartoes.stream().map(this::toResponse).toList();
    }

    // get cartao por cliente
    public List<CartaoResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        log.info(ConstantUtils.BUSCANDO_CARTOES_VINCULADOS_CLIENTE, id_cliente);
        List<Cartao> cartoes = cartaoDAO.findByContaClienteId(id_cliente);
        return cartoes.stream().map(this::toResponse).toList();
    }

    // get um cartao
    public CartaoResponse getCartaoById(Long id_cartao, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO, id_cartao);
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, cartao.getId());
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return toResponse(cartao);
    }

    // get cartão por usuário
    public List<CartaoResponse> listarPorUsuario(Usuario usuario) {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_USUARIO, usuario.getId());
        List<Cartao> cartoes = cartaoDAO.findByContaClienteUsuario(usuario);
        return cartoes.stream().map(this::toResponse).toList();
    }

    // deletar cartoes de cliente
    @Transactional
    public void deleteCartoesByCliente(Long id_cliente) {
        log.info(ConstantUtils.INICIO_DELETE_CARTAO, id_cliente);
        List<Cartao> cartoes = cartaoDAO.findByContaClienteId(id_cliente);
        if (cartoes.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_CARTOES, id_cliente);
            return;
        }
        for (Cartao cartao : cartoes) {
            try {
                cartaoDAO.validarVinculosCartao(cartao.getId());
                Validator.verificaSeTemFaturaAbertaDeCartaoCredito(cartao);
                Long id = cartao.getId();
                cartaoDAO.deletarCartaoPorId(cartao.getId());
                log.info(ConstantUtils.CARTAO_DELETADO_SUCESSO, id);

            } catch (DataIntegrityViolationException e) {
                log.error(ConstantUtils.ERRO_DELETAR_CARTAO, cartao.getId(), e);
                throw new ValidationException(ConstantUtils.ERRO_DELETAR_CARTAO_MENSAGEM + e.getMessage());
            }
        }
    }

    // pagar
    @Transactional
    public PagamentoResponse pagar(Long id_cartao, Usuario usuarioLogado, BigDecimal valor, String senha, String descricao) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        Validator.verificarCartaoAtivo(cartao.getStatus());
        Validator.verificarSenhaCorreta(senha, cartao.getSenha(), passwordEncoder);
        Validator.verificarLimiteSuficiente(valor, cartao.getLimiteAtual());

        if (cartao.getTipoCartao().equals(TipoCartao.DEBITO) && cartao.getLimiteAtual().compareTo(valor) < 0) {
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SALDO_INSUFICIENTE);
        }

        cartao.realizarPagamento(valor);
        cartaoDAO.salvar(cartao);
        return PagamentoResponse.toPagamentoResponse(cartao, valor, descricao);
    }

    // alter limite
    @Transactional
    public LimiteResponse alterarLimite(Long id_cartao, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_ALTERACAO_LIMITE, id_cartao);
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, cartao.getId());
        Validator.verificarCartaoAtivo(cartao.getStatus());
        log.info(ConstantUtils.CARTAO_ATIVO, cartao.getId());

        BigDecimal limiteConsumido = cartao.getLimite().subtract(cartao.getLimiteAtual());

        if (valor.compareTo(limiteConsumido) < 0)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_LIMITE_CONSUMIDO);

        cartao.alterarLimite(valor);
        cartaoDAO.salvar(cartao);
        return LimiteResponse.toLimiteResponse(cartao, valor);
    }

    // alter status cartao
    @Transactional
    public StatusCartaoResponse alterarStatus(Long id_cartao, Usuario usuarioLogado, Status statusNovo) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());

        if (statusNovo.equals(Status.INATIVO) && cartao.getTipoCartao().equals(TipoCartao.CREDITO)) {
            Validator.verificaSeTemFaturaAbertaDeCartaoCredito(cartao);
        }

        cartao.alterarStatus(statusNovo);
        cartaoDAO.salvar(cartao);
        return StatusCartaoResponse.toStatusCartaoResponse(cartao, statusNovo);
    }

    // alter senha
    @Transactional
    public void alterarSenha(Long id_cartao, Usuario usuarioLogado, String senhaAntiga, String senhaNova) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());

        Validator.verificarCartaoAtivo(cartao.getStatus());

        cartao.alterarSenha(senhaAntiga, senhaNova, passwordEncoder);
        cartaoDAO.salvar(cartao);
    }

    // get fatura
    public FaturaResponse getFatura(Long id_cartao, Usuario usuarioLogado) {
        Cartao ccr = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
        Validator.verificarCartaoAtivo(ccr.getStatus());

        if (ccr.getTipoCartao() != TipoCartao.CREDITO)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_CARTAO_NAO_CREDITO);

        return FaturaResponse.toFaturaResponse(ccr);
    }

    // ressetar limite credito
    @Transactional
    public FaturaPagaResponse pagarFatura(Long id_cartao, Usuario usuarioLogado) {
        Cartao ccr = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());

        Validator.verificarCartaoAtivo(ccr.getStatus());
        if (ccr.getTotalFatura().compareTo(ccr.getConta().getSaldo()) > 0)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SALDO_INSUFICIENTE);

        ccr.pagarFatura();
        cartaoDAO.salvar(ccr);
        return FaturaPagaResponse.toFaturaPagaResponse(ccr);
    }

    // ressetar limite diario
    @Transactional
    public RessetarLimiteDiarioResponse ressetarDebito(Long id_cartao) {
        Cartao cdb = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);

        Validator.verificarCartaoAtivo(cdb.getStatus());
        cdb.reiniciarLimiteDebito();
        cartaoDAO.salvar(cdb);
        return RessetarLimiteDiarioResponse.toRessetarLimiteDiarioResponse(cdb);
    }

    public CartaoResponse toResponse(Cartao cartao) {
        return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
                cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(), cartao.getLimite());
    }
}

package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
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
import br.com.cdb.bancodigital.dao.SeguroDAO;
import br.com.cdb.bancodigital.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.dto.response.FaturaResponse;
import br.com.cdb.bancodigital.dto.response.LimiteResponse;
import br.com.cdb.bancodigital.dto.response.PagamentoResponse;
import br.com.cdb.bancodigital.dto.response.RessetarLimiteDiarioResponse;
import br.com.cdb.bancodigital.dto.response.StatusCartaoResponse;

@Service
@AllArgsConstructor
@Slf4j
public class CartaoService {

    private final CartaoDAO cartaoDAO;
    private final ContaDAO contaDAO;
    private final ClienteDAO clienteDAO;
    private final SeguroDAO seguroDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxasDAO;
    private final SecurityService securityService;

    // add cartao
    @Transactional
    public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
        log.info("Iniciando emissão de cartão para conta ID {}", id_conta);

        // Busca e validações
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info("Conta encontrada: ID {}", conta.getId());

        securityService.validateAccess(usuarioLogado, conta.getCliente());
        log.info("Acesso validado para usuário ID {}", usuarioLogado.getId());

        // Lógica de criação
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
        return toResponse(cartaoNovo);
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
    // get cartoes
    public List<CartaoResponse> getCartoes() {
        log.info("Buscando todos os cartões");
        List<Cartao> cartoes = cartaoDAO.buscarTodosCartoes();
        return cartoes.stream().map(this::toResponse).toList();
    }
    // get cartoes por conta
    public List<CartaoResponse> listarPorConta(Long id_conta, Usuario usuarioLogado) {
        log.info("Buscando cartões por conta ID: {}", id_conta);
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info("Conta encontrada: ID {}", conta.getId());
        securityService.validateAccess(usuarioLogado, conta.getCliente());
        log.info("Acesso validado");
        log.info("Buscando cartões vinculados à conta ID: {}", id_conta);
        List<Cartao> cartoes = cartaoDAO.findByContaId(id_conta);
        return cartoes.stream().map(this::toResponse).toList();
    }
    // get cartao por cliente
    public List<CartaoResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado) {
        log.info("Buscando cartões por cliente ID: {}", id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info("Cliente encontrado: ID {}", cliente.getId());
        securityService.validateAccess(usuarioLogado, cliente);
        log.info("Acesso validado");
        log.info("Buscando cartões vinculados ao cliente ID: {}", id_cliente);
        List<Cartao> cartoes = cartaoDAO.findByContaClienteId(id_cliente);
        return cartoes.stream().map(this::toResponse).toList();
    }
    // get um cartao
    public CartaoResponse getCartaoById(Long id_cartao, Usuario usuarioLogado) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        return toResponse(cartao);
    }
    // get cartão por usuário
    public List<CartaoResponse> listarPorUsuario(Usuario usuario) {
        List<Cartao> cartoes = cartaoDAO.findByContaClienteUsuario(usuario);
        return cartoes.stream().map(this::toResponse).toList();
    }
    // deletar cartoes de cliente
    @Transactional
    public void deleteCartoesByCliente(Long id_cliente) {
        List<Cartao> cartoes = cartaoDAO.findByContaClienteId(id_cliente);
        if (cartoes.isEmpty()) {
            log.info("Cliente Id {} não possui cartões.", id_cliente);
            return;
        }
        for (Cartao cartao : cartoes) {
            try {
                Validator.verificarSegurosVinculados(seguroDAO, cartao);
                Validator.verificaSeTemFaturaAbertaDeCartaoCredito(cartao);
                Long id = cartao.getId();
                cartaoDAO.deletarCartaoPorId(cartao.getId());
                log.info("Cartão ID {} deletado com sucesso", id);

            } catch (DataIntegrityViolationException e) {
                log.error("Falha ao deletar cartão ID {}", cartao.getId(), e);
                throw new ValidationException("Erro ao deletar cartão: " + e.getMessage());
            }
        }
    }
    // pagar
    @Transactional
    public PagamentoResponse pagar(Long id_cartao, Usuario usuarioLogado, BigDecimal valor, String senha, String descricao) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        Validator.verificarCartaoAtivo(cartao.getStatus());
        Validator.verificarSenhaCorreta(senha, cartao.getSenha());
        Validator.verificarLimiteSuficiente(valor, cartao.getLimiteAtual());

        if (cartao.getTipoCartao().equals(TipoCartao.DEBITO)) {
            if (valor.compareTo(cartao.getConta().getSaldo()) > 0)
                throw new InvalidInputParameterException("Saldo insuficiente para esta transação. Saldo atual: " + cartao.getConta().getSaldo());
        }

        cartao.realizarPagamento(valor);
        cartaoDAO.salvar(cartao);
        return PagamentoResponse.toPagamentoResponse(cartao, valor, descricao);
    }
    // alter limite
    @Transactional
    public LimiteResponse alterarLimite(Long id_cartao, BigDecimal valor) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        Validator.verificarCartaoAtivo(cartao.getStatus());

        BigDecimal limiteConsumido = cartao.getLimite().subtract(cartao.getLimiteAtual());

        if (valor.compareTo(limiteConsumido) < 0)
            throw new InvalidInputParameterException("Limite não pode ser alterado, pois o limite consumido é maior do que o novo valor de limite.");

        cartao.alterarLimite(valor);
        cartaoDAO.salvar(cartao);
        return LimiteResponse.toLimiteResponse(cartao, valor);
    }
    // alter status cartao
    @Transactional
    public StatusCartaoResponse alterarStatus(Long id_cartao, Usuario usuarioLogado, Status statusNovo) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());

        if (statusNovo.equals(Status.INATIVO)) {
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
        Validator.verificarSenhaCorreta(senhaAntiga, cartao.getSenha());

        cartao.alterarSenha(senhaAntiga, senhaNova);
        cartaoDAO.salvar(cartao);
    }
    // get fatura
    public FaturaResponse getFatura(Long id_cartao, Usuario usuarioLogado) {
        Cartao ccr = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
        Validator.verificarCartaoAtivo(ccr.getStatus());

        return FaturaResponse.toFaturaResponse(ccr);
    }
    // ressetar limite credito
    @Transactional
    public FaturaResponse pagarFatura(Long id_cartao, Usuario usuarioLogado) {
        Cartao ccr = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());

        Validator.verificarCartaoAtivo(ccr.getStatus());
        if (ccr.getTotalFatura().compareTo(ccr.getConta().getSaldo()) > 0)
            throw new InvalidInputParameterException("Saldo insuficiente para esta transação. Saldo atual: " + (ccr.getConta().getSaldo()));

        ccr.pagarFatura();
        cartaoDAO.salvar(ccr);
        return FaturaResponse.toFaturaResponse(ccr);
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

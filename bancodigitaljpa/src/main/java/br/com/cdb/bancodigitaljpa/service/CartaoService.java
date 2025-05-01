package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigitaljpa.model.Cartao;
import br.com.cdb.bancodigitaljpa.model.Cliente;
import br.com.cdb.bancodigitaljpa.model.Conta;
import br.com.cdb.bancodigitaljpa.model.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.model.Usuario;
import br.com.cdb.bancodigitaljpa.model.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.model.enums.Status;
import br.com.cdb.bancodigitaljpa.model.enums.TipoCartao;
import br.com.cdb.bancodigitaljpa.exceptions.ErrorMessages;
import br.com.cdb.bancodigitaljpa.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ValidationException;
import br.com.cdb.bancodigitaljpa.dao.CartaoRepository;
import br.com.cdb.bancodigitaljpa.dao.ClienteRepository;
import br.com.cdb.bancodigitaljpa.dao.ContaRepository;
import br.com.cdb.bancodigitaljpa.dao.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.dao.SeguroRepository;
import br.com.cdb.bancodigitaljpa.dto.response.CartaoResponse;
import br.com.cdb.bancodigitaljpa.dto.response.FaturaResponse;
import br.com.cdb.bancodigitaljpa.dto.response.LimiteResponse;
import br.com.cdb.bancodigitaljpa.dto.response.PagamentoResponse;
import br.com.cdb.bancodigitaljpa.dto.response.RessetarLimiteDiarioResponse;
import br.com.cdb.bancodigitaljpa.dto.response.StatusCartaoResponse;

@Service
public class CartaoService {

    private static final Logger log = LoggerFactory.getLogger(CartaoService.class);

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private SeguroRepository seguroRepository;

    @Autowired
    private PoliticaDeTaxasRepository politicaDeTaxaRepository;

    @Autowired
    private SecurityService securityService;

    // add cartao
    @Transactional
    public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
        Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
        Objects.requireNonNull(senha, "A senha do cartão não pode ser nula");

        Conta conta = verificarContaExitente(id_conta);
        securityService.validateAccess(usuarioLogado, conta.getCliente());
        Cartao cartaoNovo = criarCartaoPorTipo(tipo, conta, senha);
        cartaoRepository.save(cartaoNovo);

        return toResponse(cartaoNovo);
    }

    public Cartao criarCartaoPorTipo(TipoCartao tipo, Conta conta, String senha) {

        CategoriaCliente categoria = conta.getCliente().getCategoria();
        PoliticaDeTaxas parametros = verificarPolitiaExitente(categoria);

        return switch (tipo) {
            case CREDITO -> {
                Cartao ccr = new Cartao(conta, senha, tipo);
                ccr.setLimite(parametros.getLimiteCartaoCredito());
                ccr.setTotalFatura(BigDecimal.ZERO);
                yield ccr;
            }
            case DEBITO -> {
                Cartao cdb = new Cartao(conta, senha, tipo);
                cdb.setLimite(parametros.getLimiteDiarioDebito());
                yield cdb;
            }
        };

    }

    // get cartoes
    public List<CartaoResponse> getCartoes() {
        List<Cartao> cartoes = cartaoRepository.findAll();
        return cartoes.stream().map(this::toResponse).toList();
    }

    // get cartoes por conta
    public List<CartaoResponse> listarPorConta(Long id_conta, Usuario usuarioLogado) {
        Conta conta = verificarContaExitente(id_conta);
        securityService.validateAccess(usuarioLogado, conta.getCliente());
        List<Cartao> cartoes = cartaoRepository.findByContaId(id_conta);
        return cartoes.stream().map(this::toResponse).toList();
    }

    // get cartao por cliente
    public List<CartaoResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado) {
        Cliente cliente = verificarClienteExistente(id_cliente);
        securityService.validateAccess(usuarioLogado, cliente);
        List<Cartao> cartoes = cartaoRepository.findByContaClienteId(id_cliente);
        return cartoes.stream().map(this::toResponse).toList();
    }

    // get um cartao
    public CartaoResponse getCartaoById(Long id_cartao, Usuario usuarioLogado) {
        Cartao cartao = verificarCartaoExistente(id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        return toResponse(cartao);
    }

    // get cartao por usuário
    public List<CartaoResponse> listarPorUsuario(Usuario usuario) {
        List<Cartao> cartoes = cartaoRepository.findByContaClienteUsuario(usuario);
        return cartoes.stream().map(this::toResponse).toList();
    }

    // deletar cartoes de cliente
    @Transactional
    public void deleteCartoesByCliente(Long id_cliente) {
        List<Cartao> cartoes = cartaoRepository.findByContaClienteId(id_cliente);
        if (cartoes.isEmpty()) {
            log.info("Cliente Id {} não possui cartões.", id_cliente);
            return;
        }
        for (Cartao cartao : cartoes) {
            try {
                verificarSegurosVinculados(cartao);
                verificaSeTemFaturaAbertaDeCartaoCredito(cartao);
                Long id = cartao.getId();
                cartaoRepository.delete(cartao);
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
        Cartao cartao = verificarCartaoExistente(id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        verificarCartaoAtivo(cartao.getStatus());
        verificarSenhaCorreta(senha, cartao.getSenha());
        verificarLimiteSuficiente(valor, cartao.getLimiteAtual());

        if (cartao.getTipoCartao().equals(TipoCartao.DEBITO)) {
            if (valor.compareTo(cartao.getConta().getSaldo()) > 0)
                throw new InvalidInputParameterException("Saldo insuficiente para esta transação. Saldo atual: " + cartao.getConta().getSaldo());
        }

        cartao.realizarPagamento(valor);
        cartaoRepository.save(cartao);
        return PagamentoResponse.toPagamentoResponse(cartao, valor, descricao);
    }

    // alter limite
    @Transactional
    public LimiteResponse alterarLimite(Long id_cartao, BigDecimal valor) {
        Cartao cartao = verificarCartaoExistente(id_cartao);
        verificarCartaoAtivo(cartao.getStatus());

        BigDecimal limiteConsumido = cartao.getLimite().subtract(cartao.getLimiteAtual());

        if (valor.compareTo(limiteConsumido) < 0)
            throw new InvalidInputParameterException("Limite não pode ser alterado, pois o limite consumido é maior do que o novo valor de limite.");

        cartao.alterarLimite(valor);
        cartaoRepository.save(cartao);
        return LimiteResponse.toLimiteResponse(cartao, valor);
    }

    // alter status cartao
    @Transactional
    public StatusCartaoResponse alterarStatus(Long id_cartao, Usuario usuarioLogado, Status statusNovo) {
        Cartao cartao = verificarCartaoExistente(id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());

        if (statusNovo.equals(Status.INATIVO)) {
            verificaSeTemFaturaAbertaDeCartaoCredito(cartao);
        }

        cartao.alterarStatus(statusNovo);
        cartaoRepository.save(cartao);
        return StatusCartaoResponse.toStatusCartaoResponse(cartao, statusNovo);
    }

    // alter senha
    @Transactional
    public void alterarSenha(Long id_cartao, Usuario usuarioLogado, String senhaAntiga, String senhaNova) {
        Cartao cartao = verificarCartaoExistente(id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());

        verificarCartaoAtivo(cartao.getStatus());
        verificarSenhaCorreta(senhaAntiga, cartao.getSenha());

        cartao.alterarSenha(senhaAntiga, senhaNova);
        cartaoRepository.save(cartao);
    }

    // get fatura
    public FaturaResponse getFatura(Long id_cartao, Usuario usuarioLogado) {
        Cartao ccr = cartaoRepository.findCartaoById(id_cartao)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
        verificarCartaoAtivo(ccr.getStatus());

        return FaturaResponse.toFaturaResponse(ccr);
    }

    // ressetar limite credito
    @Transactional
    public FaturaResponse pagarFatura(Long id_cartao, Usuario usuarioLogado) {
        Cartao ccr = cartaoRepository.findCartaoById(id_cartao)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());

        verificarCartaoAtivo(ccr.getStatus());
        if (ccr.getTotalFatura().compareTo(ccr.getConta().getSaldo()) > 0)
            throw new InvalidInputParameterException("Saldo insuficiente para esta transação. Saldo atual: " + (ccr.getConta().getSaldo()));

        ccr.pagarFatura();
        cartaoRepository.save(ccr);
        return FaturaResponse.toFaturaResponse(ccr);
    }

    // ressetar limite diario
    @Transactional
    public RessetarLimiteDiarioResponse ressetarDebito(Long id_cartao) {
        Cartao cdb = cartaoRepository.findCartaoById(id_cartao)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));

        verificarCartaoAtivo(cdb.getStatus());

        cdb.reiniciarLimiteDebito();
        cartaoRepository.save(cdb);
        return RessetarLimiteDiarioResponse.toRessetarLimiteDiarioResponse(cdb);
    }

    // M
    public CartaoResponse toResponse(Cartao cartao) {
        return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
                cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(), cartao.getLimite());
    }

    public void verificaSeTemFaturaAbertaDeCartaoCredito(Cartao cartao) {
        if (cartao.getTotalFatura().compareTo(BigDecimal.ZERO) > 0)
            throw new InvalidInputParameterException("Cartão não pode ser desativado com fatura em aberto.");
    }

    public void verificarSegurosVinculados(Cartao cartao) {
        Long id = cartao.getId_cartao();
        boolean existeSeguro = seguroRepository.existsByCartaoCreditoId(id);
        log.info("Cartão ID {} possui seguro vinculado? {}", id, existeSeguro);
        if (existeSeguro) {
            throw new InvalidInputParameterException("Cartão não pode ser excluído com seguros vinculados.");
        }
    }

    public Conta verificarContaExitente(Long id_conta) {
        Conta conta = contaRepository.findById(id_conta)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
        return conta;
    }

    public Cliente verificarClienteExistente(Long id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
        return cliente;
    }

    public PoliticaDeTaxas verificarPolitiaExitente(CategoriaCliente categoria) {
        PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
                .orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
        return parametros;
    }

    public Cartao verificarCartaoExistente(Long id_cartao) {
        Cartao cartao = cartaoRepository.findById(id_cartao)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
        return cartao;
    }

    public void verificarCartaoAtivo(Status status) {
        if (status.equals(Status.INATIVO))
            throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
    }

    public void verificarSenhaCorreta(String senhaDigitada, String senhaCartao) {
        if (!senhaDigitada.equals(senhaCartao)) throw new ValidationException("A senha informada está incorreta!");
    }

    public void verificarLimiteSuficiente(BigDecimal valor, BigDecimal limiteAtual) {
        if (valor.compareTo(limiteAtual) > 0)
            throw new InvalidInputParameterException("Limite insuficiente para esta transação. Limite atual: " + (limiteAtual));
    }
}

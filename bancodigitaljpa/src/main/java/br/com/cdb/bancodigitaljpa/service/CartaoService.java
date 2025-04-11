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

import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.CartaoDebito;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import br.com.cdb.bancodigitaljpa.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ValidationException;
import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.repository.SeguroRepository;
import br.com.cdb.bancodigitaljpa.response.CartaoResponse;
import br.com.cdb.bancodigitaljpa.response.FaturaResponse;
import br.com.cdb.bancodigitaljpa.response.LimiteResponse;
import br.com.cdb.bancodigitaljpa.response.PagamentoResponse;
import br.com.cdb.bancodigitaljpa.response.RessetarLimiteDiarioResponse;
import br.com.cdb.bancodigitaljpa.response.StatusCartaoResponse;

@Service
public class CartaoService {
	
	private static final Logger log = LoggerFactory.getLogger(CartaoService.class);

	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private SeguroRepository seguroRepository;

	@Autowired
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;

	// add cartao
	@Transactional
	public CartaoResponse emitirCartao(Long id_conta, TipoCartao tipo, String senha) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		Objects.requireNonNull(senha, "A senha do cartão não pode ser nula");

		ContaBase conta = verificarContaExitente(id_conta);
		CartaoBase cartaoNovo = criarCartaoPorTipo(tipo, conta, senha);
		cartaoRepository.save(cartaoNovo);

		return toResponse(cartaoNovo);
	}

	public CartaoBase criarCartaoPorTipo(TipoCartao tipo, ContaBase conta, String senha) {

		CategoriaCliente categoria = conta.getCliente().getCategoria();
		PoliticaDeTaxas parametros = verificarPolitiaExitente(categoria);
		
		return switch (tipo) {
		case CREDITO -> {
			CartaoCredito ccr = new CartaoCredito(conta, senha, parametros.getLimiteCartaoCredito());
			yield ccr;
		}
		case DEBITO -> {
			CartaoDebito cdb = new CartaoDebito(conta, senha, parametros.getLimiteDiarioDebito());
			yield cdb;
		}
		};

	}

	// get cartoes
	public List<CartaoResponse> getCartoes() {
		List<CartaoBase> cartoes = cartaoRepository.findAll();
		return cartoes.stream().map(this::toResponse).toList();
	}

	// get cartoes por conta
	public List<CartaoResponse> listarPorConta(Long id_conta) {
		verificarContaExitente(id_conta);
		List<CartaoBase> cartoes = cartaoRepository.findByContaId(id_conta);
		return cartoes.stream().map(this::toResponse).toList();
	}

	// get cartao por cliente
	public List<CartaoResponse> listarPorCliente(Long id_cliente) {
		List<CartaoBase> cartoes = cartaoRepository.findByContaClienteId(id_cliente);
		return cartoes.stream().map(this::toResponse).toList();
	}

	// get um cartao
	public CartaoResponse getCartaoById(Long id_cartao) {
		CartaoBase cartao = verificarCartaoExistente(id_cartao);
		return toResponse(cartao);
	}
	
	// deletar cartoes de cliente
	@Transactional
	public void deleteCartoesByCliente(Long id_cliente) {
		List<CartaoBase> cartoes = cartaoRepository.findByContaClienteId(id_cliente);
		if (cartoes.isEmpty()) {
			log.info("Cliente Id {} não possui cartões.", id_cliente);
			return;
		} 
		for (CartaoBase cartao : cartoes) {
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
	public PagamentoResponse pagar(Long id_cartao, BigDecimal valor, String senha, String descricao) {
		CartaoBase cartao = verificarCartaoExistente(id_cartao);
		verificarCartaoAtivo(cartao.getStatus());
		verificarSenhaCorreta(senha, cartao.getSenha());
		verificarLimiteSuficiente(valor, cartao.getLimiteAtual());
		
		if (cartao instanceof CartaoDebito) {
			if(valor.compareTo(((CartaoDebito) cartao).getConta().getSaldo())>0) throw new InvalidInputParameterException("Saldo insuficiente para esta transação. Saldo atual: "+((CartaoDebito) cartao).getConta().getSaldo());	
		}
		
		cartao.realizarPagamento(valor);
		cartaoRepository.save(cartao);
		return PagamentoResponse.toPagamentoResponse(cartao, valor, descricao);
	}

	// alter limite
	@Transactional
	public LimiteResponse alterarLimite(Long id_cartao, BigDecimal valor) {
		CartaoBase cartao = verificarCartaoExistente(id_cartao);
		verificarCartaoAtivo(cartao.getStatus());
		
		if (cartao instanceof CartaoDebito) {
			BigDecimal limiteConsumido = ((CartaoDebito) cartao).getLimiteDiario().subtract(((CartaoDebito) cartao).getLimiteAtual());
			if (valor.compareTo(limiteConsumido)<0) throw new InvalidInputParameterException("Limite não pode ser alterado, pois o limite consumido é maior do que o novo valor de limite.");
		} else if (cartao instanceof CartaoCredito) {
			BigDecimal limiteConsumido = ((CartaoCredito) cartao).getLimiteCredito().subtract(((CartaoCredito) cartao).getLimiteAtual());
			if (valor.compareTo(limiteConsumido)<0) throw new InvalidInputParameterException("Limite não pode ser alterado, pois o limite consumido é maior do que o novo valor de limite.");
		}
		
		cartao.alterarLimite(valor);
		cartaoRepository.save(cartao);
		return LimiteResponse.toLimiteResponse(cartao, valor);
	}

	// alter status cartao
	@Transactional
	public StatusCartaoResponse alterarStatus(Long id_cartao, Status statusNovo) {
		CartaoBase cartao = verificarCartaoExistente(id_cartao);
		
		if (statusNovo.equals(Status.DESATIVADO)) {
			verificaSeTemFaturaAbertaDeCartaoCredito(cartao);
		}
		
		cartao.alterarStatus(statusNovo);
		cartaoRepository.save(cartao);
		return StatusCartaoResponse.toStatusCartaoResponse(cartao, statusNovo);
	}

	// alter senha
	@Transactional
	public void alterarSenha(Long id_cartao, String senhaAntiga, String senhaNova) {
		CartaoBase cartao = verificarCartaoExistente(id_cartao);
		
		verificarCartaoAtivo(cartao.getStatus());
		verificarSenhaCorreta(senhaAntiga, cartao.getSenha());
		
		cartao.alterarSenha(senhaAntiga, senhaNova);
		cartaoRepository.save(cartao);
	}

	// get fatura
	public FaturaResponse getFatura(Long id_cartao) {
		CartaoCredito ccr = cartaoRepository.findCartaoCreditoById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		
		verificarCartaoAtivo(ccr.getStatus());
		
		return FaturaResponse.toFaturaResponse(ccr);
	}

	// ressetar limite credito
	@Transactional
	public FaturaResponse pagarFatura(Long id_cartao) {
		CartaoCredito ccr = cartaoRepository.findCartaoCreditoById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		
		verificarCartaoAtivo(ccr.getStatus());
		if(ccr.getTotalFatura().compareTo(ccr.getConta().getSaldo())>0) throw new InvalidInputParameterException("Saldo insuficiente para esta transação. Saldo atual: "+(ccr.getConta().getSaldo()));
		
		ccr.pagarFatura();
		cartaoRepository.save(ccr);
		return FaturaResponse.toFaturaResponse(ccr);
	}

	// ressetar limite diario
	@Transactional
	public RessetarLimiteDiarioResponse ressetarDebito(Long id_cartao) {
		CartaoDebito cdb = cartaoRepository.findCartaoDebitoById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		
		verificarCartaoAtivo(cdb.getStatus());
		
		cdb.ressetarLimiteDiario();
		cartaoRepository.save(cdb);
		return RessetarLimiteDiarioResponse.toRessetarLimiteDiarioResponse(cdb);
	}

	// M
	public CartaoResponse toResponse(CartaoBase cartao) {
		return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
				cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(),
				(cartao instanceof CartaoCredito) ? ((CartaoCredito) cartao).getLimiteCredito()
						: (cartao instanceof CartaoDebito) ? ((CartaoDebito) cartao).getLimiteDiario() : null);
	}
	public void verificaSeTemFaturaAbertaDeCartaoCredito (CartaoBase cartao) {
		if (cartao instanceof CartaoCredito) {
			if (((CartaoCredito) cartao).getTotalFatura().compareTo(BigDecimal.ZERO) > 0)
				throw new InvalidInputParameterException("Cartão não pode ser desativado com fatura em aberto.");
		}
	}
	public void verificarSegurosVinculados(CartaoBase cartao) {
		if (cartao instanceof CartaoCredito) {
			if (!seguroRepository.existsByCartaoCreditoId(((CartaoCredito) cartao).getId()))
				throw new InvalidInputParameterException("Cartão não pode ser excluído com seguros vinculados.");
		}
	}
	public ContaBase verificarContaExitente(Long id_conta) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID "+id_conta+" não encontrada."));
		return conta;
	}
	public PoliticaDeTaxas verificarPolitiaExitente(CategoriaCliente categoria) {
		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
		.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
		return parametros;
	}
	public CartaoBase verificarCartaoExistente(Long id_cartao) {
		CartaoBase cartao = cartaoRepository.findById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		return cartao;
	}
	public void verificarCartaoAtivo(Status status) {
		if (status.equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
	}
	public void verificarSenhaCorreta(String senhaDigitada, String senhaCartao) {
		if (!senhaDigitada.equals(senhaCartao)) throw new ValidationException("A senha informada está incorreta!");
	}
	public void verificarLimiteSuficiente(BigDecimal valor, BigDecimal limiteAtual) {
		if(valor.compareTo(limiteAtual)>0) throw new InvalidInputParameterException("Limite insuficiente para esta transação. Limite atual: "+(limiteAtual));
	}
}

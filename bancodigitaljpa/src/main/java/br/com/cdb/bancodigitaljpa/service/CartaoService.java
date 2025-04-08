package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigitaljpa.dto.CartaoResponse;
import br.com.cdb.bancodigitaljpa.dto.FaturaResponse;
import br.com.cdb.bancodigitaljpa.dto.LimiteResponse;
import br.com.cdb.bancodigitaljpa.dto.PagamentoResponse;
import br.com.cdb.bancodigitaljpa.dto.RessetarLimiteDiarioResponse;
import br.com.cdb.bancodigitaljpa.dto.StatusCartaoResponse;
import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.CartaoDebito;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import br.com.cdb.bancodigitaljpa.exceptions.InvalidInputParameterException;
import br.com.cdb.bancodigitaljpa.exceptions.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.exceptions.ValidationException;
import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;

	// add cartao
	@Transactional
	public CartaoResponse emitirCartao(Long id_conta, TipoCartao tipo, String senha) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		Objects.requireNonNull(senha, "A senha do cartão não pode ser nula");

		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID "+id_conta+" não encontrada."));

		CartaoBase cartaoNovo = criarCartaoPorTipo(tipo, conta, senha);
		cartaoRepository.save(cartaoNovo);

		return toResponse(cartaoNovo);
	}

	public CartaoBase criarCartaoPorTipo(TipoCartao tipo, ContaBase conta, String senha) {

		CategoriaCliente categoria = conta.getCliente().getCategoria();

		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
				.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));

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
		CartaoBase cartao = cartaoRepository.findById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		return toResponse(cartao);
	}

	// pagar
	@Transactional
	public PagamentoResponse pagar(Long id_cartao, BigDecimal valor, String senha, String descricao) {
		CartaoBase cartao = cartaoRepository.findById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		if (cartao.getStatus().equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
		if (!senha.equals(cartao.getSenha())) throw new ValidationException("Compra não finalizada. A senha informada está incorreta!");
		if(valor.compareTo(cartao.getLimiteAtual())>0) throw new InvalidInputParameterException("Limite insuficiente para esta transação. Limite atual: "+(cartao.getLimiteAtual()));
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
		CartaoBase cartao = cartaoRepository.findById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		if (cartao.getStatus().equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
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
		CartaoBase cartao = cartaoRepository.findById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		if (statusNovo.equals(Status.DESATIVADO)) {
			if (cartao instanceof CartaoCredito) {
				if (((CartaoCredito) cartao).getTotalFatura().compareTo(BigDecimal.ZERO) > 0)
					throw new InvalidInputParameterException("Cartão não pode ser desativado com fatura em aberto.");
			}
		}
		cartao.alterarStatus(statusNovo);
		cartaoRepository.save(cartao);
		return StatusCartaoResponse.toStatusCartaoResponse(cartao, statusNovo);
	}

	// alter senha
	@Transactional
	public void alterarSenha(Long id_cartao, String senhaAntiga, String senhaNova) {
		CartaoBase cartao = cartaoRepository.findById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		if (cartao.getStatus().equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
		cartao.alterarSenha(senhaAntiga, senhaNova);
		cartaoRepository.save(cartao);
	}

	// get fatura
	public FaturaResponse getFatura(Long id_cartao) {
		CartaoCredito ccr = cartaoRepository.findCartaoCreditoById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		if (ccr.getStatus().equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
		return FaturaResponse.toFaturaResponse(ccr);
	}

	// ressetar limite credito
	@Transactional
	public FaturaResponse pagarFatura(Long id_cartao) {
		CartaoCredito ccr = cartaoRepository.findCartaoCreditoById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		if (ccr.getStatus().equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
		
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
		if (cdb.getStatus().equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
		cdb.ressetarLimiteDiario();
		cartaoRepository.save(cdb);
		return RessetarLimiteDiarioResponse.toRessetarLimiteDiarioResponse(cdb);
	}

	// M
	public CartaoResponse toResponse(CartaoBase cartao) {
		return new CartaoResponse(cartao.getId_cartao(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
				cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(),
				(cartao instanceof CartaoCredito) ? ((CartaoCredito) cartao).getLimiteCredito()
						: (cartao instanceof CartaoDebito) ? ((CartaoDebito) cartao).getLimiteDiario() : null);
	}
}

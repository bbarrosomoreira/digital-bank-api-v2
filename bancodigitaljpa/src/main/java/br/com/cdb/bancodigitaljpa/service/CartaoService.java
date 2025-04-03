package br.com.cdb.bancodigitaljpa.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigitaljpa.dto.CartaoResponse;
import br.com.cdb.bancodigitaljpa.dto.EmitirCartaoDTO;
import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.CartaoDebito;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import br.com.cdb.bancodigitaljpa.exceptions.CartaoNaoEncontradoException;
import br.com.cdb.bancodigitaljpa.exceptions.ContaNaoEncontradaException;
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
	public CartaoResponse emitirCartao(Long id_conta, EmitirCartaoDTO dto) {
		Objects.requireNonNull(dto, "Campos não podem ser nulos");

		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ContaNaoEncontradaException(id_conta));

		CartaoBase cartaoNovo = criarCartaoPorTipo(dto.getTipoCartao(), conta, dto.getSenha());
		cartaoRepository.save(cartaoNovo);
		
		return toResponse(cartaoNovo);
	}

	public CartaoBase criarCartaoPorTipo(TipoCartao tipo, ContaBase conta, String senha) {

		CategoriaCliente categoria = conta.getCliente().getCategoria();

		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
				.orElseThrow(() -> new RuntimeException("Parâmetros não encontrados para a categoria: " + categoria));

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
	public List<CartaoResponse> getCartoes(){
		List<CartaoBase> cartoes = cartaoRepository.findAll();
		return cartoes.stream()
				.map(this::toResponse)
				.toList();
	}
	
	// get cartoes por conta
	public List<CartaoResponse> listarPorConta(Long id_conta){
		List<CartaoBase> cartoes = cartaoRepository.findByContaId(id_conta);
		return cartoes.stream()
				.map(this::toResponse)
				.toList();
	}
	
	// get cartao por cliente
	public List<CartaoResponse> listarPorCliente(Long id_cliente) {
		List<CartaoBase> cartoes = cartaoRepository.findByContaClienteId(id_cliente);
		return cartoes.stream()
				.map(this::toResponse)
				.toList();
	}
	
	// get um cartao
	public CartaoResponse getCartaoById(Long id_cartao) {
		CartaoBase cartao = cartaoRepository.findById(id_cartao)
				.orElseThrow(()-> new CartaoNaoEncontradoException(id_cartao));
		return toResponse(cartao);
	}

	// pagar
	@Transactional
	

	// alter limite

	// alter status cartao

	// alter senha

	// get fatura

	// alter limite diario

	//M
	public CartaoResponse toResponse(CartaoBase cartao) {
		return new CartaoResponse(
				cartao.getId_cartao(),
				cartao.getNumeroCartao(),
				cartao.getTipoCartao(),
				cartao.getStatus(),
				cartao.getConta().getId(),
				cartao.getDataVencimento(),
				(cartao instanceof CartaoCredito) ? ((CartaoCredito) cartao).getLimiteCredito() :
					(cartao instanceof CartaoDebito) ? ((CartaoDebito) cartao).getLimiteDiario() : null);
	}
}

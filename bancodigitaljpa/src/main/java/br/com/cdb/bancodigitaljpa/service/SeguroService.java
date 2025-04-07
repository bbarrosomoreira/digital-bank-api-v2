package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljpa.dto.AcionarSeguroResponse;
import br.com.cdb.bancodigitaljpa.dto.SeguroResponse;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
import br.com.cdb.bancodigitaljpa.entity.SeguroFraude;
import br.com.cdb.bancodigitaljpa.entity.SeguroViagem;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import br.com.cdb.bancodigitaljpa.exceptions.CartaoNaoEncontradoException;
import br.com.cdb.bancodigitaljpa.exceptions.SeguroNaoEncontradoException;
import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.repository.SeguroRepository;
import jakarta.transaction.Transactional;

@Service
public class SeguroService {

	@Autowired
	private SeguroRepository seguroRepository;

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;

	// contrataSeguro
	@Transactional
	public SeguroResponse contratarSeguro(Long id_cartaoCredito, TipoSeguro tipo) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		CartaoCredito ccr = cartaoRepository.findCartaoCreditoById(id_cartaoCredito)
				.orElseThrow(() -> new CartaoNaoEncontradoException(id_cartaoCredito));
		SeguroBase seguroNovo = contratarSeguroPorTipo(tipo, ccr);
		seguroRepository.save(seguroNovo);
		return toResponse(seguroNovo);
	}

	public SeguroBase contratarSeguroPorTipo(TipoSeguro tipo, CartaoCredito ccr) {
		
		CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();

		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
				.orElseThrow(() -> new RuntimeException("Parâmetros não encontrados para a categoria: " + categoria));

		return switch (tipo) {
		case FRAUDE -> {
			SeguroFraude sf = new SeguroFraude(ccr);
			sf.setPremioApolice(parametros.getTarifaSeguroFraude());
			yield sf;
		}
		case VIAGEM -> {
			SeguroViagem sv = new SeguroViagem(ccr);
			sv.setPremioApolice(parametros.getTarifaSeguroViagem());
			yield sv;
		}
		};
	}

	// obtemDetalhesApolice
	public SeguroResponse getSeguroById(Long id_seguro) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new SeguroNaoEncontradoException(id_seguro));
		return SeguroResponse.toSeguroResponse(seguro);
	}

	// CancelarApoliceSeguro
	@Transactional
	public void cancelarSeguro(Long id_seguro) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new SeguroNaoEncontradoException(id_seguro));
		seguro.setarStatusSeguro(Status.DESATIVADO);
		seguroRepository.save(seguro);
	}

	// get seguros
	public List<SeguroResponse> getSeguros() {
		List<SeguroBase> seguros = seguroRepository.findAll();
		return seguros.stream().map(this::toResponse).toList();
	}

	// get seguros by cartao
	public List<SeguroResponse> getSeguroByCartaoId(Long id_cartao) {
		List<SeguroBase> seguros = seguroRepository.findByCartaoCreditoId(id_cartao);
		return seguros.stream().map(this::toResponse).toList();
	}

	// get seguros by cliente
	public List<SeguroResponse> getSeguroByClienteId(Long id_cliente) {
		List<SeguroBase> seguros = seguroRepository.findByClienteId(id_cliente);
		return seguros.stream().map(this::toResponse).toList();
	}
	
	@Transactional
	// acionarSeguro
	public AcionarSeguroResponse acionarSeguroFraude(Long id_seguro, BigDecimal valor) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new SeguroNaoEncontradoException(id_seguro));
		if (!(seguro instanceof SeguroFraude)) throw new IllegalArgumentException("O seguro precisa ser do tipo Fraude."); 
		((SeguroFraude) seguro).setValorFraude(valor);
		((SeguroFraude) seguro).acionarSeguro();
		return AcionarSeguroResponse.toSeguroResponse((SeguroFraude) seguro);
	}
	
	@Transactional
	// debitarPremioSeguro
	public void debitarPremioSeguroViagem(Long id_seguro) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new SeguroNaoEncontradoException(id_seguro));
		if (!(seguro instanceof SeguroViagem)) throw new IllegalArgumentException("O seguro precisa ser do tipo Viagem.");
		if (seguro.getStatusSeguro().equals(Status.DESATIVADO)) throw new IllegalArgumentException("Prêmio não cobrado. O Seguro de Viagem está desativado!");
		((SeguroViagem) seguro).acionarSeguro();
	}

	public SeguroResponse toResponse(SeguroBase seguro) {
		return SeguroResponse.toSeguroResponse(seguro);
	}

}

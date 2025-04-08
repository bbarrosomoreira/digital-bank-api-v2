package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
import br.com.cdb.bancodigitaljpa.entity.SeguroFraude;
import br.com.cdb.bancodigitaljpa.entity.SeguroViagem;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import br.com.cdb.bancodigitaljpa.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.repository.SeguroRepository;
import br.com.cdb.bancodigitaljpa.response.CancelarSeguroResponse;
import br.com.cdb.bancodigitaljpa.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigitaljpa.response.SeguroResponse;
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
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartaoCredito + " não encontrado."));
		SeguroBase seguroNovo = contratarSeguroPorTipo(tipo, ccr);
		seguroRepository.save(seguroNovo);
		return toResponse(seguroNovo);
	}

	public SeguroBase contratarSeguroPorTipo(TipoSeguro tipo, CartaoCredito ccr) {
		
		CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();

		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
				.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));

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
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		return SeguroResponse.toSeguroResponse(seguro);
	}

	// CancelarApoliceSeguro
	@Transactional
	public CancelarSeguroResponse cancelarSeguro(Long id_seguro) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		seguro.setarStatusSeguro(Status.DESATIVADO);
		seguroRepository.save(seguro);
		return CancelarSeguroResponse.toCancelarSeguroResponse(seguro);
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
	public SeguroBase acionarSeguro(Long id_seguro, BigDecimal valor) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		if(seguro.getStatusSeguro().equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Seguro desativado - operação bloqueada");
		if(seguro instanceof SeguroFraude) {
			((SeguroFraude) seguro).setValorFraude(valor);
		}
		seguro.acionarSeguro();
		seguroRepository.save(seguro);
		return seguro;
	}
	
	@Transactional
	// debitarPremioSeguro
	public DebitarPremioSeguroResponse debitarPremioSeguro(Long id_seguro) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		if (seguro.getStatusSeguro().equals(Status.DESATIVADO)) throw new InvalidInputParameterException("Seguro desativado - operação bloqueada");
		if (seguro.getPremioApolice().compareTo(seguro.getCartaoCredito().getConta().getSaldo())>0) throw new InvalidInputParameterException("Saldo insuficiente para esta transação.");
		seguro.aplicarPremio();
		return DebitarPremioSeguroResponse.toDebitarPremioSeguroResponse(seguro);
	}

	public SeguroResponse toResponse(SeguroBase seguro) {
		return SeguroResponse.toSeguroResponse(seguro);
	}

}

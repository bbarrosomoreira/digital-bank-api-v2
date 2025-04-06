//package br.com.cdb.bancodigitaljpa.service;
//
//import java.util.Objects;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import br.com.cdb.bancodigitaljpa.dto.SeguroResponse;
//import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
//import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
//import br.com.cdb.bancodigitaljpa.entity.SeguroFraude;
//import br.com.cdb.bancodigitaljpa.entity.SeguroViagem;
//import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
//import br.com.cdb.bancodigitaljpa.exceptions.CartaoNaoEncontradoException;
//import br.com.cdb.bancodigitaljpa.exceptions.ContaNaoEncontradaException;
//import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
//import br.com.cdb.bancodigitaljpa.repository.SeguroRepository;
//import jakarta.transaction.Transactional;
//
//@Service
//public class SeguroService {
//
//	@Autowired
//	private SeguroRepository seguroRepository;
//
//	@Autowired
//	private CartaoRepository cartaoRepository;
//
//	// contrataSeguro
//	@Transactional
//	public SeguroResponse contratarSeguro(Long id_cartaoCredito, TipoSeguro tipo) {
//		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
//		CartaoCredito ccr = cartaoRepository.findCartaoCreditoById(id_cartaoCredito)
//				.orElseThrow(() -> new CartaoNaoEncontradoException(id_cartaoCredito));
//		SeguroBase seguroNovo = contratarSeguroPorTipo(tipo, ccr);
//		seguroRepository.save(seguroNovo);
//		return SeguroResponse.toSeguroResponse(seguroNovo);
//	}
//
//	public SeguroBase contratarSeguroPorTipo(TipoSeguro tipo, CartaoCredito ccr) {
//		// ver categoria e ver parametros para definir valor do premio
//
//		return switch (tipo) {
//		case FRAUDE -> {
//			SeguroFraude sf = new SeguroFraude(ccr);
//			yield sf;
//		}
//		case VIAGEM -> {
//			SeguroViagem sv = new SeguroViagem(ccr);
//			yield sv;
//		}
//		};
//	}
//
//	// obtemDetalhesApolice
//	// ListaTodosSegurosDisponiveis (suas descricoes)
//	// ListarApolicesContratadas (ativadas?)
//	// CancelarApoliceSeguro
//	// acionarSeguroFraude
//	// acionarSeguroViagem
//	// debitarPremioSeguro
//
//}

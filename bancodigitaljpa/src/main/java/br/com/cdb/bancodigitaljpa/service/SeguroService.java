package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
import br.com.cdb.bancodigitaljpa.entity.SeguroFraude;
import br.com.cdb.bancodigitaljpa.entity.SeguroViagem;
import br.com.cdb.bancodigitaljpa.entity.Usuario;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import br.com.cdb.bancodigitaljpa.exceptions.ErrorMessages;
import br.com.cdb.bancodigitaljpa.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ValidationException;
import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
import br.com.cdb.bancodigitaljpa.repository.ClienteRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.repository.SeguroRepository;
import br.com.cdb.bancodigitaljpa.response.CancelarSeguroResponse;
import br.com.cdb.bancodigitaljpa.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigitaljpa.response.SeguroResponse;
import jakarta.transaction.Transactional;

@Service
public class SeguroService {
	
	private static final Logger log = LoggerFactory.getLogger(SeguroService.class);

	@Autowired
	private SeguroRepository seguroRepository;

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;
	
	@Autowired
	private SecurityService securityService;

	// contrataSeguro
	@Transactional
	public SeguroResponse contratarSeguro(Long id_cartaoCredito, Usuario usuarioLogado, TipoSeguro tipo) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		CartaoCredito ccr = cartaoRepository.findCartaoCreditoById(id_cartaoCredito)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartaoCredito + " não encontrado."));
		securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
		
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
	public SeguroResponse getSeguroById(Long id_seguro, Usuario usuarioLogado) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		securityService.validateAccess(usuarioLogado, seguro.getCartaoCredito().getConta().getCliente());
		return SeguroResponse.toSeguroResponse(seguro);
	}
	
	// get seguro por usuario
	public List<SeguroResponse> listarPorUsuario(Usuario usuario) {
		List<SeguroBase> seguros = seguroRepository.findByCartaoCreditoContaClienteUsuario(usuario);
		return seguros.stream().map(this::toResponse).toList();
	}

	// cancelar apolice seguro
	@Transactional
	public CancelarSeguroResponse cancelarSeguro(Long id_seguro, Usuario usuarioLogado) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		securityService.validateAccess(usuarioLogado, seguro.getCartaoCredito().getConta().getCliente());
		seguro.setarStatusSeguro(Status.DESATIVADO);
		seguroRepository.save(seguro);
		return CancelarSeguroResponse.toCancelarSeguroResponse(seguro);
	}
	
	// deletar seguros de cliente
	@Transactional
	public void deleteSegurosByCliente(Long id_cliente) {
		verificarClienteExistente(id_cliente);
		List<SeguroBase> seguros = seguroRepository.findByClienteId(id_cliente);
		if (seguros.isEmpty()) {
			log.info("Cliente Id {} não possui seguros.", id_cliente);
			return;
		} 
		for (SeguroBase seguro : seguros) {
			try {
				Long id = seguro.getId();
				seguroRepository.delete(seguro);
				log.info("Seguro ID {} deletado com sucesso", id);
				
			} catch (DataIntegrityViolationException e) {
	            log.error("Falha ao deletar seguro ID {}", seguro.getId(), e);
	            throw new ValidationException("Erro ao deletar seguro: " + e.getMessage());
	        }
		}
	}

	// get seguros
	public List<SeguroResponse> getSeguros() {
		List<SeguroBase> seguros = seguroRepository.findAll();
		return seguros.stream().map(this::toResponse).toList();
	}

	// get seguros by cartao
	public List<SeguroResponse> getSeguroByCartaoId(Long id_cartao, Usuario usuarioLogado) {
		CartaoBase cartao = verificarCartaoExistente(id_cartao);
		securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
		List<SeguroBase> seguros = seguroRepository.findByCartaoCreditoId(id_cartao);
		return seguros.stream().map(this::toResponse).toList();
	}

	// get seguros by cliente
	public List<SeguroResponse> getSeguroByClienteId(Long id_cliente, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		List<SeguroBase> seguros = seguroRepository.findByClienteId(id_cliente);
		return seguros.stream().map(this::toResponse).toList();
	}
	
	@Transactional
	// acionarSeguro
	public SeguroBase acionarSeguro(Long id_seguro, Usuario usuarioLogado, BigDecimal valor) {
		SeguroBase seguro = seguroRepository.findById(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		securityService.validateAccess(usuarioLogado, seguro.getCartaoCredito().getConta().getCliente());
		
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
	public Cliente verificarClienteExistente(Long id_cliente) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
		return cliente;
	}
	public CartaoBase verificarCartaoExistente(Long id_cartao) {
		CartaoBase cartao = cartaoRepository.findById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		return cartao;
	}

}

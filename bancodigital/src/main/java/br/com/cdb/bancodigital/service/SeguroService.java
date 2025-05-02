package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Status;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.exceptions.ErrorMessages;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dao.CartaoDAO;
import br.com.cdb.bancodigital.dao.ClienteDAO;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.dao.SeguroDAO;
import br.com.cdb.bancodigital.dto.response.CancelarSeguroResponse;
import br.com.cdb.bancodigital.dto.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigital.dto.response.SeguroResponse;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SeguroService {
	
	private static final Logger log = LoggerFactory.getLogger(SeguroService.class);

	private final SeguroDAO seguroDAO;
	private final CartaoDAO cartaoDAO;
	private final ClienteDAO clienteDAO;
	private final PoliticaDeTaxasDAO politicaDeTaxaDAO;
	private final SecurityService securityService;

	// contrataSeguro
	@Transactional
	public SeguroResponse contratarSeguro(Long id_cartao, Usuario usuarioLogado, TipoSeguro tipo) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		Cartao ccr = cartaoDAO.findCartaoById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());

		Seguro seguroNovo = contratarSeguroPorTipo(tipo, ccr);
		seguroDAO.salvar(seguroNovo);
		return toResponse(seguroNovo);
	}

	public Seguro contratarSeguroPorTipo(TipoSeguro tipo, Cartao ccr) {

		CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();

		PoliticaDeTaxas parametros = politicaDeTaxaDAO.findByCategoria(categoria)
				.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));

		return switch (tipo) {
			case FRAUDE -> {
				Seguro sf = new Seguro(ccr);
				sf.setTipoSeguro(TipoSeguro.FRAUDE);
				sf.setValorApolice(parametros.getValorApoliceFraude());
				sf.setPremioApolice(parametros.getTarifaSeguroFraude());
				sf.setDescricaoCondicoes(TipoSeguro.FRAUDE.getDescricao());
				yield sf;
			}
			case VIAGEM -> {
				Seguro sv = new Seguro(ccr);
				sv.setTipoSeguro(TipoSeguro.VIAGEM);
				sv.setValorApolice(parametros.getValorApoliceViagem());
				sv.setPremioApolice(parametros.getTarifaSeguroViagem());
				sv.setDescricaoCondicoes(TipoSeguro.VIAGEM.getDescricao());
				yield sv;
			}
		};
	}

	// obtemDetalhesApolice
	public SeguroResponse getSeguroById(Long id_seguro, Usuario usuarioLogado) {
		Seguro seguro = seguroDAO.buscarSeguroPorId(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		securityService.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
		return SeguroResponse.toSeguroResponse(seguro);
	}
	
	// get seguro por usuario
	public List<SeguroResponse> listarPorUsuario(Usuario usuario) {
		List<Seguro> seguros = seguroDAO.findByCartaoContaClienteUsuario(usuario);
		return seguros.stream().map(this::toResponse).toList();
	}

	// cancelar apolice seguro
	@Transactional
	public CancelarSeguroResponse cancelarSeguro(Long id_seguro, Usuario usuarioLogado) {
		Seguro seguro = seguroDAO.buscarSeguroPorId(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		securityService.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
		seguro.setarStatusSeguro(Status.INATIVO);
		seguroDAO.salvar(seguro);
		return CancelarSeguroResponse.toCancelarSeguroResponse(seguro);
	}
	
	// deletar seguros de cliente
	@Transactional
	public void deleteSegurosByCliente(Long id_cliente) {
		verificarClienteExistente(id_cliente);
		List<Seguro> seguros = seguroDAO.findSegurosByClienteId(id_cliente);
		if (seguros.isEmpty()) {
			log.info("Cliente Id {} não possui seguros.", id_cliente);
			return;
		} 
		for (Seguro seguro : seguros) {
			try {
				Long id = seguro.getId();
				seguroDAO.deletarSeguroPorId(seguro.getId());
				log.info("Seguro ID {} deletado com sucesso", id);
				
			} catch (DataIntegrityViolationException e) {
	            log.error("Falha ao deletar seguro ID {}", seguro.getId(), e);
	            throw new ValidationException("Erro ao deletar seguro: " + e.getMessage());
	        }
		}
	}

	// get seguros
	public List<SeguroResponse> getSeguros() {
		List<Seguro> seguros = seguroDAO.buscarTodosSeguros();
		return seguros.stream().map(this::toResponse).toList();
	}

	// get seguros by cartao
	public List<SeguroResponse> getSeguroByCartaoId(Long id_cartao, Usuario usuarioLogado) {
		Cartao cartao = verificarCartaoExistente(id_cartao);
		securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
		List<Seguro> seguros = seguroDAO.findByCartaoId(id_cartao);
		return seguros.stream().map(this::toResponse).toList();
	}

	// get seguros by cliente
	public List<SeguroResponse> getSeguroByClienteId(Long id_cliente, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		List<Seguro> seguros = seguroDAO.findSegurosByClienteId(id_cliente);
		return seguros.stream().map(this::toResponse).toList();
	}
	
	@Transactional
	// acionarSeguro
	public Seguro acionarSeguro(Long id_seguro, Usuario usuarioLogado, BigDecimal valor) {
		Seguro seguro = seguroDAO.buscarSeguroPorId(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		securityService.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
		
		if(seguro.getStatusSeguro().equals(Status.INATIVO)) throw new InvalidInputParameterException("Seguro desativado - operação bloqueada");
		if(seguro.getTipoSeguro().equals(TipoSeguro.FRAUDE)) seguro.setValorFraude(valor);

		seguro.acionarSeguro();
		seguroDAO.salvar(seguro);
		return seguro;
	}
	
	@Transactional
	// debitarPremioSeguro
	public DebitarPremioSeguroResponse debitarPremioSeguro(Long id_seguro) {
		Seguro seguro = seguroDAO.buscarSeguroPorId(id_seguro)
				.orElseThrow(() -> new ResourceNotFoundException("Seguro com ID " + id_seguro + " não encontrado."));
		
		if (seguro.getStatusSeguro().equals(Status.INATIVO)) throw new InvalidInputParameterException("Seguro desativado - operação bloqueada");
		if (seguro.getPremioApolice().compareTo(seguro.getCartao().getConta().getSaldo())>0) throw new InvalidInputParameterException("Saldo insuficiente para esta transação.");
		
		seguro.aplicarPremio();
		return DebitarPremioSeguroResponse.toDebitarPremioSeguroResponse(seguro);
	}

	public SeguroResponse toResponse(Seguro seguro) {
		return SeguroResponse.toSeguroResponse(seguro);
	}
	public Cliente verificarClienteExistente(Long id_cliente) {
		return clienteDAO.buscarClienteporId(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
	}
	public Cartao verificarCartaoExistente(Long id_cartao) {
		return cartaoDAO.findCartaoById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
	}

}

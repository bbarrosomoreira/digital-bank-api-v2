package br.com.cdb.bancodigital.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.dto.AcionarSeguroFraudeDTO;
import br.com.cdb.bancodigital.dto.ContratarSeguroDTO;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.dto.response.AcionarSeguroFraudeResponse;
import br.com.cdb.bancodigital.dto.response.AcionarSeguroViagemResponse;
import br.com.cdb.bancodigital.dto.response.CancelarSeguroResponse;
import br.com.cdb.bancodigital.dto.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigital.dto.response.SeguroResponse;
import br.com.cdb.bancodigital.dto.response.TipoSeguroResponse;
import br.com.cdb.bancodigital.service.SeguroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ConstantUtils.SEGURO)
@AllArgsConstructor
@Slf4j
public class SeguroController {
	
	private final SeguroService seguroService;
	
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@PostMapping
	public ResponseEntity<SeguroResponse> contratarSeguro (
			@Valid @RequestBody ContratarSeguroDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_CONTRATACAO_SEGURO);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		SeguroResponse response = seguroService.contratarSeguro(dto.getId_cartao(), usuarioLogado, dto.getTipo());
		log.info(ConstantUtils.SUCESSO_CONTRATACAO_SEGURO);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

	// Listar tipos de Seguros Disponiveis (suas descricoes)
	@GetMapping(ConstantUtils.TIPOS)
	public ResponseEntity<List<TipoSeguroResponse>> listarTiposSeguros(){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_LISTAGEM_TIPO_SEGURO);

		List<TipoSeguroResponse> tipos = Arrays.stream(TipoSeguro.values())
				.map(seguro -> new TipoSeguroResponse(seguro.getNome(), seguro.getDescricao(), seguro.getCondicoes()))
				.toList();
		log.info(ConstantUtils.SUCESSO_LISTAGEM_TIPO_SEGURO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(tipos);
	}
	
	// só admin pode puxar uma lista de todos seguros
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@GetMapping
	public ResponseEntity<List<SeguroResponse>> getSeguros(){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO);

		List<SeguroResponse> seguros = seguroService.getSeguros();
		log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(seguros);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.CARTAO + ConstantUtils.CARTAO_ID)
	public ResponseEntity<List<SeguroResponse>> listarPorCartao(
			@PathVariable Long id_cartao,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO + ConstantUtils.ID_CARTAO, id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<SeguroResponse> seguros = seguroService.getSeguroByCartaoId(id_cartao, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO + ConstantUtils.ID_CARTAO, id_cartao);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(seguros);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.CLIENTE + ConstantUtils.CLIENTE_ID)
	public ResponseEntity<List<SeguroResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO + ConstantUtils.ID_CLIENTE, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<SeguroResponse> seguros = seguroService.getSeguroByClienteId(id_cliente, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO + ConstantUtils.ID_CLIENTE, id_cliente);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(seguros);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.SEGURO_ID)
	public ResponseEntity<SeguroResponse> getSeguroById(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO + ConstantUtils.ID_SEGURO, id_seguro);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		SeguroResponse seguro = seguroService.getSeguroById(id_seguro, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO + ConstantUtils.ID_SEGURO, id_seguro);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(seguro);	
	}
	
	// para usuário logado ver informações de seus cartoes (cliente)
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@GetMapping(ConstantUtils.GET_USUARIO)
	public ResponseEntity<List<SeguroResponse>> buscarSegurosDoUsuario (
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<SeguroResponse> seguros = seguroService.listarPorUsuario(usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(seguros);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping(ConstantUtils.SEGURO_ID + ConstantUtils.CANCELAR_ENDPOINT)
	public ResponseEntity<CancelarSeguroResponse> cancelarSeguro(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_CANCELAMENTO_SEGURO, id_seguro);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		CancelarSeguroResponse response = seguroService.cancelarSeguro(id_seguro, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_CANCELAMENTO_SEGURO);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// só o admin pode confirmar a exclusão de cadastro de seguros
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@DeleteMapping(ConstantUtils.CLIENTE + ConstantUtils.CLIENTE_ID)
	public ResponseEntity<Void> deleteSegurosByCliente(
			@PathVariable Long id_cliente){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_DELETE_SEGURO, id_cliente);

		seguroService.deleteSegurosByCliente(id_cliente);
		log.info(ConstantUtils.SUCESSO_DELETE_SEGURO, id_cliente);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.noContent().build();
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping(ConstantUtils.FRAUDE_ENDPOINT + ConstantUtils.SEGURO_ID + ConstantUtils.ACIONAR_ENDPOINT)
	public ResponseEntity<AcionarSeguroFraudeResponse> acionarSeguroFraude(
			@PathVariable Long id_seguro,
			@Valid @RequestBody AcionarSeguroFraudeDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ACIONAMENTO_SEGURO, ConstantUtils.SEGURO_FRAUDE);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		Seguro seguro = seguroService.acionarSeguro(id_seguro, usuarioLogado, dto.getValorFraude());
		log.info(ConstantUtils.SUCESSO_ACIONAMENTO_SEGURO, ConstantUtils.SEGURO_FRAUDE);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(AcionarSeguroFraudeResponse.toSeguroFraudeResponse(seguro));
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping(ConstantUtils.VIAGEM_ENDPOINT + ConstantUtils.SEGURO_ID + ConstantUtils.ACIONAR_ENDPOINT)
	public ResponseEntity<AcionarSeguroViagemResponse> acionarSeguroViagem(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ACIONAMENTO_SEGURO, ConstantUtils.SEGURO_VIAGEM);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		Seguro seguro = seguroService.acionarSeguro(id_seguro, usuarioLogado, BigDecimal.ZERO);
		log.info(ConstantUtils.SUCESSO_ACIONAMENTO_SEGURO, ConstantUtils.SEGURO_VIAGEM);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(AcionarSeguroViagemResponse.toSeguroViagemResponse(seguro));
	}
	
	// debitar premio seguro quando ativo
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PostMapping(ConstantUtils.SEGURO_ID + ConstantUtils.PREMIO_ENDPOINT)
	public ResponseEntity<DebitarPremioSeguroResponse> debitarPremioSeguro(
			@PathVariable Long id_seguro){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_DEBITO_PREMIO_SEGURO, id_seguro);

		DebitarPremioSeguroResponse response = seguroService.debitarPremioSeguro(id_seguro);
		log.info(ConstantUtils.SUCESSO_DEBITO_PREMIO_SEGURO);
		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
		
	}
	
	
	
}

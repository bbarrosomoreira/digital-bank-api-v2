package br.com.cdb.bancodigital.controller;

import java.util.List;

import br.com.cdb.bancodigital.application.core.domain.dto.response.*;
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

import br.com.cdb.bancodigital.application.core.domain.dto.AjustarLimiteDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.AlterarSenhaDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.AlterarStatusCartaoDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.EmitirCartaoDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.PagamentoDTO;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.service.CartaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ConstantUtils.CARTAO)
@AllArgsConstructor
@Slf4j
public class CartaoController {
	
	private final CartaoService cartaoService;
	
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@PostMapping
	public ResponseEntity<CartaoResponse> emitirCartao(
			@Valid @RequestBody EmitirCartaoDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_EMISSAO_CARTAO);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		CartaoResponse response = cartaoService.emitirCartao(dto.getId_conta(), usuarioLogado, dto.getTipoCartao(), dto.getSenha());
		log.info(ConstantUtils.SUCESSO_EMISSAO_CARTAO, response.getId());

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	//get cartao
	@GetMapping(ConstantUtils.CARTAO_ID)
	public ResponseEntity<CartaoResponse> getCartaoById(
			@PathVariable Long id_cartao,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO + ConstantUtils.ID_CARTAO, id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		CartaoResponse cartao = cartaoService.getCartaoById(id_cartao, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO + ConstantUtils.ID_CARTAO, id_cartao);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(cartao);
	}

	//outros gets
	// só admin pode puxar uma lista de todos cartoes
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@GetMapping
	public ResponseEntity<List<CartaoResponse>> getCartoes(){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO);

		List<CartaoResponse> cartoes = cartaoService.getCartoes();
		log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(cartoes);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.CLIENTE + ConstantUtils.CLIENTE_ID)
	public ResponseEntity<List<CartaoResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO + ConstantUtils.ID_CLIENTE, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<CartaoResponse> cartoes = cartaoService.listarPorCliente(id_cliente, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO + ConstantUtils.ID_CLIENTE, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(cartoes);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.CONTA + ConstantUtils.CONTA_ID)
	public ResponseEntity<List<CartaoResponse>> listarPorConta(
			@PathVariable Long id_conta,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO + ConstantUtils.ID_CONTA, id_conta);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<CartaoResponse> cartoes = cartaoService.listarPorConta(id_conta, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO + ConstantUtils.ID_CONTA, id_conta);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(cartoes);
	}
	
	// para usuário logado ver informações de seus cartoes (cliente)
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@GetMapping(ConstantUtils.GET_USUARIO)
	public ResponseEntity<List<CartaoResponse>> buscarCartoesDoUsuario (
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<CartaoResponse> cartoes = cartaoService.listarPorUsuario(usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(cartoes);
	}
	
	// deletar cartoes by cliente
	// só o admin pode confirmar a exclusão de cadastro de cartões
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@DeleteMapping(ConstantUtils.CLIENTE + ConstantUtils.CLIENTE_ID)
	public ResponseEntity<Void> deleteCartoesByCliente (
			@PathVariable Long id_cliente) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_EXCLUSAO_CARTAO, id_cliente);

		cartaoService.deleteCartoesByCliente(id_cliente);
		log.info(ConstantUtils.SUCESSO_EXCLUSAO_CARTAO, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.noContent().build();
	}
	
	
	//post pagamento
	// só cliente pode fazer comprar com o cartão
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@PostMapping(ConstantUtils.CARTAO_ID + ConstantUtils.PAGAMENTO_ENDPOINT)
	public ResponseEntity<PagamentoResponse> pagar(
			@PathVariable Long id_cartao, 
			@Valid @RequestBody PagamentoDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_PAGAMENTO_CARTAO, id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		PagamentoResponse response = cartaoService.pagar(id_cartao, usuarioLogado, dto.getValor(), dto.getSenha(), dto.getDescricao());
		log.info(ConstantUtils.SUCESSO_PAGAMENTO_CARTAO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	//put alterar limite
	// só o admin pode confirmar a alteração de limites
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PutMapping(ConstantUtils.CARTAO_ID + ConstantUtils.LIMITE_ENDPOINT)
	public ResponseEntity<LimiteResponse> alterarLimite(@PathVariable Long id_cartao, @Valid @RequestBody AjustarLimiteDTO dto){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ALTERACAO_LIMITE, id_cartao);

		LimiteResponse response = cartaoService.alterarLimite(id_cartao, dto.getLimiteNovo());
		log.info(ConstantUtils.SUCESSO_ALTERACAO_LIMITE);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}

	//put alterar status
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping(ConstantUtils.CARTAO_ID + ConstantUtils.STATUS_ENDPOINT)
	public ResponseEntity<StatusCartaoResponse> alterarStatus(
			@PathVariable Long id_cartao, 
			@Valid @RequestBody AlterarStatusCartaoDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ALTERACAO_STATUS, id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		StatusCartaoResponse response = cartaoService.alterarStatus(id_cartao, usuarioLogado, dto.getStatus());
		log.info(ConstantUtils.SUCESSO_ALTERACAO_STATUS);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	//put alterar senha
	// só cliente pode alterar a senha do cartão
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@PutMapping(ConstantUtils.CARTAO_ID + ConstantUtils.SENHA_ENDPOINT)
	public ResponseEntity<String> alterarSenha(
			@PathVariable Long id_cartao,
			@Valid @RequestBody AlterarSenhaDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ALTERACAO_SENHA, id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		cartaoService.alterarSenha(id_cartao, usuarioLogado, dto.getSenhaAntiga(), dto.getSenhaNova());
		log.info(ConstantUtils.SUCESSO_ALTERACAO_SENHA);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(ConstantUtils.SUCESSO_ALTERACAO_SENHA);
	}

	//get fatura
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.CARTAO_ID + ConstantUtils.FATURA_ENDPOINT)
	public ResponseEntity<FaturaResponse> getFatura(
			@PathVariable Long id_cartao,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_LEITURA_FATURA, id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		FaturaResponse response = cartaoService.getFatura(id_cartao, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_LEITURA_FATURA);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
		
	}
	
	//post pagamento fatura
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PostMapping(ConstantUtils.CARTAO_ID + ConstantUtils.FATURA_ENDPOINT + ConstantUtils.PAGAMENTO_ENDPOINT)
	public ResponseEntity<FaturaPagaResponse> pagarFatura(
			@PathVariable Long id_cartao,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_PAGAMENTO_FATURA, id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		FaturaPagaResponse response = cartaoService.pagarFatura(id_cartao, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_PAGAMENTO_FATURA);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}

	//put ressetar limite diario
	// só o admin pode ressetar o limite
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PutMapping(ConstantUtils.CARTAO_ID + ConstantUtils.LIMITE_DIARIO)
	public ResponseEntity<RessetarLimiteDiarioResponse> ressetarDebito(@PathVariable Long id_cartao){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_RESET_LIMITE, id_cartao);

		RessetarLimiteDiarioResponse response = cartaoService.ressetarDebito(id_cartao);
		log.info(ConstantUtils.SUCESSO_RESET_LIMITE);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
		
	}

}

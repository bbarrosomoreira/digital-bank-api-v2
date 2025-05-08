package br.com.cdb.bancodigital.controller;

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

import br.com.cdb.bancodigital.dto.AjustarLimiteDTO;
import br.com.cdb.bancodigital.dto.AlterarSenhaDTO;
import br.com.cdb.bancodigital.dto.AlterarStatusCartaoDTO;
import br.com.cdb.bancodigital.dto.EmitirCartaoDTO;
import br.com.cdb.bancodigital.dto.PagamentoDTO;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.dto.response.FaturaResponse;
import br.com.cdb.bancodigital.dto.response.LimiteResponse;
import br.com.cdb.bancodigital.dto.response.PagamentoResponse;
import br.com.cdb.bancodigital.dto.response.RessetarLimiteDiarioResponse;
import br.com.cdb.bancodigital.dto.response.StatusCartaoResponse;
import br.com.cdb.bancodigital.service.CartaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartoes")
@AllArgsConstructor
@Slf4j
public class CartaoController {
	
	private final CartaoService cartaoService;
	
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping
	public ResponseEntity<CartaoResponse> emitirCartao(
			@Valid @RequestBody EmitirCartaoDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando emissão de cartão para conta ID: {}.", dto.getId_conta());

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		CartaoResponse response = cartaoService.emitirCartao(dto.getId_conta(), usuarioLogado, dto.getTipoCartao(), dto.getSenha());
		log.info("Cartão emitido com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Emissão de cartão concluída em {} ms.", endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	//get cartao
	@GetMapping("/{id_cartao}")
	public ResponseEntity<CartaoResponse> getCartaoById(
			@PathVariable Long id_cartao,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Buscando informações do cartão ID: {}.", id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		CartaoResponse cartao = cartaoService.getCartaoById(id_cartao, usuarioLogado);
		log.info("Informações do cartão obtidas com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Busca de informações do cartão concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(cartao);
	}

	//outros gets
	// só admin pode puxar uma lista de todos cartoes
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<CartaoResponse>> getCartoes(){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de todos os cartões.");

		List<CartaoResponse> cartoes = cartaoService.getCartoes();
		log.info(ConstantUtils.CARTAO_ENCONTRADO);

		long endTime = System.currentTimeMillis();
		log.info("Busca de cartões concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(cartoes);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<CartaoResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Buscando cartões para cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<CartaoResponse> cartoes = cartaoService.listarPorCliente(id_cliente, usuarioLogado);
		log.info(ConstantUtils.CARTAO_ENCONTRADO);

		long endTime = System.currentTimeMillis();
		log.info("Busca de cartões por cliente concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(cartoes);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/conta/{id_conta}")
	public ResponseEntity<List<CartaoResponse>> listarPorConta(
			@PathVariable Long id_conta,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Buscando cartões para conta ID: {}.", id_conta);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<CartaoResponse> cartoes = cartaoService.listarPorConta(id_conta, usuarioLogado);
		log.info(ConstantUtils.CARTAO_ENCONTRADO);

		long endTime = System.currentTimeMillis();
		log.info("Busca de cartões por conta concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(cartoes);
	}
	
	// para usuário logado ver informações de seus cartoes (cliente)
	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("/meus-cartoes")
	public ResponseEntity<List<CartaoResponse>> buscarCartoesDoUsuario (
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Buscando cartões do usuário logado.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<CartaoResponse> cartoes = cartaoService.listarPorUsuario(usuarioLogado);
		log.info(ConstantUtils.CARTAO_ENCONTRADO);

		long endTime = System.currentTimeMillis();
		log.info("Busca de cartões do usuário logado concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(cartoes);
	}
	
	// deletar cartoes by cliente
	// só o admin pode confirmar a exclusão de cadastro de cartões
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/cliente/{id_cliente}")
	public ResponseEntity<Void> deleteCartoesByCliente (
			@PathVariable Long id_cliente) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando exclusão de cartões para cliente ID: {}.", id_cliente);

		cartaoService.deleteCartoesByCliente(id_cliente);
		log.info("Cartões excluídos com sucesso para cliente ID: {}.", id_cliente);

		long endTime = System.currentTimeMillis();
		log.info("Exclusão de cartões concluída em {} ms.", endTime - startTime);
		return ResponseEntity.noContent().build();
	}
	
	
	//post pagamento
	// só cliente pode fazer comprar com o cartão
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping("/{id_cartao}/pagamento")
	public ResponseEntity<PagamentoResponse> pagar(
			@PathVariable Long id_cartao, 
			@Valid @RequestBody PagamentoDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando pagamento com cartão ID: {}.", id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		PagamentoResponse response = cartaoService.pagar(id_cartao, usuarioLogado, dto.getValor(), dto.getSenha(), dto.getDescricao());
		log.info("Pagamento realizado com sucesso");

		long endTime = System.currentTimeMillis();
		log.info("Pagamento concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	//put alterar limite
	// só o admin pode confirmar a alteração de limites
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_cartao}/limite")
	public ResponseEntity<LimiteResponse> alterarLimite(@PathVariable Long id_cartao,@Valid @RequestBody AjustarLimiteDTO dto){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando alteração de limite para cartão ID: {}.", id_cartao);

		LimiteResponse response = cartaoService.alterarLimite(id_cartao, dto.getLimiteNovo());
		log.info("Limite alterado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Alteração de limite concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}

	//put alterar status
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping("/{id_cartao}/status")
	public ResponseEntity<StatusCartaoResponse> alterarStatus(
			@PathVariable Long id_cartao, 
			@Valid @RequestBody AlterarStatusCartaoDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando alteração de status para cartão ID: {}.", id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		StatusCartaoResponse response = cartaoService.alterarStatus(id_cartao, usuarioLogado, dto.getStatus());
		log.info("Status alterado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Alteração de status concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	//put alterar senha
	// só cliente pode alterar a senha do cartão
	@PreAuthorize("hasRole('CLIENTE')")
	@PutMapping("/{id_cartao}/senha")
	public ResponseEntity<String> alterarSenha(
			@PathVariable Long id_cartao,
			@Valid @RequestBody AlterarSenhaDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando alteração de senha para cartão ID: {}.", id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		cartaoService.alterarSenha(id_cartao, usuarioLogado, dto.getSenhaAntiga(), dto.getSenhaNova());
		log.info("Senha alterada com sucesso");

		long endTime = System.currentTimeMillis();
		log.info("Alteração de senha concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok("Senha alterada com sucesso.");
	}

	//get fatura
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/{id_cartao}/fatura")
	public ResponseEntity<FaturaResponse> getFatura(
			@PathVariable Long id_cartao,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando leitura de fatura para cartão ID: {}.", id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		FaturaResponse response = cartaoService.getFatura(id_cartao, usuarioLogado);
		log.info("Fatura obtida com sucesso");

		long endTime = System.currentTimeMillis();
		log.info("Leitura de fatura concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
		
	}
	
	//post pagamento fatura
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PostMapping("/{id_cartao}/fatura/pagamento")
	public ResponseEntity<FaturaResponse> pagarFatura(
			@PathVariable Long id_cartao,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando pagamento de fatura para cartão ID: {}.", id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		FaturaResponse response = cartaoService.pagarFatura(id_cartao, usuarioLogado);
		log.info("Fatura paga com sucesso");

		long endTime = System.currentTimeMillis();
		log.info("Pagamento de fatura concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}

	//put ressetar limite diario
	// só o admin pode ressetar o limite
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_cartao}/limite-diario")
	public ResponseEntity<RessetarLimiteDiarioResponse> ressetarDebito(@PathVariable Long id_cartao){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando reset de limite diário para cartão ID: {}.", id_cartao);

		RessetarLimiteDiarioResponse response = cartaoService.ressetarDebito(id_cartao);
		log.info("Limite diário redefinido com sucesso");

		long endTime = System.currentTimeMillis();
		log.info("Reset de limite diário concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
		
	}

}

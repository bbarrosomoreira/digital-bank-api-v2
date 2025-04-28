package br.com.cdb.bancodigitaljpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.cdb.bancodigitaljpa.dto.AjustarLimiteDTO;
import br.com.cdb.bancodigitaljpa.dto.AlterarSenhaDTO;
import br.com.cdb.bancodigitaljpa.dto.AlterarStatusCartaoDTO;
import br.com.cdb.bancodigitaljpa.dto.EmitirCartaoDTO;
import br.com.cdb.bancodigitaljpa.dto.PagamentoDTO;
import br.com.cdb.bancodigitaljpa.model.Usuario;
import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.response.CartaoResponse;
import br.com.cdb.bancodigitaljpa.response.FaturaResponse;
import br.com.cdb.bancodigitaljpa.response.LimiteResponse;
import br.com.cdb.bancodigitaljpa.response.PagamentoResponse;
import br.com.cdb.bancodigitaljpa.response.RessetarLimiteDiarioResponse;
import br.com.cdb.bancodigitaljpa.response.StatusCartaoResponse;
import br.com.cdb.bancodigitaljpa.service.CartaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
	
	@Autowired
	private CartaoService cartaoService;
	
	// ambos podem emitir novo cartao
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping
	public ResponseEntity<CartaoResponse> emitirCartao(
			@Valid @RequestBody EmitirCartaoDTO dto,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		CartaoResponse response = cartaoService.emitirCartao(dto.getId_conta(), usuarioLogado, dto.getTipoCartao(), dto.getSenha());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	//get cartao
	@GetMapping("/{id_cartao}")
	public ResponseEntity<CartaoResponse> getCartaoById(
			@PathVariable Long id_cartao,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		CartaoResponse cartao = cartaoService.getCartaoById(id_cartao, usuarioLogado);
		return ResponseEntity.ok(cartao);
	}

	//outros gets
	// só admin pode puxar uma lista de todos cartoes
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<CartaoResponse>> getCartoes(){
		List<CartaoResponse> cartoes = cartaoService.getCartoes();
		return ResponseEntity.ok(cartoes);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<CartaoResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		List<CartaoResponse> cartoes = cartaoService.listarPorCliente(id_cliente, usuarioLogado);
		return ResponseEntity.ok(cartoes);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/conta/{id_conta}")
	public ResponseEntity<List<CartaoResponse>> listarPorConta(
			@PathVariable Long id_conta,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		List<CartaoResponse> cartoes = cartaoService.listarPorConta(id_conta, usuarioLogado);
		return ResponseEntity.ok(cartoes);
	}
	
	// para usuário logado ver informações de seus cartoes (cliente)
	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("/meus-cartoes")
	public ResponseEntity<List<CartaoResponse>> buscarCartoesDoUsuario (
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		List<CartaoResponse> cartoes = cartaoService.listarPorUsuario(usuarioLogado);
		return ResponseEntity.ok(cartoes);
	}
	
	// deletar cartoes by cliente
	// só o admin pode confirmar a exclusão de cadastro de cartões
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/cliente/{id_cliente}")
	public ResponseEntity<Void> deleteCartoesByCliente (
			@PathVariable Long id_cliente) {
		cartaoService.deleteCartoesByCliente(id_cliente);
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
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		PagamentoResponse response = cartaoService.pagar(id_cartao, usuarioLogado, dto.getValor(), dto.getSenha(), dto.getDescricao());
		return ResponseEntity.ok(response);
	}
	
	//put alterar limite
	// só o admin pode confirmar a alteração de limites
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_cartao}/limite")
	public ResponseEntity<LimiteResponse> alterarLimite(@PathVariable Long id_cartao,@Valid @RequestBody AjustarLimiteDTO dto){
		LimiteResponse response = cartaoService.alterarLimite(id_cartao, dto.getLimiteNovo());
		return ResponseEntity.ok(response);
	}

	//put alterar status
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping("/{id_cartao}/status")
	public ResponseEntity<StatusCartaoResponse> alterarStatus(
			@PathVariable Long id_cartao, 
			@Valid @RequestBody AlterarStatusCartaoDTO dto,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Status statusNovo = dto.getStatus();
		StatusCartaoResponse response = cartaoService.alterarStatus(id_cartao, usuarioLogado, statusNovo);
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
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		cartaoService.alterarSenha(id_cartao, usuarioLogado, dto.getSenhaAntiga(), dto.getSenhaNova());
		return ResponseEntity.ok("Senha alterada com sucesso.");
	}

	//get fatura
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/{id_cartao}/fatura")
	public ResponseEntity<FaturaResponse> getFatura(
			@PathVariable Long id_cartao,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		FaturaResponse response = cartaoService.getFatura(id_cartao, usuarioLogado);
		return ResponseEntity.ok(response);
		
	}
	
	//post pagamento fatura
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PostMapping("/{id_cartao}/fatura/pagamento")
	public ResponseEntity<FaturaResponse> pagarFatura(
			@PathVariable Long id_cartao,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		FaturaResponse response = cartaoService.pagarFatura(id_cartao, usuarioLogado);
		return ResponseEntity.ok(response);
	}

	//put ressetar limite diario
	// só o admin pode ressetar o limite
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_cartao}/limite-diario")
	public ResponseEntity<RessetarLimiteDiarioResponse> ressetarDebito(@PathVariable Long id_cartao){
		RessetarLimiteDiarioResponse response = cartaoService.ressetarDebito(id_cartao);
		return ResponseEntity.ok(response);
		
	}

}

package br.com.cdb.bancodigitaljpa.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.dto.AlterarSenhaDTO;
import br.com.cdb.bancodigitaljpa.dto.CartaoResponse;
import br.com.cdb.bancodigitaljpa.dto.EmitirCartaoDTO;
import br.com.cdb.bancodigitaljpa.dto.FaturaResponse;
import br.com.cdb.bancodigitaljpa.dto.PagamentoDTO;
import br.com.cdb.bancodigitaljpa.dto.PagamentoResponse;
import br.com.cdb.bancodigitaljpa.enums.StatusCartao;
import br.com.cdb.bancodigitaljpa.service.CartaoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
	
	@Autowired
	private CartaoService cartaoService;
	
	//post emitir novo cartao
	@PostMapping("/add")
	public ResponseEntity<CartaoResponse> emitirCartao(@RequestBody EmitirCartaoDTO dto) {
		CartaoResponse cartaoNovo = cartaoService.emitirCartao(dto.getId_conta(), dto.getTipoCartao(), dto.getSenha());
		return ResponseEntity.status(HttpStatus.CREATED).body(cartaoNovo);
	}
	
	//get cartao
	@GetMapping("/{id_cartao}")
	public ResponseEntity<CartaoResponse> getCartaoById(@PathVariable Long id_cartao){
		CartaoResponse cartao = cartaoService.getCartaoById(id_cartao);
		return ResponseEntity.ok(cartao);
	}

	//outros gets
	@GetMapping("/listAll")
	public ResponseEntity<List<CartaoResponse>> getCartoes(){
		List<CartaoResponse> cartoes = cartaoService.getCartoes();
		return ResponseEntity.ok(cartoes);
	}
	
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<CartaoResponse>> listarPorCliente(
			@PathVariable Long id_cliente ){
		List<CartaoResponse> cartoes = cartaoService.listarPorCliente(id_cliente);
		return ResponseEntity.ok(cartoes);
	}
	
	@GetMapping("/conta/{id_conta}")
	public ResponseEntity<List<CartaoResponse>> listarPorConta(
			@PathVariable Long id_conta){
		List<CartaoResponse> cartoes = cartaoService.listarPorConta(id_conta);
		return ResponseEntity.ok(cartoes);
	}
	
//	//post pagamento
	@PostMapping("/{id_cartao}/pagamento")
	public ResponseEntity<PagamentoResponse> pagar(
			@PathVariable Long id_cartao,
			@RequestBody PagamentoDTO dto){
		PagamentoResponse response = cartaoService.pagar(id_cartao, dto.getValor(), dto.getSenha(), dto.getDescricao());
		return ResponseEntity.ok(response);
	}
	
//	//put alterar limite
	@PutMapping("/{id_cartao}/limite")
	public ResponseEntity<String> alterarLimite(
			@PathVariable Long id_cartao,
			@RequestBody BigDecimal limiteNovo){
		cartaoService.alterarLimite(id_cartao, limiteNovo);
		return ResponseEntity.ok("Limite alterado com sucesso. Novo limite ajustado para: R$" + limiteNovo);
	}

//	//put alterar status
	@PutMapping("/{id_cartao}/status")
	public ResponseEntity<String> alterarStatus(
			@PathVariable Long id_cartao,
			@RequestBody StatusCartao statusNovo){
		cartaoService.alterarStatus(id_cartao, statusNovo);
		return ResponseEntity.ok("O cartão " + cartaoService.getCartaoById(id_cartao).getNumCartao() + " está "+ statusNovo.toString().toLowerCase());
	}
	
//	//put alterar senha
	@PutMapping("/{id_cartao}/senha")
	public ResponseEntity<String> alterarSenha(
			@PathVariable Long id_cartao,
			@RequestBody AlterarSenhaDTO dto){
		cartaoService.alterarSenha(id_cartao, dto.getSenhaAntiga(), dto.getSenhaNova());
		return ResponseEntity.ok("Senha alterada com sucesso.");
	}

//	//get fatura
	@GetMapping("/{id_cartao}/fatura")
	public ResponseEntity<FaturaResponse> getFatura(
			@PathVariable Long id_cartao){
		FaturaResponse response = cartaoService.getFatura(id_cartao);
		return ResponseEntity.ok(response);
		
	}
	
//	//post pagamento fatura
	@PostMapping("/{id_cartao}/fatura/pagamento")
	public ResponseEntity<String> pagarFatura(@PathVariable Long id_cartao){
		cartaoService.pagarFatura(id_cartao);
		return ResponseEntity.ok("Fatura paga com sucesso! Seu limite para pagamentos é de R$" + cartaoService.getCartaoById(id_cartao).getLimite());
	}

//	//put ressetar limite diario
	@PutMapping("/{id_cartao}/limite-diario")
	public ResponseEntity<String> ressetarDebito(@PathVariable Long id_cartao){
		cartaoService.ressetarDebito(id_cartao);
		return ResponseEntity.ok("Limite diário reiniciado. Seu limite para pagamentos é de R$" + cartaoService.getCartaoById(id_cartao).getLimite());
		
	}

}

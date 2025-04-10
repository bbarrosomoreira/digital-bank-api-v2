package br.com.cdb.bancodigitaljpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	//post emitir novo cartao
	@PostMapping
	public ResponseEntity<CartaoResponse> emitirCartao(@Valid @RequestBody EmitirCartaoDTO dto) {
		CartaoResponse response = cartaoService.emitirCartao(dto.getId_conta(), dto.getTipoCartao(), dto.getSenha());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	//get cartao
	@GetMapping("/{id_cartao}")
	public ResponseEntity<CartaoResponse> getCartaoById(@PathVariable Long id_cartao){
		CartaoResponse cartao = cartaoService.getCartaoById(id_cartao);
		return ResponseEntity.ok(cartao);
	}

	//outros gets
	@GetMapping
	public ResponseEntity<List<CartaoResponse>> getCartoes(){
		List<CartaoResponse> cartoes = cartaoService.getCartoes();
		return ResponseEntity.ok(cartoes);
	}
	
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<CartaoResponse>> listarPorCliente(@PathVariable Long id_cliente ){
		List<CartaoResponse> cartoes = cartaoService.listarPorCliente(id_cliente);
		return ResponseEntity.ok(cartoes);
	}
	
	@GetMapping("/conta/{id_conta}")
	public ResponseEntity<List<CartaoResponse>> listarPorConta(@PathVariable Long id_conta){
		List<CartaoResponse> cartoes = cartaoService.listarPorConta(id_conta);
		return ResponseEntity.ok(cartoes);
	}
	
	// deletar cartoes by cliente
	@DeleteMapping("/cliente/{id_cliente}")
	public ResponseEntity<Void> deleteCartoesByCliente (
			@PathVariable Long id_cliente) {
		cartaoService.deleteCartoesByCliente(id_cliente);
		return ResponseEntity.noContent().build();
	}
	
	
//	//post pagamento
	@PostMapping("/{id_cartao}/pagamento")
	public ResponseEntity<PagamentoResponse> pagar(@PathVariable Long id_cartao, @Valid @RequestBody PagamentoDTO dto){
		PagamentoResponse response = cartaoService.pagar(id_cartao, dto.getValor(), dto.getSenha(), dto.getDescricao());
		return ResponseEntity.ok(response);
	}
	
//	//put alterar limite
	@PutMapping("/{id_cartao}/limite")
	public ResponseEntity<LimiteResponse> alterarLimite(@PathVariable Long id_cartao,@Valid @RequestBody AjustarLimiteDTO dto){
		LimiteResponse response = cartaoService.alterarLimite(id_cartao, dto.getLimiteNovo());
		return ResponseEntity.ok(response);
	}

//	//put alterar status
	@PutMapping("/{id_cartao}/status")
	public ResponseEntity<StatusCartaoResponse> alterarStatus(@PathVariable Long id_cartao, @Valid @RequestBody AlterarStatusCartaoDTO dto){
		Status statusNovo = dto.getStatus();
		StatusCartaoResponse response = cartaoService.alterarStatus(id_cartao, statusNovo);
		return ResponseEntity.ok(response);
	}
	
//	//put alterar senha
	@PutMapping("/{id_cartao}/senha")
	public ResponseEntity<String> alterarSenha(
			@PathVariable Long id_cartao,
			@Valid @RequestBody AlterarSenhaDTO dto){
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
	public ResponseEntity<FaturaResponse> pagarFatura(@PathVariable Long id_cartao){
		FaturaResponse response = cartaoService.pagarFatura(id_cartao);
		return ResponseEntity.ok(response);
	}

//	//put ressetar limite diario
	@PutMapping("/{id_cartao}/limite-diario")
	public ResponseEntity<RessetarLimiteDiarioResponse> ressetarDebito(@PathVariable Long id_cartao){
		RessetarLimiteDiarioResponse response = cartaoService.ressetarDebito(id_cartao);
		return ResponseEntity.ok(response);
		
	}

}

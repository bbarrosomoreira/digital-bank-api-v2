package br.com.cdb.bancodigitaljpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.dto.CartaoResponse;
import br.com.cdb.bancodigitaljpa.dto.EmitirCartaoDTO;
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
	
	//outros gets
	
	//get cartao
	@GetMapping("/{id_cartao}")
	public ResponseEntity<CartaoResponse> getCartaoById(@PathVariable Long id_cartao){
		CartaoResponse cartao = cartaoService.getCartaoById(id_cartao);
		return ResponseEntity.ok(cartao);
	}

//	//post pagamento
//	@PostMapping("/{id_cartao}/pagamento")
//	//cartaoService.pagar(id_conta, valor)
//	//criar PagamentoResponse
//	
//	//put alterar limite
//	@PutMapping("/{id_cartao}/limite")
//	//cartaoService.alterLimite(id_conta, valor)
//	
//	//put alterar status
//	@PutMapping("/{id_cartao}/status")
//	//cartaoService.alterStatus(id_conta, statusNovo)
//	
//	//put alterar senha
//	@PutMapping("/{id_cartao}/senha")
//	//cartaoService.alterSenha(id_conta, senhaAntiga, senhaNova)
//	
//	//get fatura
//	@GetMapping("/{id_cartao}/fatura")
//	//cartaoService.getFatura(id_conta)
//	
//	//post pagamento fatura
//	@PostMapping("/{id_cartao}/fatura/pagamento")
//	//cartaoService.pagarFatura(id_conta)
//	
//	//put ressetar limite diario
//	@PutMapping("/{id_cartao}/limite-diario")
//	//cartaoService.ressetarDebito(id_conta)

}

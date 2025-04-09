package br.com.cdb.bancodigitaljpa.entity;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.Status;

public interface Cartao {
	String getDescricaoTipoCartao();
	void realizarPagamento(BigDecimal valor);
	void alterarLimite(BigDecimal limiteNovo);
	void alterarStatus(Status statusNovo);
	void alterarSenha(String senhaAntiga, String senhaNova);
	
}

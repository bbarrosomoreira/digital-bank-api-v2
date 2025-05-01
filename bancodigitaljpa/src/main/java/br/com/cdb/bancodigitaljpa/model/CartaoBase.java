package br.com.cdb.bancodigitaljpa.model;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.enums.Status;

public interface CartaoBase {
	String getDescricaoTipoCartao();
	void realizarPagamento(BigDecimal valor);
	void alterarLimite(BigDecimal limiteNovo);
	void alterarStatus(Status statusNovo);
	void alterarSenha(String senhaAntiga, String senhaNova);
	
}

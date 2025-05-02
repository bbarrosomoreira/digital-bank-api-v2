package br.com.cdb.bancodigital.model;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.model.enums.Status;

public interface CartaoBase {
	void realizarPagamento(BigDecimal valor);
	void alterarLimite(BigDecimal limiteNovo);
	void alterarStatus(Status statusNovo);
	void alterarSenha(String senhaAntiga, String senhaNova);
	
}

package br.com.cdb.bancodigitaljpa.entity;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.exceptions.LimiteInsuficienteException;
import br.com.cdb.bancodigitaljpa.exceptions.SenhaIncorretaException;

public interface Cartao {
	String getTipo();
	void realizarPagamento(BigDecimal valor) throws LimiteInsuficienteException;
	void alterarLimite(BigDecimal limiteNovo);
	void alterarStatus(Status statusNovo);
	void alterarSenha(String senhaAntiga, String senhaNova) throws SenhaIncorretaException; 
	
}

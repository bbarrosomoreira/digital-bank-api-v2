package br.com.cdb.bancodigitaljpa.entity;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import br.com.cdb.bancodigitaljpa.exceptions.LimiteInsuficienteException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DEBITO")
public class CartaoDebito extends CartaoBase {

	//limiteDiario
	//limiteAtual
	//totalGastos
	// alterar limite diario
	
	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void realizarPagamento(BigDecimal valor) throws LimiteInsuficienteException {
		// TODO Auto-generated method stub
	}
	@Override
	public void alterarLimite() {
		// TODO Auto-generated method stub
	}
	@Override
	public void alterarStatus() {
		// TODO Auto-generated method stub
	}
	@Override
	public TipoCartao getTipoCartao() {
		return TipoCartao.DEBITO;
	}
}

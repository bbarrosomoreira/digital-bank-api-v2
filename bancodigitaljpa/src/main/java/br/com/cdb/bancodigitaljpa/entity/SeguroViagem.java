package br.com.cdb.bancodigitaljpa.entity;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("VIAGEM")
public class SeguroViagem extends SeguroBase {
	
	@Enumerated(EnumType.STRING)
	private Status statusSeguro;
	
	public Status getStatusSeguro() {
		return statusSeguro;
	}
	public void setStatusSeguro(Status statusSeguro) {
		this.statusSeguro = statusSeguro;
	}
	
	//C
	public SeguroViagem () {}
	public SeguroViagem(CartaoCredito ccr) {
		super(ccr);
		this.statusSeguro = Status.ATIVADO;
		this.setDescricaoCondicoes(TipoSeguro.VIAGEM.getDescricao());
		this.setValorApolice(BigDecimal.valueOf(10000.00));
	}
	
	//M
	@Override
	@Transient
	public TipoSeguro getTipoSeguro() {
		return TipoSeguro.VIAGEM;
	}
	public String getTipo() {
		return TipoSeguro.VIAGEM.getDescricao();
	}
	@Override
	public void setarStatusSeguro(Status statusNovo) {
		this.setStatusSeguro(statusNovo);
	}
	@Override
	public void acionarSeguro() {
		this.getCartaoCredito().getConta().sacar(getPremioApolice());
	}
	

}

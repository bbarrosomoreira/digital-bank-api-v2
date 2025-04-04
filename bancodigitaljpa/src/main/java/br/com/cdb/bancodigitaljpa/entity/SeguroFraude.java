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
@DiscriminatorValue("FRAUDE")
public class SeguroFraude extends SeguroBase {
	
	@Enumerated(EnumType.STRING)
	private Status statusSeguroFraude;

	//G&S
	public Status getStatusSeguroFraude() {
		return statusSeguroFraude;
	}
	public void setStatusSeguroFraude(Status statusSeguroFraude) {
		this.statusSeguroFraude = statusSeguroFraude;
	}
	
	//C
	public SeguroFraude (CartaoCredito ccr) {
		super(ccr);
		this.statusSeguroFraude = Status.ATIVADO;
		this.setDescricaoCondicoes(TipoSeguro.FRAUDE.getDescricao());
		this.setValorApolice(BigDecimal.valueOf(5000.00));
		this.setPremioApolice(BigDecimal.ZERO);
	}
	
	//M
	@Override
	@Transient
	public TipoSeguro getTipoSeguro() {
		return TipoSeguro.FRAUDE;
	}
	public String getTipo() {
		return TipoSeguro.FRAUDE.getDescricao();
	}
	

}

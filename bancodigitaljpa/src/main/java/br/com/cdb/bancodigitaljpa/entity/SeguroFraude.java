package br.com.cdb.bancodigitaljpa.entity;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	private Status statusSeguro;
	
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataAcionamento;
	
	private BigDecimal valorFraude;

	//G&S
	public Status getStatusSeguro() {
		return statusSeguro;
	}
	public void setStatusSeguro(Status statusSeguro) {
		this.statusSeguro = statusSeguro;
	}
	public BigDecimal getValorFraude() {
		return valorFraude;
	}
	public void setValorFraude(BigDecimal valor) {
		this.valorFraude = valor;
	}
	public LocalDate getDataAcionamento() {
		return dataAcionamento;
	}
	public void setDataAcionamento(LocalDate dataAcionamento) {
		this.dataAcionamento = dataAcionamento;
	}
	
	//C
	public SeguroFraude () {}
	public SeguroFraude (CartaoCredito ccr) {
		super(ccr);
		this.statusSeguro = Status.ATIVADO;
		this.setDescricaoCondicoes(TipoSeguro.FRAUDE.getDescricao());
		this.setValorApolice(BigDecimal.valueOf(5000.00));
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
	@Override
	public void setarStatusSeguro(Status statusNovo) {
		this.setStatusSeguro(statusNovo);
	}
	
	public BigDecimal getValorRessarcido() {
		BigDecimal valor;
		if(valorFraude.compareTo(getValorApolice())>0) {
			valor = this.getValorApolice();
		} else {
			valor = valorFraude;
		}
		return valor;
	}

	@Override
	public void acionarSeguro() {
		if(valorFraude == null) throw new IllegalArgumentException("Valor da fraude é obrigatório!");
		BigDecimal valorRessarcido = getValorRessarcido();
		this.getCartaoCredito().getConta().depositar(valorRessarcido);
		this.setDataAcionamento(LocalDate.now());
	}

}

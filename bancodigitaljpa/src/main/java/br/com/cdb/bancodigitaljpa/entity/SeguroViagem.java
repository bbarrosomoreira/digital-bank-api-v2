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
@DiscriminatorValue("VIAGEM")
public class SeguroViagem extends SeguroBase {
	
	@Enumerated(EnumType.STRING)
	private Status statusSeguro;
	
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataAcionamento;
	
	public Status getStatusSeguro() {
		return statusSeguro;
	}
	public void setStatusSeguro(Status statusSeguro) {
		this.statusSeguro = statusSeguro;
	}
	public LocalDate getDataAcionamento() {
		return dataAcionamento;
	}
	public void setDataAcionamento(LocalDate dataAcionamento) {
		this.dataAcionamento = dataAcionamento;
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
		this.setarStatusSeguro(Status.ATIVADO);
		this.setDataAcionamento(LocalDate.now());
	}
	public void aplicarPremio() {
		this.getCartaoCredito().getConta().sacar(getPremioApolice());
	}
	

}

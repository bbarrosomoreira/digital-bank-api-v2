//package br.com.cdb.bancodigitaljpa.entity;
//
//import java.beans.Transient;
//import java.math.BigDecimal;
//
//import br.com.cdb.bancodigitaljpa.enums.Status;
//import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
//import jakarta.persistence.DiscriminatorValue;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//
//@Entity
//@DiscriminatorValue("VIAGEM")
//public class SeguroViagem extends SeguroBase{
//	
//	@Enumerated(EnumType.STRING)
//	private Status statusSeguroViagem;
//	
//	public Status getStatusSeguroViagem() {
//		return statusSeguroViagem;
//	}
//	public void setStatusSeguroViagem(Status statusSeguroViagem) {
//		this.statusSeguroViagem = statusSeguroViagem;
//	}
//	
//	//C
//	public SeguroViagem(CartaoCredito ccr) {
//		super(ccr);
//		this.statusSeguroViagem = Status.ATIVADO;
//		this.setDescricaoCondicoes(TipoSeguro.VIAGEM.getDescricao());
//		this.setValorApolice(BigDecimal.valueOf(10000.00));
//	}
//	
//	//M
//	@Override
//	@Transient
//	public TipoSeguro getTipoSeguro() {
//		return TipoSeguro.VIAGEM;
//	}
//	public String getTipo() {
//		return TipoSeguro.VIAGEM.getDescricao();
//	}
//	
//	
//	
//
//}

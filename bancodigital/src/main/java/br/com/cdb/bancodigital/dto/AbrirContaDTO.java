package br.com.cdb.bancodigital.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class AbrirContaDTO {
	
	private Long id_cliente;
	
	@NotNull(message = "Tipo de conta é obrigatório")
	@Enumerated(EnumType.STRING)
	private TipoConta tipoConta;
	
	@NotNull(message = "Moeda da conta é obrigatório")
	@Enumerated(EnumType.STRING)
	private Moeda moeda;
	
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorDeposito;
	
	
	//G&S
	public Long getId_cliente() {
		return id_cliente;
	}
	public void setId_cliente(Long id_cliente) {
		this.id_cliente = id_cliente;
	}
	public TipoConta getTipoConta() {
		return tipoConta;
	}
	public void setTipoConta(String tipoConta) {
		this.tipoConta = TipoConta.valueOf(tipoConta.toUpperCase());
	}
	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}
	public Moeda getMoeda() {
		return moeda;
	}
	public void setMoeda(String moeda) {
		this.moeda = Moeda.valueOf(moeda.toUpperCase());
	}
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}
	public BigDecimal getValorDeposito() {
		return valorDeposito;
	}
	public void setValorDeposito(BigDecimal valorDeposito) {
		this.valorDeposito = valorDeposito;
	}
	
	public AbrirContaDTO() {}

}

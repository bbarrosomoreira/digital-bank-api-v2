package br.com.cdb.bancodigitaljpa.entity;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import br.com.cdb.bancodigitaljpa.exceptions.LimiteInsuficienteException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CREDITO")
public class CartaoCredito extends CartaoBase {
	
	//limiteCredito
	//limiteAtual
	//fatura
	//pagarFatura() e consultar
	@Column(name = "limite_mensal_de_credito", precision = 19, scale = 2)
	private BigDecimal limiteCredito;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal limiteAtual;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal totalFatura;
	
	//C
	public CartaoCredito() {}
	public CartaoCredito(ContaBase conta) {
		super(conta);
		this.limiteAtual = this.limiteCredito;
		this.totalFatura = BigDecimal.ZERO;
	}
	
	//G&S
	// virá dos parâmetros
	public BigDecimal getLimiteCredito() {
		return limiteCredito;
	}
	public void setLimiteCredito(BigDecimal limiteCredito) {
		this.limiteCredito = limiteCredito;
	}
	public BigDecimal getLimiteAtual() {
		return limiteAtual;
	}
	public void setLimiteAtual() {
		this.limiteAtual = this.limiteCredito;
	}
	public void setLimiteAtual(BigDecimal limiteAtual) {
		this.limiteAtual = limiteAtual;
	}
	public BigDecimal getTotalFatura() {
		return totalFatura;
	}
	public void setTotalFatura() {
		this.totalFatura = BigDecimal.ZERO;
	}
	public void setTotalFatura(BigDecimal totalFatura) {
		this.totalFatura = totalFatura;
	}
	
	//M
	@Override
	@Transient
	public TipoCartao getTipoCartao() {
		return TipoCartao.CREDITO;
	}
	
	@Override
	public String getTipo() {
		return TipoCartao.CREDITO.getDescricao();
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


}

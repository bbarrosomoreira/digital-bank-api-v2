package br.com.cdb.bancodigitaljpa.entity;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CREDITO")
public class CartaoCredito extends CartaoBase {
	
	@Column(name = "limite_mensal_de_credito", precision = 19, scale = 2)
	private BigDecimal limiteCredito;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal limiteAtual;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal totalFatura;
	
	//C
	public CartaoCredito() {}
	public CartaoCredito(ContaBase conta, String senha, BigDecimal limiteCredito) {
		super(conta, senha);
		this.limiteCredito = limiteCredito;
		this.limiteAtual = limiteCredito;
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
	public void setLimiteAtual(BigDecimal limiteAtual) {
		this.limiteAtual = limiteAtual;
	}
	public BigDecimal getTotalFatura() {
		return totalFatura;
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
	public void realizarPagamento(BigDecimal valor) {
		BigDecimal limiteAtualizado = this.limiteAtual.subtract(valor);
		this.setLimiteAtual(limiteAtualizado);
		BigDecimal faturaAtualizada = this.totalFatura.add(valor);
		this.setTotalFatura(faturaAtualizada);
	}
	@Override
	public void alterarLimite(BigDecimal limiteNovo) {
		this.setLimiteCredito(limiteNovo);
	}
	
	//pagarFatura()
	public void pagarFatura() {
		BigDecimal saldoAtualizado = this.getConta().getSaldo().subtract(this.totalFatura);
		this.getConta().setSaldo(saldoAtualizado);
		this.setLimiteAtual(limiteCredito);
		this.setTotalFatura(BigDecimal.ZERO);
	}

}

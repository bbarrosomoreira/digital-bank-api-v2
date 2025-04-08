package br.com.cdb.bancodigitaljpa.entity;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import br.com.cdb.bancodigitaljpa.exceptions.LimiteInsuficienteException;
import br.com.cdb.bancodigitaljpa.exceptions.SaldoInsuficienteException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DEBITO")
public class CartaoDebito extends CartaoBase {

	@Column(name = "limite_diario_de_compras", precision = 19, scale = 2)
	private BigDecimal limiteDiario;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal limiteAtual;
	
	//C
	public CartaoDebito() {}
	public CartaoDebito(ContaBase conta, String senha, BigDecimal limiteDiario) {
		super(conta, senha);
		this.limiteDiario = limiteDiario;
		this.limiteAtual = limiteDiario;
	}
	
	//G&S
	public BigDecimal getLimiteDiario() {
		return limiteDiario;
	}
	public void setLimiteDiario(BigDecimal limiteDiario) {
		this.limiteDiario = limiteDiario;
	}
	public BigDecimal getLimiteAtual() {
		return limiteAtual;
	}
	public void setLimiteAtual(BigDecimal limiteAtual) {
		this.limiteAtual = limiteAtual;
	}
	
	//M
	@Override
	@Transient
	public TipoCartao getTipoCartao() {
		return TipoCartao.DEBITO;
	}
	@Override
	public String getTipo() {
		return TipoCartao.DEBITO.getDescricao();
	}
	@Override
	public void realizarPagamento(BigDecimal valor) throws LimiteInsuficienteException, SaldoInsuficienteException {
		if(valor.compareTo(limiteAtual)>0) {
			throw new LimiteInsuficienteException(this.getId_cartao(), this.getNumeroCartao(), this.getLimiteAtual());
		}
		if(valor.compareTo(this.getConta().getSaldo())>0) {
			throw new SaldoInsuficienteException(this.getConta().getId(), this.getConta().getNumeroConta(), this.getConta().getSaldo());
		}
		BigDecimal limiteAtualizado = this.limiteAtual.subtract(valor);
		this.setLimiteAtual(limiteAtualizado);
		BigDecimal saldoAtualizado = this.getConta().getSaldo().subtract(valor);
		this.getConta().setSaldo(saldoAtualizado);
		
	}
	@Override
	public void alterarLimite(BigDecimal limiteNovo) {
		this.setLimiteDiario(limiteNovo);
	}
	
	public void ressetarLimiteDiario() {
		this.setLimiteAtual(this.getLimiteDiario());
	}
}

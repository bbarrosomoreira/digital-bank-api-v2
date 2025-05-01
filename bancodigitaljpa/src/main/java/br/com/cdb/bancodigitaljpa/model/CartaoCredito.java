package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.enums.TipoCartao;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartaoCredito extends CartaoBase {

	private BigDecimal limiteCredito;
	private BigDecimal limiteAtual;
	private BigDecimal totalFatura;
	private BigDecimal totalFaturaPaga;
	
	public CartaoCredito(ContaBase conta, String senha, BigDecimal limiteCredito) {
		super(conta, senha);
		this.limiteCredito = limiteCredito;
		this.limiteAtual = limiteCredito;
		this.totalFatura = BigDecimal.ZERO;
	}
	
	@Override
	@Transient
	public TipoCartao getTipoCartao() {
		return TipoCartao.CREDITO;
	}	
	@Override
	public String getDescricaoTipoCartao() {
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
		this.setTotalFaturaPaga(this.totalFatura);
		BigDecimal saldoAtualizado = this.getConta().getSaldo().subtract(this.totalFatura);
		this.getConta().setSaldo(saldoAtualizado);
		this.setLimiteAtual(limiteCredito);
		this.setTotalFatura(BigDecimal.ZERO);
	}

}

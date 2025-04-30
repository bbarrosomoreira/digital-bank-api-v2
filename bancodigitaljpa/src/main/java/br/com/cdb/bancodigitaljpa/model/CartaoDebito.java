package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartaoDebito extends CartaoBase {

	private BigDecimal limiteDiario;
	private BigDecimal limiteAtual;
	
	public CartaoDebito(ContaBase conta, String senha, BigDecimal limiteDiario) {
		super(conta, senha);
		this.limiteDiario = limiteDiario;
		this.limiteAtual = limiteDiario;
	}
	
	@Override
	@Transient
	public TipoCartao getTipoCartao() {
		return TipoCartao.DEBITO;
	}
	@Override
	public String getDescricaoTipoCartao() {
		return TipoCartao.DEBITO.getDescricao();
	}
	@Override
	public void realizarPagamento(BigDecimal valor) {	
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

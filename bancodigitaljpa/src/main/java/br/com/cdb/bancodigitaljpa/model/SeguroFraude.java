package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SeguroFraude extends SeguroBase {
	
	private Status statusSeguro;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataAcionamento;
	private BigDecimal valorFraude;

	public SeguroFraude (CartaoCredito ccr) {
		super(ccr);
		this.statusSeguro = Status.ATIVO;
		this.setDescricaoCondicoes(TipoSeguro.FRAUDE.getDescricao());
		this.setValorApolice(BigDecimal.valueOf(5000.00));
	}
	
	@Override
	@Transient
	public TipoSeguro getTipoSeguro() {
		return TipoSeguro.FRAUDE;
	}
	public String getDescricaoTipoSeguro() {
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
		this.getCartaoCredito().getConta().depositar(getValorRessarcido());
		this.setDataAcionamento(LocalDate.now());
	}
	public void aplicarPremio() {
		this.getCartaoCredito().getConta().sacar(getPremioApolice());
	}

}

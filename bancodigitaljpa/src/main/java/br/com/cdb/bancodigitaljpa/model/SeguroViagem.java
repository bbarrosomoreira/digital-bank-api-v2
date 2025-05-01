package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.model.enums.Status;
import br.com.cdb.bancodigitaljpa.model.enums.TipoSeguro;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SeguroViagem extends SeguroBase {
	
	private Status statusSeguro;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataAcionamento;
	
	public SeguroViagem(CartaoCredito ccr) {
		super(ccr);
		this.statusSeguro = Status.ATIVO;
		this.setDescricaoCondicoes(TipoSeguro.VIAGEM.getDescricao());
		this.setValorApolice(BigDecimal.valueOf(10000.00));
	}
	
	@Override
	@Transient
	public TipoSeguro getTipoSeguro() {
		return TipoSeguro.VIAGEM;
	}
	public String getDescricaoTipoSeguro() {
		return TipoSeguro.VIAGEM.getDescricao();
	}
	@Override
	public void setarStatusSeguro(Status statusNovo) {
		this.setStatusSeguro(statusNovo);
	}
	@Override
	public void acionarSeguro() {
		this.setarStatusSeguro(Status.ATIVO);
		this.setDataAcionamento(LocalDate.now());
	}
	public void aplicarPremio() {
		this.getCartaoCredito().getConta().sacar(getPremioApolice());
	}
	

}

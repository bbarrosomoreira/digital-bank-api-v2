package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferenciaDTO {
	
	@NotNull (message = "ID da conta de destino é obrigatório")
	private Long id_contaDestino;
	
	@Positive(message = "O valor deve ser positivo")
	@DecimalMin(value = "1.00", message = "O valor mínimo é R$1,00")
	private BigDecimal valor;

	//G&S
	public Long getId_contaDestino() {
		return id_contaDestino;
	}
	public void setId_contaDestino(Long id_contaDestino) {
		this.id_contaDestino = id_contaDestino;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	//constructor
	public TransferenciaDTO() {
	}
	
	

}

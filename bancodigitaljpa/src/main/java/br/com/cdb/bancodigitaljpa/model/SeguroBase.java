package br.com.cdb.bancodigitaljpa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public abstract class SeguroBase implements Seguro {
	
	private Long id_seguro;
	private String numApolice;
	private CartaoCredito cartaoCredito;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataContratacao;
	private BigDecimal valorApolice;
	@Size(max=300, message="A descrição deve ter no máximo 300 caracteres.")
	private String descricaoCondicoes;
	private BigDecimal premioApolice;

	public SeguroBase(CartaoCredito ccr) {
		super();
		this.cartaoCredito = ccr;
		this.dataContratacao = LocalDate.now();
		this.numApolice = gerarNumeroApolice();
	}

	private String gerarNumeroApolice() {
		return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}
	public abstract TipoSeguro getTipoSeguro();
	public abstract Status getStatusSeguro();
	
}

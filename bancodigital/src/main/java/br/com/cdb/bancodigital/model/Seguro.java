package br.com.cdb.bancodigital.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.NumberGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.model.enums.Status;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Seguro implements SeguroBase {
	
	private Long id;
	private TipoSeguro tipoSeguro;
	private String numApolice;
	private Cartao cartao;
	@JsonFormat(pattern = ConstantUtils.FORMATO_DATA_DD_MM_YYYY)
	private LocalDate dataContratacao;
	private BigDecimal valorApolice;
	@Size(max = 300, message = ConstantUtils.DESCRICAO_TAMANHO)
	private String descricaoCondicoes;
	private BigDecimal premioApolice;
	private Status statusSeguro;
	@JsonFormat(pattern = ConstantUtils.FORMATO_DATA_DD_MM_YYYY)
	private LocalDate dataAcionamento;
	private BigDecimal valorFraude;

	public Seguro(Cartao cartao) {
		super();
		this.cartao = cartao;
		this.statusSeguro = Status.ATIVO;
		this.dataContratacao = LocalDate.now();
		this.numApolice = NumberGenerator.gerarNumeroApolice();
	}

	public void setarStatusSeguro(Status statusNovo) {
		this.setStatusSeguro(statusNovo);
	}

	@Override
	public void acionarSeguro() {
		switch (tipoSeguro) {
			case FRAUDE -> {
				this.getCartao().getConta().depositar(getValorRessarcido());
				this.setDataAcionamento(LocalDate.now());
			}
			case VIAGEM -> {
				this.setarStatusSeguro(Status.ATIVO);
				this.setDataAcionamento(LocalDate.now());
			}
		}

	}

	public void aplicarPremio() {
		this.getCartao().getConta().sacar(getPremioApolice());
	}

	// SF
	public BigDecimal getValorRessarcido() {
		BigDecimal valor;
		if(valorFraude.compareTo(getValorApolice())>0) {
			valor = this.getValorApolice();
		} else {
			valor = valorFraude;
		}
		return valor;
	}
	
}

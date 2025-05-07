package br.com.cdb.bancodigital.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigital.utils.NumberGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.model.enums.Status;
import br.com.cdb.bancodigital.model.enums.TipoCartao;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Cartao implements CartaoBase {
	
	private Long id;
	private TipoCartao tipoCartao;
	private String numeroCartao;
	private Conta conta;
	private Status status;
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senha;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataEmissao;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataVencimento;
	private BigDecimal taxaUtilizacao;
	private BigDecimal limite;
	private BigDecimal limiteAtual;
	private BigDecimal totalFatura;
	private BigDecimal totalFaturaPaga;

		
	public Cartao(Conta conta, String senha, TipoCartao tipoCartao) {
		super();
		this.conta = conta;
		this.tipoCartao = tipoCartao;
		this.dataEmissao = LocalDate.now();
		this.dataVencimento = this.dataEmissao.plusYears(5);
		this.status = Status.ATIVO;
		definirSenha(senha);
		this.numeroCartao = NumberGenerator.gerarNumeroCartao();
	}

	public void realizarPagamento(BigDecimal valor) {
		BigDecimal limiteAtualizado = this.limiteAtual.subtract(valor);
		this.setLimiteAtual(limiteAtualizado);
		switch (tipoCartao) {
			case CREDITO -> {
				BigDecimal faturaAtualizada = this.totalFatura.add(valor);
				this.setTotalFatura(faturaAtualizada);
			}
			case DEBITO -> {
				BigDecimal saldoAtualizado = this.getConta().getSaldo().subtract(valor);
				this.getConta().setSaldo(saldoAtualizado);
			}
		}
	}

	public void alterarLimite(BigDecimal limiteNovo) {
		this.setLimite(limiteNovo);
	}
	
	@Override
	public void alterarSenha(String senhaAntiga, String senhaNova) {
		definirSenha(senhaNova);
	}
	
	@Override
	public void alterarStatus(Status statusNovo) {
		this.setStatus(statusNovo);
	}
	
	public void definirSenha(String senhaNova) {
		if (senhaNova == null || !senhaNova.matches("\\d{4}")) {
			throw new IllegalArgumentException("A senha deve ter exatamente 4 dígitos numéricos.");
		}
		this.senha = senhaNova;
	}

	// CC
	public void pagarFatura() {
		this.setTotalFaturaPaga(this.totalFatura);
		BigDecimal saldoAtualizado = this.getConta().getSaldo().subtract(this.totalFatura);
		this.getConta().setSaldo(saldoAtualizado);
		this.setLimiteAtual(limite);
		this.setTotalFatura(BigDecimal.ZERO);
	}

	// CD
	public void reiniciarLimiteDebito() {
		this.setLimiteAtual(this.getLimite());
	}

}

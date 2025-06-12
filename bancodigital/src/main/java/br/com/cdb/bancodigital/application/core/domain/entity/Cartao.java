package br.com.cdb.bancodigital.application.core.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.NumberGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Status;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoCartao;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	@Size(min = 4, max = 4, message = ConstantUtils.SENHA_TAMANHO)
	private String senha;
	@JsonFormat(pattern = ConstantUtils.FORMATO_DATA_DD_MM_YYYY)
	private LocalDate dataEmissao;
	@JsonFormat(pattern = ConstantUtils.FORMATO_DATA_DD_MM_YYYY)
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
		if (this.limite.equals(this.limiteAtual))
			this.setLimiteAtual(limiteNovo);
		this.setLimite(limiteNovo);

	}
	
	@Override
	public void alterarSenha(String senhaAntiga, String senhaNova, PasswordEncoder passwordEncoder) {
		// Valida a senha antiga
		if (!passwordEncoder.matches(senhaAntiga, this.senha)) {
			throw new IllegalArgumentException(ConstantUtils.SENHA_ANTIGA_INCORRETA);
		}

		// Valida o formato da nova senha
		if (senhaNova == null || !senhaNova.matches("\\d{4}")) {
			throw new IllegalArgumentException(ConstantUtils.SENHA_TAMANHO);
		}

		// Criptografa e define a nova senha
		this.senha = passwordEncoder.encode(senhaNova);
	}
	
	@Override
	public void alterarStatus(Status statusNovo) {
		this.setStatus(statusNovo);
	}
	
	public void definirSenha(String senhaCriptografada) {
		// Define diretamente a senha já criptografada
		this.senha = senhaCriptografada;
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


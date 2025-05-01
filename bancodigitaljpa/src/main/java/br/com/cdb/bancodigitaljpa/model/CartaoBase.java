package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.model.enums.Status;
import br.com.cdb.bancodigitaljpa.model.enums.TipoCartao;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public abstract class CartaoBase implements Cartao {
	
	private Long id_cartao;
	protected String numeroCartao;
	private ContaBase conta;
	private Status status;
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senha;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataEmissao;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataVencimento;
	private BigDecimal taxaUtilizacao;

		
	public CartaoBase(ContaBase conta, String senha) {
		super();
		this.conta = conta;
		this.dataEmissao = LocalDate.now();
		this.dataVencimento = this.dataEmissao.plusYears(5);
		this.status = Status.ATIVO;
		definirSenha(senha);
		gerarNumeroCartao();
	}

	//G&S
	public Long getId() {
		return id_cartao;
	}
	public String getNumeroCartao() {
		return numeroCartao;
	}
	public ContaBase getConta() {
		return conta;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public LocalDate getDataEmissao() {
		return dataEmissao;
	}
	public LocalDate getDataVencimento() {
		return dataVencimento;
	}
	public BigDecimal getTaxaUtilizacao() {
		return taxaUtilizacao;
	}
	public void setTaxaUtilizacao(BigDecimal taxaUtilizacao) {
		this.taxaUtilizacao = taxaUtilizacao;
	}

	//M
	public abstract void realizarPagamento(BigDecimal valor);
	public abstract void alterarLimite(BigDecimal valor);
	public abstract BigDecimal getLimiteAtual();
	
	@Transient
	public abstract TipoCartao getTipoCartao();
	
	@PrePersist
	public void prePersist() {
	    if (this.dataEmissao == null) {
	        this.dataEmissao = LocalDate.now();
	    }
	    if (this.dataVencimento == null) {
	        this.dataVencimento = dataEmissao.plusYears(5);
	    }
	    if (this.numeroCartao == null) {
	    	gerarNumeroCartao();
	    }
	}
	
	protected void gerarNumeroCartao() {
	    Random random = new Random();
	    StringBuilder numeroCartao = new StringBuilder();

	    // Define o primeiro dígito (Visa = 4, Mastercard = 5)
	    int primeiroDigito = random.nextBoolean() ? 4 : 5; 
	    numeroCartao.append(primeiroDigito);

	    // Gera os outros 15 dígitos
	    for (int i = 1; i < 16; i++) {
	        numeroCartao.append(random.nextInt(10)); // Números de 0 a 9
	    }

	    // Aplica a formatação para "XXXX XXXX XXXX XXXX"
	    this.numeroCartao = numeroCartao.toString().replaceAll("(.{4})", "$1 ").trim();
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

}

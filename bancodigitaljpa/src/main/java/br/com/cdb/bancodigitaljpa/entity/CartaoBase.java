package br.com.cdb.bancodigitaljpa.entity;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_cartao", discriminatorType = DiscriminatorType.STRING)
public abstract class CartaoBase implements Cartao {
	
	//A
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_cartao;
	
	@Column(nullable = false, unique = true, updatable = false)
	protected String numeroCartao;
	
	@ManyToOne
	@JoinColumn(name = "contaId", nullable = false, updatable = false)
	private ContaBase conta;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	
	@Column(nullable = false)
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senha;
	
	@Column(name = "data_emissao", nullable = false, updatable = false)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataEmissao;
	
	@Column(name = "data_vencimento", nullable = false, updatable = false)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataVencimento;
	
	@Column(precision = 5, scale = 4)
	private BigDecimal taxaUtilizacao;
	
	//C
	public CartaoBase() {}
		
	public CartaoBase(ContaBase conta, String senha) {
		super();
		this.conta = conta;
		this.dataEmissao = LocalDate.now();
		this.dataVencimento = this.dataEmissao.plusYears(5);
		this.status = Status.ATIVADO;
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

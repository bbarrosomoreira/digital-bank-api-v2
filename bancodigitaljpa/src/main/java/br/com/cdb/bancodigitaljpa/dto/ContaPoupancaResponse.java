package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;

public class ContaPoupancaResponse extends ContaResponse {

	private BigDecimal taxaRendimento;

	//G
	public BigDecimal getTaxaRendimento() {
		return taxaRendimento;
	}

	//C
    public ContaPoupancaResponse(Long id, String numConta,
            Long clienteId, Moeda moeda,
            LocalDate dataCriacao,
            BigDecimal taxaRendimento) {
    	super(id, numConta, TipoConta.POUPANCA, clienteId, moeda, dataCriacao);
    	this.taxaRendimento = taxaRendimento;
}
	
	
}

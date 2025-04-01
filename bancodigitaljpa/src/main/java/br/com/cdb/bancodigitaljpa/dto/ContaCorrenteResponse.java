package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;

public class ContaCorrenteResponse extends ContaResponse {

	private BigDecimal taxaManutencao;
	
	//G
	public BigDecimal getTaxaManutencao() {
		return taxaManutencao;
	}

	//C
    public ContaCorrenteResponse(Long id, String numConta, 
                               Long clienteId, Moeda moeda,
                               LocalDate dataCriacao, 
                               BigDecimal taxaManutencao) {
        super(id, numConta, TipoConta.CORRENTE, clienteId, moeda, dataCriacao);
        this.taxaManutencao = taxaManutencao;
    }
	
	
	
	
}

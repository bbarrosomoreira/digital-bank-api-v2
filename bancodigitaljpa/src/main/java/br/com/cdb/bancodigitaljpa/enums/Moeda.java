package br.com.cdb.bancodigitaljpa.enums;

import java.math.BigDecimal;
import java.text.NumberFormat;

public enum Moeda {
	BRL ("Real Brasileiro", "R$"),
	USD ("Dólar Americano", "US$"),
	EUR ("Euro", "€");
	
	private final String nome;
	private final String simbolo;
	
	Moeda (String nome, String simbolo) {
		this.nome = nome;
		this.simbolo = simbolo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getSimbolo() {
		return simbolo;
	}
	
    // Método para formatar valores
    public String formatar(BigDecimal valor) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        if (this == BRL) {
            format.setCurrency(java.util.Currency.getInstance("BRL"));
        } else if (this == USD) {
            format.setCurrency(java.util.Currency.getInstance("USD"));
        }
        return format.format(valor);
    }

}

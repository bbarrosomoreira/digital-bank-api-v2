package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiConversorMoedasResponse {
	private boolean success;
	private Query query;
	private Info info;
	private BigDecimal result;


    @Setter
    @Getter
    public static class Query {
		private String from;
		private String to;
		private BigDecimal amount;

    }
	
	@Setter
    @Getter
    public static class Info {
		private BigDecimal quote;
		private Long timestamp;


    }
	
	

}

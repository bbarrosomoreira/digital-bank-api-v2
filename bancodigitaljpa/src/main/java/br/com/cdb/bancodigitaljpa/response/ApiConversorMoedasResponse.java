package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiConversorMoedasResponse {
	private boolean success;
	private Query query;
	private Info info;
	private BigDecimal result;
	
	public ApiConversorMoedasResponse() {}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Query getQuery() {
		return query;
	}
	public void setQuery(Query query) {
		this.query = query;
	}
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	public BigDecimal getResult() {
		return result;
	}
	public void setResult(BigDecimal result) {
		this.result = result;
	}
	
	public static class Query {
		private String from;
		private String to;
		private BigDecimal amount;
		
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public BigDecimal getAmount() {
			return amount;
		}
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}		
	}
	
	public static class Info {
		private BigDecimal quote;
		private Long timestamp;
		
		public BigDecimal getQuote() {
			return quote;
		}
		public void setQuote(BigDecimal quote) {
			this.quote = quote;
		}
		public Long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}


	}
	
	

}

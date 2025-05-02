package br.com.cdb.bancodigital.dto.response;

import br.com.cdb.bancodigital.model.EnderecoCliente;

public class EnderecoResponse {
	
	   private String rua;
	    private int numero;
	    private String complemento;
	    private String cidade;
	    private String estado;
	    private String cep;

	    public EnderecoResponse(EnderecoCliente endereco) {
	        this.rua = endereco.getRua();
	        this.numero = endereco.getNumero();
	        this.complemento = endereco.getComplemento();
	        this.cidade = endereco.getCidade();
	        this.estado = endereco.getEstado();
	        this.cep = endereco.getCep();
	    }

	    //G
		public String getRua() {
			return rua;
		}
		public int getNumero() {
			return numero;
		}
		public String getComplemento() {
			return complemento;
		}
		public String getCidade() {
			return cidade;
		}
		public String getEstado() {
			return estado;
		}
		public String getCep() {
			return cep;
		}
		
}

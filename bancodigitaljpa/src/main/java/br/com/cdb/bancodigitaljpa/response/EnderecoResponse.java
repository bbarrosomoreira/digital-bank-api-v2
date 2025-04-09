package br.com.cdb.bancodigitaljpa.response;

import br.com.cdb.bancodigitaljpa.entity.EnderecoCliente;

public class EnderecoResponse {
	
	   private String rua;
	    private int numero;
	    private String complemento;
	    private String cidade;
	    private String estado;
	    private String cep;
	    private boolean enderecoPrincipal;

	    public EnderecoResponse(EnderecoCliente endereco) {
	        this.rua = endereco.getRua();
	        this.numero = endereco.getNumero();
	        this.complemento = endereco.getComplemento();
	        this.cidade = endereco.getCidade();
	        this.estado = endereco.getEstado();
	        this.cep = endereco.getCep();
	        this.enderecoPrincipal = endereco.isEnderecoPrincipal();
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
		public boolean isEnderecoPrincipal() {
			return enderecoPrincipal;
		}
		
}

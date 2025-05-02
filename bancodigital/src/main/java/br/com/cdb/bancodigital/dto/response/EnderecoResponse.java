package br.com.cdb.bancodigital.dto.response;

import br.com.cdb.bancodigital.model.EnderecoCliente;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnderecoResponse {

    private String rua;
    private int numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public EnderecoResponse(EnderecoCliente endereco) {
        this.rua = endereco.getRua();
        this.numero = endereco.getNumero();
        this.complemento = endereco.getComplemento();
        this.bairro = endereco.getBairro();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
        this.cep = endereco.getCep();
    }

}

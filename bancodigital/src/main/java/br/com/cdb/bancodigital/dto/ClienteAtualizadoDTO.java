package br.com.cdb.bancodigital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteAtualizadoDTO {


    // Dados pessoais
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Nome deve conter apenas letras e espaços")
    private String nome;

    @CPF(message = "CPF inválido")
    private String cpf;

    @Past(message = "Data de nascimento dever ser no passado")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataNascimento;

    // Dados de endereço
    @Pattern(regexp = "\\d{8}", message = "CEP deve estar no formato XXXXXXXX")
    private String cep;

    private Integer numero;

    @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
    private String complemento;

    private String rua;
    private String bairro;
    private String cidade;
    private String estado;


}

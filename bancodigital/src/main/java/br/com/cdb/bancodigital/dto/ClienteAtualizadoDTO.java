package br.com.cdb.bancodigital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteAtualizadoDTO {


    // Dados pessoais
    @Size(min = 2, max = 100, message = NOME_TAMANHO)
    @Pattern(regexp = "^[\\p{L} ]+$", message = NOME_FORMATO)
    private String nome;

    @CPF(message = CPF_INVALIDO)
    private String cpf;

    @Past(message = DATA_NASCIMENTO_PASSADO)
    @JsonFormat(pattern = FORMATO_DATA_DD_MM_YYYY)
    private LocalDate dataNascimento;

    // Dados de endereço
    @Pattern(regexp = "\\d{8}", message = CEP_FORMATO)
    private String cep;

    private Integer numero;

    @Size(max = 100, message = COMPLEMENTO_TAMANHO)
    private String complemento;

    private String rua;
    private String bairro;
    private String cidade;
    private String estado;


}

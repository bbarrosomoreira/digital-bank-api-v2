package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
public class ConsultaCpfDTO {
    @NotBlank(message = "CPF é obrigatótio")
    @CPF(message = "CPF inválido")
    private String cpf;
}

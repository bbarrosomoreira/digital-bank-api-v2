package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class ConsultaCpfDTO {
    @NotBlank(message = CPF_OBRIGATORIO)
    @CPF(message = CPF_INVALIDO)
    private String cpf;
}


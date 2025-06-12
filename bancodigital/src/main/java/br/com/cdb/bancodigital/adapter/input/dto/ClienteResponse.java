package br.com.cdb.bancodigital.adapter.input.dto;

import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponse {

    private Long id;
    private String nome;
    private String cpf;
    private CategoriaCliente categoria;
    private LocalDate dataNascimento;
    private Usuario usuario;

}

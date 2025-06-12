package br.com.cdb.bancodigital.adapter.output.model;

import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteModel {

    private Long id;
    private String nome;
    private String cpf;
    private CategoriaCliente categoria;
    private LocalDate dataNascimento;
    private Usuario usuario;

}

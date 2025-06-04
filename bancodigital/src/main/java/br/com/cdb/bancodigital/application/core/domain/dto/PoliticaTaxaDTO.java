package br.com.cdb.bancodigital.application.core.domain.dto;

import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PoliticaTaxaDTO {

    private CategoriaCliente categoria;
}

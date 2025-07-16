package br.com.cdb.bancodigital.adapter.input.mapper;

import br.com.cdb.bancodigital.adapter.input.dto.EnderecoDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Endereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoDTOMapper {

    Endereco toEntity(EnderecoDTO dto);
    EnderecoDTO toDTO(Endereco entity);


}
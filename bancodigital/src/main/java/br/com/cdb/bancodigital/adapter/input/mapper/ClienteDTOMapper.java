package br.com.cdb.bancodigital.adapter.input.mapper;

import br.com.cdb.bancodigital.adapter.input.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteDTOMapper {
    Cliente toEntity(ClienteDTO dto);
    ClienteDTO toDTO(Cliente entity);
}

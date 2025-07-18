package br.com.cdb.bancodigital.adapter.input.mapper;

import br.com.cdb.bancodigital.adapter.input.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UsuarioDTOMapper.class, EnderecoDTOMapper.class})
public interface ClienteDTOMapper {

    @Mapping(source = "endereco", target = ".", ignore = true)
    Cliente toEntity(ClienteDTO dto);

    @Mapping(target = "endereco", ignore = true)
    ClienteDTO toDTO(Cliente entity);
}

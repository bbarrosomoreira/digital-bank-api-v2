package br.com.cdb.bancodigital.adapter.input.mapper;

import br.com.cdb.bancodigital.adapter.input.dto.UsuarioDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioDTOMapper {
    Usuario toEntity(UsuarioDTO dto);
    UsuarioDTO toDTO(Usuario entity);
}

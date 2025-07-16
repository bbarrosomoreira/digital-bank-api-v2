package br.com.cdb.bancodigital.adapter.output.mapper;

import br.com.cdb.bancodigital.adapter.output.model.UsuarioModel;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioPersistenceMapper {
    UsuarioModel toModel(Usuario entity);
    Usuario toEntity(UsuarioModel model);
}

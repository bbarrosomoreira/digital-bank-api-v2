package br.com.cdb.bancodigital.adapter.output.mapper;

import br.com.cdb.bancodigital.adapter.output.model.ClienteModel;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientePersistenceMapper {
    ClienteModel toModel(Cliente entity);
    Cliente toEntity(ClienteModel model);
}

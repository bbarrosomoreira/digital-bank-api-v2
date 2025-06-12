package br.com.cdb.bancodigital.adapter.input.mapper;

import br.com.cdb.bancodigital.adapter.input.dto.ClienteRequest;
import br.com.cdb.bancodigital.adapter.input.dto.ClienteResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteRequestMapper {
    Cliente toEntity(ClienteRequest dto);
    ClienteResponse toResponse(Cliente entity);
}

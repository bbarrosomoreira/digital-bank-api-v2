package br.com.cdb.bancodigital.adapter.output.mapper;

import br.com.cdb.bancodigital.adapter.output.model.EnderecoModel;
import br.com.cdb.bancodigital.application.core.domain.entity.Endereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClientePersistenceMapper.class})
public interface EnderecoPersistenceMapper {
    EnderecoModel toModel(Endereco entity);
    Endereco toEntity(EnderecoModel model);
}

package br.com.cdb.bancodigital.application.port.in.endereco;

import br.com.cdb.bancodigital.adapter.input.dto.ClienteRequest;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.EnderecoCliente;

import java.util.Optional;

public interface EnderecoUseCase {

    void addEndereco(ClienteRequest dto, Cliente cliente);
//    void addEndereco(ClienteRequest dto, Cliente cliente);
    Optional<EnderecoCliente> findByCliente(Cliente cliente);
}

package br.com.cdb.bancodigital.application.port.in.endereco;

import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Endereco;

import java.util.Optional;

public interface EnderecoUseCase {

    void addEndereco(Endereco endereco, Cliente cliente);
//    void addEndereco(ClienteRequest dto, Cliente cliente);
    Optional<Endereco> findByCliente(Cliente cliente);
}

package br.com.cdb.bancodigital.application.port.out.repository;

import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.EnderecoCliente;

import java.util.Optional;

public interface EnderecoClienteRepository {
    EnderecoCliente save(EnderecoCliente enderecoCliente);
    EnderecoCliente add(EnderecoCliente enderecoCliente);
    Optional<EnderecoCliente> findByCliente(Cliente cliente);
    EnderecoCliente update(EnderecoCliente enderecoCliente);
}

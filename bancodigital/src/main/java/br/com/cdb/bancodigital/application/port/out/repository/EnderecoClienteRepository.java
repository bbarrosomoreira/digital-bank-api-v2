package br.com.cdb.bancodigital.application.port.out.repository;

import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Endereco;

import java.util.Optional;

public interface EnderecoClienteRepository {
    Endereco save(Endereco endereco);
    Endereco add(Endereco endereco);
    Optional<Endereco> findByCliente(Cliente cliente);
    Endereco update(Endereco endereco);
}

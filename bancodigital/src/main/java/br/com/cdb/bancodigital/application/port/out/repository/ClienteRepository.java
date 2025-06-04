package br.com.cdb.bancodigital.application.port.out.repository;

import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente save(Cliente cliente);
    Cliente add(Cliente cliente);
    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    Optional<Cliente> findByUsuario(Usuario usuario);
    boolean existsWithCpf(String cpf);
    void validateVinculosCliente(Long id);
    Cliente update(Cliente cliente);
    void updateCondicoesByCategoria(Long id, CategoriaCliente categoria);
    void delete(Long id);

}

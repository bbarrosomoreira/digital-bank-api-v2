package br.com.cdb.bancodigital.application.port.out.repository;

import br.com.cdb.bancodigital.application.core.domain.entity.Seguro;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface SeguroRepository {
    Seguro save(Seguro seguro);
    Seguro addSeguro(Seguro seguro);
    List<Seguro> findAllSeguros();
    Optional<Seguro> findById(Long id);
    List<Seguro> findByCartaoId(Long cartaoId);
    List<Seguro> findyClienteId(Long clienteId);
    List<Seguro> findByCartaoContaClienteUsuario(Usuario usuario);
    Seguro updateSeguro(Seguro seguro);
    void deleteSeguroById(Long id);
}

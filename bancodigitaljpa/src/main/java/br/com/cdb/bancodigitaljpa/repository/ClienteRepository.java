package br.com.cdb.bancodigitaljpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.Usuario;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	boolean existsByCpf(String cpf);
	
	Optional<Cliente> findByUsuario(Usuario usuario);
	Optional<Cliente> findByUsuarioId(Long id);
}

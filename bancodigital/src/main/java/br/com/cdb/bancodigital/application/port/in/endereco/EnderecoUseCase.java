package br.com.cdb.bancodigital.application.port.in.endereco;

import br.com.cdb.bancodigital.application.core.domain.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.EnderecoCliente;

import java.util.Optional;

public interface EnderecoUseCase {

    void addEndereco(ClienteDTO dto, Cliente cliente);
    void addEndereco(ClienteUsuarioDTO dto, Cliente cliente);
    Optional<EnderecoCliente> findByCliente(Cliente cliente);
}

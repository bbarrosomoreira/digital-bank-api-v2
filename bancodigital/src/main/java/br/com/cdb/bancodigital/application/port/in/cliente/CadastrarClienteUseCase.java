package br.com.cdb.bancodigital.application.port.in.cliente;

import br.com.cdb.bancodigital.application.core.domain.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

public interface CadastrarClienteUseCase {

    Cliente addCliente(ClienteDTO dto, Usuario usuario);
    Cliente addCliente(ClienteUsuarioDTO dto);
}

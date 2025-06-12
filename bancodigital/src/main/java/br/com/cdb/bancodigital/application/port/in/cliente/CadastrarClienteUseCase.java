package br.com.cdb.bancodigital.application.port.in.cliente;

import br.com.cdb.bancodigital.application.core.domain.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

public interface CadastrarClienteUseCase {

    ClienteResponse addCliente(ClienteDTO dto, Usuario usuario);
    ClienteResponse addCliente(ClienteUsuarioDTO dto);
}

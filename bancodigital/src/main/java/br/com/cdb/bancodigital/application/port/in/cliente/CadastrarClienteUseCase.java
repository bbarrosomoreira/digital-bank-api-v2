package br.com.cdb.bancodigital.application.port.in.cliente;

import br.com.cdb.bancodigital.adapter.input.dto.ClienteRequest;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

public interface CadastrarClienteUseCase {

    Cliente addCliente(ClienteRequest dto, Usuario usuario);
    Cliente addCliente(ClienteRequest dto);
}

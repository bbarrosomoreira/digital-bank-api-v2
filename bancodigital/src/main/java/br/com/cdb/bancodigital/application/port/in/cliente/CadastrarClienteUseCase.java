package br.com.cdb.bancodigital.application.port.in.cliente;

import br.com.cdb.bancodigital.adapter.input.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Endereco;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

public interface CadastrarClienteUseCase {

    Cliente addCliente(Cliente cliente, Usuario usuario, Endereco endereco);
    Cliente addCliente(Cliente cliente, Endereco endereco);
}

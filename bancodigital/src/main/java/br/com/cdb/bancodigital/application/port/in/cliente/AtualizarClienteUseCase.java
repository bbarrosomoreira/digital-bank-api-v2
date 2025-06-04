package br.com.cdb.bancodigital.application.port.in.cliente;

import br.com.cdb.bancodigital.application.core.domain.dto.ClienteAtualizadoDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;
import org.springframework.security.access.AccessDeniedException;

public interface AtualizarClienteUseCase {
    ClienteResponse updateCliente(Long id_cliente, ClienteAtualizadoDTO dto, Usuario usuario) throws AccessDeniedException;
    void updateCategoriaCliente(Long id_cliente, CategoriaCliente categoria) throws AccessDeniedException;
}

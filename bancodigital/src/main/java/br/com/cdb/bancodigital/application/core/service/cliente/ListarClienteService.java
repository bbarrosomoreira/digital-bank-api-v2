package br.com.cdb.bancodigital.application.core.service.cliente;

import br.com.cdb.bancodigital.application.port.in.cliente.ListarClienteUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.EnderecoClienteRepository;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.EnderecoCliente;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ListarClienteService implements ListarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final EnderecoClienteRepository enderecoClienteRepository;

    public List<ClienteResponse> getClientes() throws AccessDeniedException { //só admin
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(this::toResponse).toList();
    }
    public ClienteResponse getClientePorId(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteRepository, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return toResponse(cliente);
    }
    public ClienteResponse getClientePorUsuario(Usuario usuario) {
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
        Cliente cliente = clienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResourceNotFoundException(ConstantUtils.ERRO_CLIENTE_NAO_ENCONTRADO_USUARIO_LOGADO));
        return toResponse(cliente);
    }
    private ClienteResponse toResponse(Cliente cliente) {
        EnderecoCliente endereco = enderecoClienteRepository.findByCliente(cliente)
                .orElseThrow(()-> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_ENDERECO));
        return new ClienteResponse(cliente, endereco);
    }
}

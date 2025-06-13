package br.com.cdb.bancodigital.adapter.input.controller;

import br.com.cdb.bancodigital.adapter.input.dto.ClienteRequest;
import br.com.cdb.bancodigital.adapter.input.dto.ClienteResponse;
import br.com.cdb.bancodigital.adapter.input.mapper.ClienteRequestMapper;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.port.in.cliente.AtualizarClienteUseCase;
import br.com.cdb.bancodigital.application.port.in.cliente.CadastrarClienteUseCase;
import br.com.cdb.bancodigital.application.port.in.cliente.DeletarClienteUseCase;
import br.com.cdb.bancodigital.application.port.in.cliente.ListarClienteUseCase;
import br.com.cdb.bancodigital.application.core.domain.dto.AtualizarCategoriaClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ConstantUtils.CLIENTE)
@AllArgsConstructor
@Slf4j
public class ClienteController {

    private final CadastrarClienteUseCase cadastrarClienteUseCase;
    private final ListarClienteUseCase listarClienteUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final DeletarClienteUseCase deletarClienteUseCase;
    private final ClienteRequestMapper clienteRequestMapper;

    // Acesso cliente - vincula cliente ao usuário logado
    @PreAuthorize(ConstantUtils.ROLE_CLIENTE)
    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrarCliente(
            @Valid @RequestBody ClienteRequest dto,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_CADASTRO_CLIENTE);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

//        Cliente cliente = clienteRequestMapper.toEntity(dto);

        ClienteResponse response = clienteRequestMapper.toResponse(cadastrarClienteUseCase.addCliente(dto, usuarioLogado));

        log.info(ConstantUtils.SUCESSO_CADASTRO_CLIENTE, response.getId());

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // Acesso admin
    @PreAuthorize(ConstantUtils.ROLE_ADMIN)
    @PostMapping(ConstantUtils.ADMIN)
    public ResponseEntity<ClienteResponse> cadastrarCliente(
            @Valid @RequestBody ClienteRequest dto) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_CADASTRO_CLIENTE);

        Cliente cliente = cadastrarClienteUseCase.addCliente(dto);
        ClienteResponse response = clienteRequestMapper.toResponse(cliente);
        log.info(ConstantUtils.SUCESSO_CADASTRO_CLIENTE, response.getId());

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // para usuário logado ver suas informações (cliente)
    @PreAuthorize(ConstantUtils.ROLE_CLIENTE)
    @GetMapping(ConstantUtils.GET_USUARIO)
    public ResponseEntity<ClienteResponse> buscarClienteDoUsuario(
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        Cliente cliente = listarClienteUseCase.getClientePorUsuario(usuarioLogado);
        log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);

        ClienteResponse clienteResponse = clienteRequestMapper.toResponse(cliente);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(clienteResponse);
    }

    @PreAuthorize(ConstantUtils.ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getClientes() {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);

        List<Cliente> clientes = listarClienteUseCase.getClientes();
        log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);



        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(clientes.stream()
                            .map(clienteRequestMapper::toResponse)
                            .toList());
    }

    // admin tem acesso ao id, cliente só pode ver se for dele
    @GetMapping(ConstantUtils.CLIENTE_ID)
    public ResponseEntity<ClienteResponse> getClienteById(
            @PathVariable Long id_cliente,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_CLIENTE + ConstantUtils.ID_CLIENTE, id_cliente);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        Cliente cliente = listarClienteUseCase.getClientePorId(id_cliente, usuarioLogado);
        log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE + ConstantUtils.ID_CLIENTE, id_cliente);

        ClienteResponse clienteResponse = clienteRequestMapper.toResponse(cliente);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(clienteResponse);
    }

    // só o admin pode confirmar a exclusão de cadastro de cliente
    @PreAuthorize(ConstantUtils.ROLE_ADMIN)
    @DeleteMapping(ConstantUtils.CLIENTE_ID)
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id_cliente){
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_DELETE_CLIENTE, id_cliente);

        deletarClienteUseCase.deleteCliente(id_cliente);
        log.info(ConstantUtils.SUCESSO_DELETE_CLIENTE, id_cliente);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.noContent().build();
    }

    // admin podem atualizar dados cadastrais, cliente só se for dele
    @PutMapping(ConstantUtils.CLIENTE_ID)
    public ResponseEntity<ClienteResponse> updateCliente(
            @PathVariable Long id_cliente,
            @Valid @RequestBody ClienteRequest clienteAtualizado,
            Authentication authentication){
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_ATUALIZACAO_CLIENTE, id_cliente);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        Cliente atualizado = atualizarClienteUseCase.updateCliente(id_cliente, clienteAtualizado, usuarioLogado);
        log.info(ConstantUtils.SUCESSO_ATUALIZACAO_CLIENTE, id_cliente);

        ClienteResponse clienteResponse = clienteRequestMapper.toResponse(atualizado);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(clienteResponse);
    }

    // admin podem atualizar dados cadastrais, cliente só se for dele
    @PatchMapping(ConstantUtils.CLIENTE_ID)
    public ResponseEntity<ClienteResponse> updateParcial(
            @PathVariable Long id_cliente,
            @Valid @RequestBody ClienteRequest clienteAtualizado,
            Authentication authentication){
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_ATUALIZACAO_CLIENTE, id_cliente);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        Cliente atualizado = atualizarClienteUseCase.updateCliente(id_cliente, clienteAtualizado, usuarioLogado);
        log.info(ConstantUtils.SUCESSO_ATUALIZACAO_CLIENTE, id_cliente);

        ClienteResponse clienteResponse = clienteRequestMapper.toResponse(atualizado);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(clienteResponse);
    }

    @PreAuthorize(ConstantUtils.ROLE_ADMIN)
    @PatchMapping(ConstantUtils.CATEGORIA_EP + ConstantUtils.CLIENTE_ID)
    public ResponseEntity<Void> updateCategoriaCliente(
            @PathVariable Long id_cliente,
            @Valid @RequestBody AtualizarCategoriaClienteDTO dto){
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_ATUALIZACAO_CATEGORIA_CLIENTE, id_cliente);

        atualizarClienteUseCase.updateCategoriaCliente(id_cliente, dto.getCategoriaCliente());
        log.info(ConstantUtils.SUCESSO_ATUALIZACAO_CATEGORIA_CLIENTE);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.noContent().build();
    }

}

package br.com.cdb.bancodigital.application.core.service.cliente;
import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.application.port.in.cliente.CadastrarClienteUseCase;
import br.com.cdb.bancodigital.application.core.domain.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.application.port.out.api.BrasilApiPort;
import br.com.cdb.bancodigital.application.port.out.api.ReceitaFederalPort;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.EnderecoClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.UsuarioRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.EnderecoCliente;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
@AllArgsConstructor
@Slf4j
public class CadastrarClienteService implements CadastrarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final EnderecoClienteRepository enderecoClienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final BrasilApiPort brasilApiPort;
    private final ReceitaFederalPort receitaFederalPort;
    private final PasswordEncoder passwordEncoder;

    // Acesso cliente
    @Transactional
    @Override
    public ClienteResponse addCliente(ClienteDTO dto, Usuario usuario) {
        log.info(ConstantUtils.INICIO_CADASTRO_CLIENTE);

        CEP2 cepInfo = buscarEnderecoPorCep(dto.getCep());
        Cliente cliente = criarCliente(dto, usuario);
        log.info(ConstantUtils.CLIENTE_CRIADO);

        validarCliente(cliente);
        log.info(ConstantUtils.VALIDACAO_CLIENTE_CONCLUIDA);

        try {
            salvarCliente(cliente);
            salvarEndereco(dto, cliente, cepInfo);
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_SALVAR_CLIENTE_BANCO, cliente.getId(), e);
            throw new SystemException(ConstantUtils.ERRO_SALVAR_CLIENTE_BANCO + cliente.getId());
        }
        log.info(ConstantUtils.SUCESSO_CADASTRO_CLIENTE, cliente.getId());
        return toResponse(cliente);
    }
    // Acesso admin
    @Transactional
    public ClienteResponse addCliente(ClienteUsuarioDTO dto) {
        log.info(ConstantUtils.INICIO_CADASTRO_CLIENTE);

        CEP2 cepInfo = buscarEnderecoPorCep(dto.getCep());

        Usuario usuario = criarUsuario(dto);
        log.info(ConstantUtils.USUARIO_CRIADO_SUCESSO, usuario.getId());

        Cliente cliente = criarCliente(dto, usuario);
        log.info(ConstantUtils.CLIENTE_CRIADO);

        validarCliente(cliente);
        log.info(ConstantUtils.VALIDACAO_CLIENTE_CONCLUIDA);

        try {
            salvarCliente(cliente);
            salvarEndereco(dto, cliente, cepInfo);
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_SALVAR_CLIENTE_BANCO, cliente.getId(), e);
            throw new SystemException(ConstantUtils.ERRO_SALVAR_CLIENTE_BANCO + cliente.getId());
        }
        log.info(ConstantUtils.SUCESSO_CADASTRO_CLIENTE, cliente.getId());
        return toResponse(cliente);
    }

    private Usuario criarUsuario(ClienteUsuarioDTO dto) {
        log.info(ConstantUtils.CRIANDO_USUARIO);
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        return usuarioRepository.add(dto.getEmail(), senhaCriptografada, dto.getRole());
    }

    private CEP2 buscarEnderecoPorCep(String cep) {
        try {
            CEP2 cepInfo = brasilApiPort.buscarEnderecoPorCep(cep);
            log.info(ConstantUtils.DADOS_CEP_SUCESSO);
            return cepInfo;
        } catch (HttpClientErrorException | ResourceAccessException e) {
            log.error(ConstantUtils.ERRO_BUSCAR_CEP_BRASILAPI, e.getMessage(), e);
            throw new SystemException(ConstantUtils.ERRO_BUSCAR_CEP_MENSAGEM_EXCEPTION);
        }
    }
    private Cliente criarCliente(ClienteDTO dto, Usuario usuario) {
        log.info(ConstantUtils.CRIANDO_CLIENTE);
        Cliente cliente = dto.transformaClienteParaObjeto();
        cliente.setCategoria(CategoriaCliente.COMUM);
        cliente.setUsuario(usuario);
        return cliente;
    }
    private Cliente criarCliente(ClienteUsuarioDTO dto, Usuario usuario) {
        log.info(ConstantUtils.CRIANDO_CLIENTE);
        Cliente cliente = dto.transformaClienteParaObjeto();
        cliente.setCategoria(CategoriaCliente.COMUM);
        cliente.setUsuario(usuario);

        return cliente;
    }
    private void validarCliente(Cliente cliente) {
        if (receitaFederalPort.isCpfInvalidoOuInativo(cliente.getCpf())) {
            log.error(ConstantUtils.CPF_INVALIDO_RECEITA_FEDERAL);
            throw new InvalidInputParameterException(ConstantUtils.CPF_INVALIDO_RECEITA_FEDERAL);
        }
        Validator.validarCpfUnico(clienteRepository, cliente.getCpf());
        log.info(ConstantUtils.CPF_VALIDO_UNICO);

        Validator.validarMaiorIdade(cliente);
        log.info(ConstantUtils.CLIENTE_MAIOR_IDADE);
    }
    private void salvarCliente(Cliente cliente) {
        log.info(ConstantUtils.SALVANDO_CLIENTE_BANCO);
        clienteRepository.save(cliente);
    }
    private void salvarEndereco(ClienteDTO dto, Cliente cliente, CEP2 cepInfo) {
        log.info(ConstantUtils.SALVANDO_ENDERECO_CLIENTE);
        EnderecoCliente enderecoCliente = dto.transformaEnderecoParaObjeto();
        enderecoCliente.setCliente(cliente);
        enderecoCliente.setBairro(cepInfo.getNeighborhood());
        enderecoCliente.setCidade(cepInfo.getCity());
        enderecoCliente.setEstado(cepInfo.getState());
        enderecoCliente.setRua(cepInfo.getStreet());
        enderecoClienteRepository.save(enderecoCliente);
    }
    private void salvarEndereco(ClienteUsuarioDTO dto, Cliente cliente, CEP2 cepInfo) {
        log.info(ConstantUtils.SALVANDO_ENDERECO_CLIENTE);
        EnderecoCliente enderecoCliente = dto.transformaEnderecoParaObjeto();
        enderecoCliente.setCliente(cliente);
        enderecoCliente.setBairro(cepInfo.getNeighborhood());
        enderecoCliente.setCidade(cepInfo.getCity());
        enderecoCliente.setEstado(cepInfo.getState());
        enderecoCliente.setRua(cepInfo.getStreet());
        enderecoClienteRepository.save(enderecoCliente);
    }
    private ClienteResponse toResponse(Cliente cliente) {
        EnderecoCliente endereco = enderecoClienteRepository.findByCliente(cliente)
                .orElseThrow(()-> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_ENDERECO));
        return new ClienteResponse(cliente, endereco);
    }

}

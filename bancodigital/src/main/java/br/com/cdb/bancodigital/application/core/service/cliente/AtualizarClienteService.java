package br.com.cdb.bancodigital.application.core.service.cliente;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.adapter.input.dto.ClienteRequest;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.in.cliente.AtualizarClienteUseCase;
import br.com.cdb.bancodigital.application.port.out.api.BrasilApiPort;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.EnderecoClienteRepository;
import br.com.cdb.bancodigital.application.port.out.api.ReceitaFederalPort;
import br.com.cdb.bancodigital.application.core.domain.dto.ClienteAtualizadoDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.config.exceptions.custom.CommunicationException;
import br.com.cdb.bancodigital.config.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.EnderecoCliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AtualizarClienteService implements AtualizarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final EnderecoClienteRepository enderecoClienteRepository;
    private final BrasilApiPort brasilApiPort;
    private final ReceitaFederalPort receitaFederalPort;
    private final SecurityUseCase securityUseCase;

    @Transactional
    public Cliente updateCliente(Long id_cliente, ClienteRequest dto, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_UPDATE_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteRepository, id_cliente);
        securityUseCase.validateAccess(usuarioLogado, cliente);

        updateDadosCliente(cliente, dto);

        try {
            clienteRepository.save(cliente);
            updateEnderecoSeNecessario(cliente, dto);
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_UPDATE_CLIENTE, id_cliente, e);
            throw new SystemException(ConstantUtils.ERRO_UPDATE_CLIENTE + id_cliente);
        }

        return cliente;
    }
    @Transactional
    public void updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) throws AccessDeniedException {
        log.info(ConstantUtils.INICIO_UPDATE_CATEGORIA_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteRepository, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());
        if (cliente.getCategoria().equals(novaCategoria)) {
            log.error(ConstantUtils.ERRO_CLIENTE_JA_NA_CATEGORIA, id_cliente, novaCategoria);
            throw new InvalidInputParameterException(ConstantUtils.ERRO_CLIENTE_JA_NA_CATEGORIA + id_cliente + novaCategoria);
        }

        try {
            clienteRepository.updateCondicoesByCategoria(id_cliente, novaCategoria);
            log.info(ConstantUtils.SUCESSO_UPDATE_CATEGORIA_CLIENTE, id_cliente, novaCategoria);
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_ACESSO_DADOS, e.getMessage(), e);
            throw new CommunicationException(ConstantUtils.ERRO_ACESSO_DADOS);
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_UPDATE_CATEGORIA_CLIENTE, id_cliente, e.getMessage(), e);
            throw new SystemException(ConstantUtils.ERRO_UPDATE_CATEGORIA_CLIENTE + id_cliente);
        }
    }
    private ClienteResponse toResponse(Cliente cliente) {
        EnderecoCliente endereco = enderecoClienteRepository.findByCliente(cliente)
                .orElseThrow(()-> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_ENDERECO));
        return new ClienteResponse(cliente, endereco);
    }
    private void updateDadosCliente(Cliente cliente, ClienteAtualizadoDTO dto) {
        if (dto.getNome() != null) {
            cliente.setNome(dto.getNome());
        }
        if (dto.getCpf() != null && !dto.getCpf().equals(cliente.getCpf())) {
            if (receitaFederalPort.isCpfInvalidoOuInativo(dto.getCpf())) {
                throw new InvalidInputParameterException(ConstantUtils.CPF_INVALIDO_RECEITA_FEDERAL);
            }
            Validator.validarCpfUnico(clienteRepository, dto.getCpf());
            cliente.setCpf(dto.getCpf());
        }
        if (dto.getDataNascimento() != null) {
            cliente.setDataNascimento(dto.getDataNascimento());
        }
        Validator.validarMaiorIdade(cliente);
    }
    private void updateEnderecoSeNecessario(Cliente cliente, ClienteAtualizadoDTO dto) {
        if (possuiDadosDeEndereco(dto)) {
            EnderecoCliente enderecoExistente = enderecoClienteRepository.findByCliente(cliente).orElse(null);
            if (enderecoExistente == null) {
                EnderecoCliente novoEndereco = construirEndereco(dto, cliente);
                enderecoClienteRepository.save(novoEndereco);
            } else {
                atualizarEnderecoExistente(enderecoExistente, dto);
                enderecoClienteRepository.update(enderecoExistente);
            }
        }
    }
    private boolean possuiDadosDeEndereco(ClienteAtualizadoDTO dto) {
        return dto.getRua() != null || dto.getNumero() != null || dto.getComplemento() != null ||
                dto.getBairro() != null || dto.getCidade() != null || dto.getEstado() != null || dto.getCep() != null;
    }
    private EnderecoCliente construirEndereco(ClienteAtualizadoDTO dto, Cliente cliente) {
        EnderecoCliente endereco = new EnderecoCliente();
        endereco.setCliente(cliente);
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());

        // opcionalmente preencher bairro/cidade/estado via BrasilAPI se cep válido
        if (dto.getCep() != null) {
            CEP2 cepInfo = brasilApiPort.buscarEnderecoPorCep(dto.getCep());
            endereco.setBairro(cepInfo.getNeighborhood());
            endereco.setCidade(cepInfo.getCity());
            endereco.setEstado(cepInfo.getState());
            endereco.setRua(cepInfo.getStreet());
        }

        return endereco;
    }
    private void atualizarEnderecoExistente(EnderecoCliente endereco, ClienteAtualizadoDTO dto) {
        if (dto.getRua() != null) endereco.setRua(dto.getRua());
        if (dto.getNumero() != null) endereco.setNumero(dto.getNumero());
        if (dto.getComplemento() != null) endereco.setComplemento(dto.getComplemento());
        if (dto.getBairro() != null) endereco.setBairro(dto.getBairro());
        if (dto.getCidade() != null) endereco.setCidade(dto.getCidade());
        if (dto.getEstado() != null) endereco.setEstado(dto.getEstado());
        if (dto.getCep() != null) endereco.setCep(dto.getCep());
    }
}

package br.com.cdb.bancodigital.application.core.service.endereco;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.application.core.domain.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Endereco;
import br.com.cdb.bancodigital.application.port.out.api.BrasilApiPort;
import br.com.cdb.bancodigital.application.port.out.repository.EnderecoClienteRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EnderecoService {

    private final EnderecoClienteRepository enderecoClienteRepository;
    private final BrasilApiPort brasilApiPort;

    // Acesso cliente
    public void addEndereco(ClienteDTO dto, Cliente cliente){
        log.info(ConstantUtils.INICIO_CRIAR_ENDERECO);
        CEP2 cepInfo = buscarEnderecoPorCep(dto.getCep());
        salvarEndereco(dto, cliente, cepInfo);
        log.info(ConstantUtils.SUCESSO_CRIAR_ENDERECO);
    }
    // Acesso admin
    public void addEndereco(ClienteUsuarioDTO dto, Cliente cliente) {
        log.info(ConstantUtils.INICIO_CRIAR_ENDERECO);
        CEP2 cepInfo = buscarEnderecoPorCep(dto.getCep());
        salvarEndereco(dto, cliente, cepInfo);
        log.info(ConstantUtils.SUCESSO_CRIAR_ENDERECO);
    }
    public Optional<Endereco> findByCliente(Cliente cliente) {
        return enderecoClienteRepository.findByCliente(cliente);
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
    private void salvarEndereco(ClienteDTO dto, Cliente cliente, CEP2 cepInfo) {
        log.info(ConstantUtils.SALVANDO_ENDERECO_CLIENTE);
        Endereco endereco = dto.transformaEnderecoParaObjeto();
        endereco.setCliente(cliente);
        endereco.setBairro(cepInfo.getNeighborhood());
        endereco.setCidade(cepInfo.getCity());
        endereco.setEstado(cepInfo.getState());
        endereco.setRua(cepInfo.getStreet());
        enderecoClienteRepository.save(endereco);
    }
    private void salvarEndereco(ClienteUsuarioDTO dto, Cliente cliente, CEP2 cepInfo) {
        log.info(ConstantUtils.SALVANDO_ENDERECO_CLIENTE);
        Endereco endereco = dto.transformaEnderecoParaObjeto();
        endereco.setCliente(cliente);
        endereco.setBairro(cepInfo.getNeighborhood());
        endereco.setCidade(cepInfo.getCity());
        endereco.setEstado(cepInfo.getState());
        endereco.setRua(cepInfo.getStreet());
        enderecoClienteRepository.save(endereco);
    }

}

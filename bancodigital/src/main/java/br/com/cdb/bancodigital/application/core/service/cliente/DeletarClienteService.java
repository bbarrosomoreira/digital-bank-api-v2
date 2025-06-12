package br.com.cdb.bancodigital.application.core.service.cliente;

import br.com.cdb.bancodigital.application.port.in.cliente.DeletarClienteUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.config.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class DeletarClienteService implements DeletarClienteUseCase {

    private final ClienteRepository clienteRepository;

    @Transactional
    public void deleteCliente(Long id_cliente) {
        log.info(ConstantUtils.INICIO_DELETE_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteRepository, id_cliente);

        try {
            clienteRepository.validateVinculosCliente(id_cliente);
        } catch (DataAccessException e) {
            log.warn(ConstantUtils.CLIENTE_POSSUI_VINCULOS, id_cliente);
            throw new ValidationException(ConstantUtils.ERRO_CLIENTE_POSSUI_VINCULOS);
        }

        try {
            clienteRepository.delete(cliente.getId());
            log.info(ConstantUtils.SUCESSO_DELETE_CLIENTE, id_cliente);
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_DELETE_CLIENTE, id_cliente, e);
            throw new SystemException(ConstantUtils.ERRO_DELETE_CLIENTE + id_cliente);
        }
    }
}

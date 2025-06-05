package br.com.cdb.bancodigital.application.core.service.seguro;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CancelarSeguroResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Seguro;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Status;
import br.com.cdb.bancodigital.application.port.in.seguro.DeletarSeguroUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.SeguroRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.config.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DeletarSeguroService implements DeletarSeguroUseCase {

    private final SeguroRepository seguroRepository;
    private final ClienteRepository clienteRepository;

    // cancelar apolice seguro
    @Transactional
    public CancelarSeguroResponse cancelarSeguro(Long id_seguro, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_CANCELAMENTO_SEGURO);
        Seguro seguro = Validator.verificarSeguroExistente(seguroRepository, id_seguro);
        log.info(ConstantUtils.SEGURO_ENCONTRADO, seguro.getId());
        securityService.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        seguro.setarStatusSeguro(Status.INATIVO);
        try {
            seguroRepository.save(seguro);
            log.info(ConstantUtils.SUCESSO_CANCELAMENTO_SEGURO, seguro.getId());
        } catch (DataIntegrityViolationException e) {
            log.error(ConstantUtils.ERRO_CANCELAMENTO_SEGURO, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_CANCELAMENTO_SEGURO);
        }
        return CancelarSeguroResponse.toCancelarSeguroResponse(seguro);
    }
    // deletar seguros de cliente
    @Transactional
    public void deleteSegurosByCliente(Long id_cliente) {
        log.info(ConstantUtils.INICIO_DELETE_SEGURO);
        Validator.verificarClienteExistente(clienteRepository, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, id_cliente);
        List<Seguro> seguros = seguroRepository.findyClienteId(id_cliente);
        if (seguros.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_SEGUROS, id_cliente);
            return;
        }
        for (Seguro seguro : seguros) {
            try {
                seguroRepository.deleteSeguroById(seguro.getId());
                log.info(ConstantUtils.SUCESSO_DELETE_SEGURO, id_cliente);

            } catch (DataIntegrityViolationException e) {
                log.error(ConstantUtils.ERRO_INESPERADO_DELETE_SEGURO, seguro.getId(), e);
                throw new ValidationException(ConstantUtils.ERRO_INESPERADO_DELETE_SEGURO + seguro.getId());
            }
        }
    }
}

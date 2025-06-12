package br.com.cdb.bancodigital.application.core.service.conta;

import br.com.cdb.bancodigital.application.core.domain.entity.Conta;
import br.com.cdb.bancodigital.application.port.in.conta.DeletarContaUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.ContaRepository;
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
public class DeletarContaService implements DeletarContaUseCase {

    private final ContaRepository contaRepository;

    @Transactional
    public void deleteContasByCliente(Long id_cliente) {
        log.info(ConstantUtils.INICIO_DELETE_CONTA, id_cliente);
        List<Conta> contas = contaRepository.findByClienteId(id_cliente);
        if (contas.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_CONTAS, id_cliente);
            return;
        }
        for (Conta conta : contas) {
            try {
                contaRepository.validateVinculosConta(conta.getId());
                Validator.verificarSaldoRemanescente(conta);
                contaRepository.delete(conta.getId());
                log.info(ConstantUtils.SUCESSO_DELETE_CONTA, conta.getCliente().getId());

            } catch (DataIntegrityViolationException e) {
                log.error(ConstantUtils.ERRO_INESPERADO_DELETE_CONTA, conta.getId(), e);
                throw new ValidationException(ConstantUtils.ERRO_INESPERADO_DELETE_CONTA + conta.getId());
            }
        }
    }
}

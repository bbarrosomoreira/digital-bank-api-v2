package br.com.cdb.bancodigital.application.core.service.seguro;

import br.com.cdb.bancodigital.application.core.domain.dto.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Seguro;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Status;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.application.port.in.seguro.AcionarSeguroUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.SeguroRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class AcionarSeguroService implements AcionarSeguroUseCase {
    
    private final SeguroRepository seguroRepository;

    @Transactional
    public Seguro acionarSeguro(Long id_seguro, Usuario usuarioLogado, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_ACIONAMENTO_SEGURO);
        Seguro seguro = Validator.verificarSeguroExistente(seguroRepository, id_seguro);
        log.info(ConstantUtils.SEGURO_ENCONTRADO, seguro.getId());
        securityService.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);

        if (seguro.getTipoSeguro().equals(TipoSeguro.FRAUDE) && seguro.getStatusSeguro().equals(Status.INATIVO))
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SEGURO_DESATIVADO);
        if (seguro.getTipoSeguro().equals(TipoSeguro.FRAUDE)) seguro.setValorFraude(valor);

        seguro.acionarSeguro();
        try {
            seguroRepository.save(seguro);
            log.info(ConstantUtils.SUCESSO_ACIONAMENTO_SEGURO, seguro.getId());
        } catch (DataIntegrityViolationException e) {
            log.error(ConstantUtils.ERRO_ACIONAMENTO_SEGURO, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_ACIONAMENTO_SEGURO);
        }
        return seguro;
    }
    @Transactional
    public DebitarPremioSeguroResponse debitarPremioSeguro(Long id_seguro) {
        log.info(ConstantUtils.INICIO_DEBITO_PREMIO_SEGURO);
        Seguro seguro = Validator.verificarSeguroExistente(seguroRepository, id_seguro);
        log.info(ConstantUtils.SEGURO_ENCONTRADO, seguro.getId());

        if (seguro.getStatusSeguro().equals(Status.INATIVO))
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SEGURO_DESATIVADO);
        if (seguro.getPremioApolice().compareTo(seguro.getCartao().getConta().getSaldo()) > 0)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SALDO_INSUFICIENTE);
        try {
            seguro.aplicarPremio();
            seguroRepository.save(seguro);
            log.info(ConstantUtils.SUCESSO_DEBITO_PREMIO_SEGURO, seguro.getId());
        } catch (DataIntegrityViolationException e) {
            log.error(ConstantUtils.ERRO_DEBITO_PREMIO_SEGURO, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_DEBITO_PREMIO_SEGURO);
        }
        return DebitarPremioSeguroResponse.toDebitarPremioSeguroResponse(seguro);
    }
    
}

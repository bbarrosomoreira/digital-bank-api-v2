package br.com.cdb.bancodigital.application.core.service.cartao;

import br.com.cdb.bancodigital.application.core.domain.model.Cartao;
import br.com.cdb.bancodigital.application.port.in.cartao.DeletarCartaoUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.CartaoRepository;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
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
public class DeletarCartaoService implements DeletarCartaoUseCase {

    private final CartaoRepository cartaoRepository;

    @Transactional
    public void deleteCartoesByCliente(Long id_cliente) {
        log.info(ConstantUtils.INICIO_DELETE_CARTAO, id_cliente);
        List<Cartao> cartoes = cartaoRepository.findByContaClienteId(id_cliente);
        if (cartoes.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_CARTOES, id_cliente);
            return;
        }
        for (Cartao cartao : cartoes) {
            try {
                cartaoRepository.validateVinculosCartao(cartao.getId());
                Validator.verificaSeTemFaturaAbertaDeCartaoCredito(cartao);
                Long id = cartao.getId();
                cartaoRepository.delete(cartao.getId());
                log.info(ConstantUtils.CARTAO_DELETADO_SUCESSO, id);

            } catch (DataIntegrityViolationException e) {
                log.error(ConstantUtils.ERRO_DELETAR_CARTAO, cartao.getId(), e);
                throw new ValidationException(ConstantUtils.ERRO_DELETAR_CARTAO_MENSAGEM + e.getMessage());
            }
        }
    }

}

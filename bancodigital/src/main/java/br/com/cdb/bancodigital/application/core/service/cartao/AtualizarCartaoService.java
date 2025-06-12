package br.com.cdb.bancodigital.application.core.service.cartao;

import br.com.cdb.bancodigital.application.core.domain.dto.response.LimiteResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.RessetarLimiteDiarioResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.StatusCartaoResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.Status;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoCartao;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.in.cartao.AtualizarCartaoUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.CartaoRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class AtualizarCartaoService implements AtualizarCartaoUseCase {

    private final CartaoRepository cartaoRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUseCase securityUseCase;

    @Transactional
    public LimiteResponse alterarLimite(Long id_cartao, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_ALTERACAO_LIMITE, id_cartao);
        Cartao cartao = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, cartao.getId());
        Validator.verificarCartaoAtivo(cartao.getStatus());
        log.info(ConstantUtils.CARTAO_ATIVO, cartao.getId());

        BigDecimal limiteConsumido = cartao.getLimite().subtract(cartao.getLimiteAtual());

        if (valor.compareTo(limiteConsumido) < 0)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_LIMITE_CONSUMIDO);

        cartao.alterarLimite(valor);
        cartaoRepository.save(cartao);
        return LimiteResponse.toLimiteResponse(cartao, valor);
    }
    // alter status cartao
    @Transactional
    public StatusCartaoResponse alterarStatus(Long id_cartao, Usuario usuarioLogado, Status statusNovo) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        securityUseCase.validateAccess(usuarioLogado, cartao.getConta().getCliente());

        if (statusNovo.equals(Status.INATIVO) && cartao.getTipoCartao().equals(TipoCartao.CREDITO)) {
            Validator.verificaSeTemFaturaAbertaDeCartaoCredito(cartao);
        }

        cartao.alterarStatus(statusNovo);
        cartaoRepository.save(cartao);
        return StatusCartaoResponse.toStatusCartaoResponse(cartao, statusNovo);
    }
    // alter senha
    @Transactional
    public void alterarSenha(Long id_cartao, Usuario usuarioLogado, String senhaAntiga, String senhaNova) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        securityUseCase.validateAccess(usuarioLogado, cartao.getConta().getCliente());

        Validator.verificarCartaoAtivo(cartao.getStatus());

        cartao.alterarSenha(senhaAntiga, senhaNova, passwordEncoder);
        cartaoRepository.save(cartao);
    }
    // ressetar limite diario
    @Transactional
    public RessetarLimiteDiarioResponse ressetarDebito(Long id_cartao) {
        Cartao cdb = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);

        Validator.verificarCartaoAtivo(cdb.getStatus());
        cdb.reiniciarLimiteDebito();
        cartaoRepository.save(cdb);
        return RessetarLimiteDiarioResponse.toRessetarLimiteDiarioResponse(cdb);
    }
    
    
}

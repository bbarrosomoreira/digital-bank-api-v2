package br.com.cdb.bancodigital.application.core.service.cartao;

import br.com.cdb.bancodigital.application.core.domain.dto.response.FaturaPagaResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.FaturaResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.PagamentoResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Cartao;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoCartao;
import br.com.cdb.bancodigital.application.port.in.cartao.PagamentoUseCase;
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
public class PagamentoService implements PagamentoUseCase {

    private final CartaoRepository cartaoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PagamentoResponse pagar(Long id_cartao, Usuario usuarioLogado, BigDecimal valor, String senha, String descricao) {
        Cartao cartao = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        Validator.verificarCartaoAtivo(cartao.getStatus());
        Validator.verificarSenhaCorreta(senha, cartao.getSenha(), passwordEncoder);
        Validator.verificarLimiteSuficiente(valor, cartao.getLimiteAtual());

        if (cartao.getTipoCartao().equals(TipoCartao.DEBITO) && cartao.getLimiteAtual().compareTo(valor) < 0) {
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SALDO_INSUFICIENTE);
        }

        cartao.realizarPagamento(valor);
        cartaoRepository.save(cartao);
        return PagamentoResponse.toPagamentoResponse(cartao, valor, descricao);
    }
    public FaturaResponse getFatura(Long id_cartao, Usuario usuarioLogado) {
        Cartao ccr = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
        Validator.verificarCartaoAtivo(ccr.getStatus());

        if (ccr.getTipoCartao() != TipoCartao.CREDITO)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_CARTAO_NAO_CREDITO);

        return FaturaResponse.toFaturaResponse(ccr);
    }
    @Transactional
    public FaturaPagaResponse pagarFatura(Long id_cartao, Usuario usuarioLogado) {
        Cartao ccr = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());

        Validator.verificarCartaoAtivo(ccr.getStatus());
        if (ccr.getTotalFatura().compareTo(ccr.getConta().getSaldo()) > 0)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SALDO_INSUFICIENTE);

        ccr.pagarFatura();
        cartaoRepository.save(ccr);
        return FaturaPagaResponse.toFaturaPagaResponse(ccr);
    }
    
    
}

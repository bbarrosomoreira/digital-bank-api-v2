package br.com.cdb.bancodigital.application.core.service.cartao;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Cartao;
import br.com.cdb.bancodigital.application.core.domain.model.Conta;
import br.com.cdb.bancodigital.application.core.domain.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoCartao;
import br.com.cdb.bancodigital.application.port.in.cartao.EmitirCartaoUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.CartaoRepository;
import br.com.cdb.bancodigital.application.port.out.repository.ContaRepository;
import br.com.cdb.bancodigital.application.port.out.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class EmitirCartaoService implements EmitirCartaoUseCase {

    private final ContaRepository contaRepository;
    private final CartaoRepository cartaoRepository;
    private final PoliticaDeTaxasRepository politicaDeTaxasRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
        log.info(ConstantUtils.INICIO_EMISSAO_CARTAO);

        // Busca e validações
        Conta conta = Validator.verificarContaExistente(contaRepository, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA, conta.getId());

        securityService.validateAccess(usuarioLogado, conta.getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO_USUARIO, usuarioLogado.getId());

        // Lógica de criação
        String senhaCriptografada = passwordEncoder.encode(senha);
        Cartao cartaoNovo = criarCartaoPorTipo(tipo, conta, senhaCriptografada);
        log.info(ConstantUtils.CARTAO_CRIADO, cartaoNovo.getId());

        try {
            cartaoRepository.save(cartaoNovo);
            log.info(ConstantUtils.CARTAO_SALVO_BANCO, cartaoNovo.getId());
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_SALVAR_CARTAO_BANCO, e);
            throw new SystemException(ConstantUtils.ERRO_SALVAR_CARTAO_BANCO_MENSAGEM_EXCEPTION);
        }

        log.info(ConstantUtils.SUCESSO_EMISSAO_CARTAO);
        return toResponse(cartaoNovo);
    }
    private Cartao criarCartaoPorTipo(TipoCartao tipo, Conta conta, String senha) {
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);
        CategoriaCliente categoria = conta.getCliente().getCategoria();
        PoliticaDeTaxas parametros = Validator.verificarPoliticaExitente(politicaDeTaxasRepository, categoria);
        log.info(ConstantUtils.POLITICA_TAXAS_ENCONTRADA);

        return switch (tipo) {
            case CREDITO -> {
                Cartao ccr = new Cartao(conta, senha, tipo);
                ccr.setLimite(parametros.getLimiteCartaoCredito());
                ccr.setLimiteAtual(ccr.getLimite());
                ccr.setTotalFatura(BigDecimal.ZERO);
                ccr.setTotalFaturaPaga(BigDecimal.ZERO);
                yield ccr;
            }
            case DEBITO -> {
                Cartao cdb = new Cartao(conta, senha, tipo);
                cdb.setLimite(parametros.getLimiteDiarioDebito());
                cdb.setLimiteAtual(cdb.getLimite());
                yield cdb;
            }
        };
    }
    private CartaoResponse toResponse(Cartao cartao) {
        return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
                cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(), cartao.getLimite());
    }


}

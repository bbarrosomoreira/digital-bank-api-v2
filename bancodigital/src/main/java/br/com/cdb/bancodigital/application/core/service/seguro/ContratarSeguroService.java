package br.com.cdb.bancodigital.application.core.service.seguro;

import br.com.cdb.bancodigital.application.core.domain.dto.response.SeguroResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;
import br.com.cdb.bancodigital.application.core.domain.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigital.application.core.domain.entity.Seguro;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoSeguro;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.in.seguro.ContratarSeguroUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.CartaoRepository;
import br.com.cdb.bancodigital.application.port.out.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigital.application.port.out.repository.SeguroRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class ContratarSeguroService implements ContratarSeguroUseCase {

    private final SeguroRepository seguroRepository;
    private final CartaoRepository cartaoRepository;
    private final PoliticaDeTaxasRepository politicaDeTaxasRepository;
    private final SecurityUseCase securityUseCase;

    @Transactional
    public SeguroResponse contratarSeguro(Long id_cartao, Usuario usuarioLogado, TipoSeguro tipo) {
        log.info(ConstantUtils.INICIO_CONTRATACAO_SEGURO);
        Cartao ccr = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, ccr.getId());
        Validator.verificarCartaoAtivo(ccr.getStatus());
        log.info(ConstantUtils.CARTAO_ATIVO);
        securityUseCase.validateAccess(usuarioLogado, ccr.getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Seguro seguroNovo = contratarSeguroPorTipo(tipo, ccr);
        log.info(ConstantUtils.SEGURO_CRIADO, seguroNovo.getId());

        try {
            seguroRepository.save(seguroNovo);
            log.info(ConstantUtils.SEGURO_SALVO_BANCO, seguroNovo.getId());
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_CONTRATAR_SEGURO, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_CONTRATAR_SEGURO_MENSAGEM_EXCEPTION);
        }
        log.info(ConstantUtils.SUCESSO_CONTRATACAO_SEGURO);
        return toResponse(seguroNovo);
    }
    private Seguro contratarSeguroPorTipo(TipoSeguro tipo, Cartao ccr) {
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);
        CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();
        PoliticaDeTaxas parametros = Validator.verificarPoliticaExitente(politicaDeTaxasRepository, categoria);
        log.info(ConstantUtils.POLITICA_TAXAS_ENCONTRADA);

        return switch (tipo) {
            case FRAUDE -> {
                Seguro sf = new Seguro(ccr);
                sf.setTipoSeguro(TipoSeguro.FRAUDE);
                sf.setValorApolice(parametros.getValorApoliceFraude());
                sf.setPremioApolice(parametros.getTarifaSeguroFraude());
                sf.setDescricaoCondicoes(TipoSeguro.FRAUDE.getDescricao());
                yield sf;
            }
            case VIAGEM -> {
                Seguro sv = new Seguro(ccr);
                sv.setTipoSeguro(TipoSeguro.VIAGEM);
                sv.setValorApolice(parametros.getValorApoliceViagem());
                sv.setPremioApolice(parametros.getTarifaSeguroViagem());
                sv.setDescricaoCondicoes(TipoSeguro.VIAGEM.getDescricao());
                yield sv;
            }
        };
    }
    private SeguroResponse toResponse(Seguro seguro) {
        return SeguroResponse.toSeguroResponse(seguro);
    }
}

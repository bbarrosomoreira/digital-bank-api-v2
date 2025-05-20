package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Status;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dao.CartaoDAO;
import br.com.cdb.bancodigital.dao.ClienteDAO;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.dao.SeguroDAO;
import br.com.cdb.bancodigital.dto.response.CancelarSeguroResponse;
import br.com.cdb.bancodigital.dto.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigital.dto.response.SeguroResponse;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class SeguroService {

    private final SeguroDAO seguroDAO;
    private final CartaoDAO cartaoDAO;
    private final ClienteDAO clienteDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxasDAO;
    private final SecurityService securityService;

    // contrataSeguro
    @Transactional
    public SeguroResponse contratarSeguro(Long id_cartao, Usuario usuarioLogado, TipoSeguro tipo) {
        log.info(ConstantUtils.INICIO_CONTRATACAO_SEGURO);
        Cartao ccr = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, ccr.getId());
        Validator.verificarCartaoAtivo(ccr.getStatus());
        log.info(ConstantUtils.CARTAO_ATIVO);
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Seguro seguroNovo = contratarSeguroPorTipo(tipo, ccr);
        log.info(ConstantUtils.SEGURO_CRIADO, seguroNovo.getId());

        try {
            seguroDAO.salvar(seguroNovo);
            log.info(ConstantUtils.SEGURO_SALVO_BANCO, seguroNovo.getId());
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_CONTRATAR_SEGURO, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_CONTRATAR_SEGURO_MENSAGEM_EXCEPTION);
        }
        log.info(ConstantUtils.SUCESSO_CONTRATACAO_SEGURO);
        return toResponse(seguroNovo);
    }

    public Seguro contratarSeguroPorTipo(TipoSeguro tipo, Cartao ccr) {
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);
        CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();
        PoliticaDeTaxas parametros = Validator.verificarPoliticaExitente(politicaDeTaxasDAO, categoria);
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

    // obtemDetalhesApolice
    public SeguroResponse getSeguroById(Long id_seguro, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        Seguro seguro = Validator.verificarSeguroExistente(seguroDAO, id_seguro);
        log.info(ConstantUtils.SEGURO_ENCONTRADO, seguro.getId());
        securityService.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return SeguroResponse.toSeguroResponse(seguro);
    }

    // get seguro por usuario
    public List<SeguroResponse> listarPorUsuario(Usuario usuario) {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        List<Seguro> seguros = seguroDAO.findByCartaoContaClienteUsuario(usuario);
        return seguros.stream().map(this::toResponse).toList();
    }

    // cancelar apolice seguro
    @Transactional
    public CancelarSeguroResponse cancelarSeguro(Long id_seguro, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_CANCELAMENTO_SEGURO);
        Seguro seguro = Validator.verificarSeguroExistente(seguroDAO, id_seguro);
        log.info(ConstantUtils.SEGURO_ENCONTRADO, seguro.getId());
        securityService.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        seguro.setarStatusSeguro(Status.INATIVO);
        try {
            seguroDAO.salvar(seguro);
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
        Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, id_cliente);
        List<Seguro> seguros = seguroDAO.findSegurosByClienteId(id_cliente);
        if (seguros.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_SEGUROS, id_cliente);
            return;
        }
        for (Seguro seguro : seguros) {
            try {
                seguroDAO.deletarSeguroPorId(seguro.getId());
                log.info(ConstantUtils.SUCESSO_DELETE_SEGURO, id_cliente);

            } catch (DataIntegrityViolationException e) {
                log.error(ConstantUtils.ERRO_INESPERADO_DELETE_SEGURO, seguro.getId(), e);
                throw new ValidationException(ConstantUtils.ERRO_INESPERADO_DELETE_SEGURO + seguro.getId());
            }
        }
    }

    // get seguros
    public List<SeguroResponse> getSeguros() {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        List<Seguro> seguros = seguroDAO.buscarTodosSeguros();
        return seguros.stream().map(this::toResponse).toList();
    }

    // get seguros by cartao
    public List<SeguroResponse> getSeguroByCartaoId(Long id_cartao, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        Cartao cartao = Validator.verificarCartaoExistente(cartaoDAO, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, cartao.getId());
        securityService.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        List<Seguro> seguros = seguroDAO.findByCartaoId(id_cartao);
        return seguros.stream().map(this::toResponse).toList();
    }

    // get seguros by cliente
    public List<SeguroResponse> getSeguroByClienteId(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        List<Seguro> seguros = seguroDAO.findSegurosByClienteId(id_cliente);
        return seguros.stream().map(this::toResponse).toList();
    }

    @Transactional
    // acionarSeguro
    public Seguro acionarSeguro(Long id_seguro, Usuario usuarioLogado, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_ACIONAMENTO_SEGURO);
        Seguro seguro = Validator.verificarSeguroExistente(seguroDAO, id_seguro);
        log.info(ConstantUtils.SEGURO_ENCONTRADO, seguro.getId());
        securityService.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);

        if (seguro.getStatusSeguro().equals(Status.INATIVO))
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SEGURO_DESATIVADO);
        if (seguro.getTipoSeguro().equals(TipoSeguro.FRAUDE)) seguro.setValorFraude(valor);

        seguro.acionarSeguro();
        try {
            seguroDAO.salvar(seguro);
            log.info(ConstantUtils.SUCESSO_ACIONAMENTO_SEGURO, seguro.getId());
        } catch (DataIntegrityViolationException e) {
            log.error(ConstantUtils.ERRO_ACIONAMENTO_SEGURO, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_ACIONAMENTO_SEGURO);
        }
        return seguro;
    }

    @Transactional
    // debitarPremioSeguro
    public DebitarPremioSeguroResponse debitarPremioSeguro(Long id_seguro) {
        log.info(ConstantUtils.INICIO_DEBITO_PREMIO_SEGURO);
        Seguro seguro = Validator.verificarSeguroExistente(seguroDAO, id_seguro);
        log.info(ConstantUtils.SEGURO_ENCONTRADO, seguro.getId());

        if (seguro.getStatusSeguro().equals(Status.INATIVO))
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SEGURO_DESATIVADO);
        if (seguro.getPremioApolice().compareTo(seguro.getCartao().getConta().getSaldo()) > 0)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SALDO_INSUFICIENTE);
        try {
            seguro.aplicarPremio();
            seguroDAO.salvar(seguro);
            log.info(ConstantUtils.SUCESSO_DEBITO_PREMIO_SEGURO, seguro.getId());
        } catch (DataIntegrityViolationException e) {
            log.error(ConstantUtils.ERRO_DEBITO_PREMIO_SEGURO, e.getMessage());
            throw new SystemException(ConstantUtils.ERRO_DEBITO_PREMIO_SEGURO);
        }
        return DebitarPremioSeguroResponse.toDebitarPremioSeguroResponse(seguro);
    }

    public SeguroResponse toResponse(Seguro seguro) {
        return SeguroResponse.toSeguroResponse(seguro);
    }

}

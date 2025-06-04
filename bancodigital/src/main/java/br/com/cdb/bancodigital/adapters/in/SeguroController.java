package br.com.cdb.bancodigital.adapters.in;

import br.com.cdb.bancodigital.application.core.domain.dto.AcionarSeguroFraudeDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.ContratarSeguroDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.response.*;
import br.com.cdb.bancodigital.application.core.domain.model.Seguro;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.application.port.in.seguro.AcionarSeguroUseCase;
import br.com.cdb.bancodigital.application.port.in.seguro.ContratarSeguroUseCase;
import br.com.cdb.bancodigital.application.port.in.seguro.DeletarSeguroUseCase;
import br.com.cdb.bancodigital.application.port.in.seguro.ListarSeguroUseCase;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(ConstantUtils.SEGURO)
@AllArgsConstructor
@Slf4j
public class SeguroController {

    private final ContratarSeguroUseCase contratarSeguroUseCase;
    private final ListarSeguroUseCase listarSeguroUseCase;
    private final DeletarSeguroUseCase deletarSeguroUseCase;
    private final AcionarSeguroUseCase acionarSeguroUseCase;

    @PostMapping
    public ResponseEntity<SeguroResponse> contratarSeguro (
            @Valid @RequestBody ContratarSeguroDTO dto,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_CONTRATACAO_SEGURO);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        SeguroResponse response = contratarSeguroUseCase.contratarSeguro(dto.getId_cartao(), usuarioLogado, dto.getTipo());
        log.info(ConstantUtils.SUCESSO_CONTRATACAO_SEGURO);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    // Listar tipos de Seguros Disponiveis (suas descricoes)
    @GetMapping(ConstantUtils.TIPOS)
    public ResponseEntity<List<TipoSeguroResponse>> listarTiposSeguros(){
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_LISTAGEM_TIPO_SEGURO);

        List<TipoSeguroResponse> tipos = Arrays.stream(TipoSeguro.values())
                .map(seguro -> new TipoSeguroResponse(seguro.getNome(), seguro.getDescricao(), seguro.getCondicoes()))
                .toList();
        log.info(ConstantUtils.SUCESSO_LISTAGEM_TIPO_SEGURO);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(tipos);
    }

    // só admin pode puxar uma lista de todos seguros
    @PreAuthorize(ConstantUtils.ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<List<SeguroResponse>> getSeguros(){
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);

        List<SeguroResponse> seguros = listarSeguroUseCase.getSeguros();
        log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(seguros);
    }

    // admin tem acesso ao id, cliente só pode ver se for dele
    @GetMapping(ConstantUtils.CARTAO + ConstantUtils.CARTAO_ID)
    public ResponseEntity<List<SeguroResponse>> listarPorCartao(
            @PathVariable Long id_cartao,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO + ConstantUtils.ID_CARTAO, id_cartao);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        List<SeguroResponse> seguros = listarSeguroUseCase.getSeguroByCartaoId(id_cartao, usuarioLogado);
        log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO + ConstantUtils.ID_CARTAO, id_cartao);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(seguros);
    }

    // admin tem acesso ao id, cliente só pode ver se for dele
    @GetMapping(ConstantUtils.CLIENTE + ConstantUtils.CLIENTE_ID)
    public ResponseEntity<List<SeguroResponse>> listarPorCliente(
            @PathVariable Long id_cliente,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO + ConstantUtils.ID_CLIENTE, id_cliente);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        List<SeguroResponse> seguros = listarSeguroUseCase.getSeguroByClienteId(id_cliente, usuarioLogado);
        log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO + ConstantUtils.ID_CLIENTE, id_cliente);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(seguros);
    }

    // admin tem acesso ao id, cliente só pode ver se for dele
    @GetMapping(ConstantUtils.SEGURO_ID)
    public ResponseEntity<SeguroResponse> getSeguroById(
            @PathVariable Long id_seguro,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO + ConstantUtils.ID_SEGURO, id_seguro);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        SeguroResponse seguro = listarSeguroUseCase.getSeguroById(id_seguro, usuarioLogado);
        log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO + ConstantUtils.ID_SEGURO, id_seguro);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(seguro);
    }

    // para usuário logado ver informações de seus cartoes (cliente)
    @PreAuthorize(ConstantUtils.ROLE_CLIENTE)
    @GetMapping(ConstantUtils.GET_USUARIO)
    public ResponseEntity<List<SeguroResponse>> buscarSegurosDoUsuario (
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        List<SeguroResponse> seguros = listarSeguroUseCase.listarPorUsuario(usuarioLogado);
        log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(seguros);
    }

    // admin tem acesso ao id, cliente só pode ver se for dele
    @PutMapping(ConstantUtils.SEGURO_ID + ConstantUtils.CANCELAR_ENDPOINT)
    public ResponseEntity<CancelarSeguroResponse> cancelarSeguro(
            @PathVariable Long id_seguro,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_CANCELAMENTO_SEGURO, id_seguro);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        CancelarSeguroResponse response = deletarSeguroUseCase.cancelarSeguro(id_seguro, usuarioLogado);
        log.info(ConstantUtils.SUCESSO_CANCELAMENTO_SEGURO);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(response);
    }

    // só o admin pode confirmar a exclusão de cadastro de seguros
    @PreAuthorize(ConstantUtils.ROLE_ADMIN)
    @DeleteMapping(ConstantUtils.CLIENTE + ConstantUtils.CLIENTE_ID)
    public ResponseEntity<Void> deleteSegurosByCliente(
            @PathVariable Long id_cliente){
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_DELETE_SEGURO, id_cliente);

        deletarSeguroUseCase.deleteSegurosByCliente(id_cliente);
        log.info(ConstantUtils.SUCESSO_DELETE_SEGURO, id_cliente);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.noContent().build();
    }

    // admin tem acesso ao id, cliente só pode ver se for dele
    @PutMapping(ConstantUtils.FRAUDE_ENDPOINT + ConstantUtils.SEGURO_ID + ConstantUtils.ACIONAR_ENDPOINT)
    public ResponseEntity<AcionarSeguroFraudeResponse> acionarSeguroFraude(
            @PathVariable Long id_seguro,
            @Valid @RequestBody AcionarSeguroFraudeDTO dto,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_ACIONAMENTO_SEGURO, ConstantUtils.SEGURO_FRAUDE);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        Seguro seguro = acionarSeguroUseCase.acionarSeguro(id_seguro, usuarioLogado, dto.getValorFraude());
        log.info(ConstantUtils.SUCESSO_ACIONAMENTO_SEGURO, ConstantUtils.SEGURO_FRAUDE);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(AcionarSeguroFraudeResponse.toSeguroFraudeResponse(seguro));
    }

    // admin tem acesso ao id, cliente só pode ver se for dele
    @PutMapping(ConstantUtils.VIAGEM_ENDPOINT + ConstantUtils.SEGURO_ID + ConstantUtils.ACIONAR_ENDPOINT)
    public ResponseEntity<AcionarSeguroViagemResponse> acionarSeguroViagem(
            @PathVariable Long id_seguro,
            Authentication authentication) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_ACIONAMENTO_SEGURO, ConstantUtils.SEGURO_VIAGEM);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

        Seguro seguro = acionarSeguroUseCase.acionarSeguro(id_seguro, usuarioLogado, BigDecimal.ZERO);
        log.info(ConstantUtils.SUCESSO_ACIONAMENTO_SEGURO, ConstantUtils.SEGURO_VIAGEM);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(AcionarSeguroViagemResponse.toSeguroViagemResponse(seguro));
    }

    // debitar premio seguro quando ativo
    @PreAuthorize(ConstantUtils.ROLE_ADMIN)
    @PostMapping(ConstantUtils.SEGURO_ID + ConstantUtils.PREMIO_ENDPOINT)
    public ResponseEntity<DebitarPremioSeguroResponse> debitarPremioSeguro(
            @PathVariable Long id_seguro){
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_DEBITO_PREMIO_SEGURO, id_seguro);

        DebitarPremioSeguroResponse response = acionarSeguroUseCase.debitarPremioSeguro(id_seguro);
        log.info(ConstantUtils.SUCESSO_DEBITO_PREMIO_SEGURO);
        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(response);

    }

}

package br.com.cdb.bancodigital.application.core.service.seguro;

import br.com.cdb.bancodigital.application.core.domain.dto.response.SeguroResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Seguro;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.in.seguro.ListarSeguroUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.CartaoRepository;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.SeguroRepository;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ListarSeguroService implements ListarSeguroUseCase {

    private final ClienteRepository clienteRepository;
    private final CartaoRepository cartaoRepository;
    private final SeguroRepository seguroRepository;
    private final SecurityUseCase securityUseCase;
    

    public SeguroResponse getSeguroById(Long id_seguro, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        Seguro seguro = Validator.verificarSeguroExistente(seguroRepository, id_seguro);
        log.info(ConstantUtils.SEGURO_ENCONTRADO, seguro.getId());
        securityUseCase.validateAccess(usuarioLogado, seguro.getCartao().getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return SeguroResponse.toSeguroResponse(seguro);
    }
    public List<SeguroResponse> listarPorUsuario(Usuario usuario) {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        List<Seguro> seguros = seguroRepository.findByCartaoContaClienteUsuario(usuario);
        return seguros.stream().map(this::toResponse).toList();
    }
    public List<SeguroResponse> getSeguros() {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        List<Seguro> seguros = seguroRepository.findAllSeguros();
        return seguros.stream().map(this::toResponse).toList();
    }
    public List<SeguroResponse> getSeguroByCartaoId(Long id_cartao, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        Cartao cartao = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, cartao.getId());
        securityUseCase.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        List<Seguro> seguros = seguroRepository.findByCartaoId(id_cartao);
        return seguros.stream().map(this::toResponse).toList();
    }
    public List<SeguroResponse> getSeguroByClienteId(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
        Cliente cliente = Validator.verificarClienteExistente(clienteRepository, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());
        securityUseCase.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        List<Seguro> seguros = seguroRepository.findyClienteId(id_cliente);
        return seguros.stream().map(this::toResponse).toList();
    }
    private SeguroResponse toResponse(Seguro seguro) {
        return SeguroResponse.toSeguroResponse(seguro);
    }
}

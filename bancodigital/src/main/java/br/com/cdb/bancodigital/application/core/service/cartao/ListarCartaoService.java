package br.com.cdb.bancodigital.application.core.service.cartao;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Conta;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.in.cartao.ListarCartaoUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.CartaoRepository;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.ContaRepository;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ListarCartaoService implements ListarCartaoUseCase {

    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final CartaoRepository cartaoRepository;
    private final SecurityUseCase securityUseCase;

    public List<CartaoResponse> getCartoes() {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO);
        List<Cartao> cartoes = cartaoRepository.findAll();
        return cartoes.stream().map(this::toResponse).toList();
    }
    // get cartoes por conta
    public List<CartaoResponse> listarPorConta(Long id_conta, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_CONTA, id_conta);
        Conta conta = Validator.verificarContaExistente(contaRepository, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA, conta.getId());
        securityUseCase.validateAccess(usuarioLogado, conta.getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        log.info(ConstantUtils.BUSCANDO_CARTOES_VINCULADOS_CONTA, id_conta);
        List<Cartao> cartoes = cartaoRepository.findByContaId(id_conta);
        return cartoes.stream().map(this::toResponse).toList();
    }
    // get cartao por cliente
    public List<CartaoResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteRepository, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO, cliente.getId());
        securityUseCase.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        log.info(ConstantUtils.BUSCANDO_CARTOES_VINCULADOS_CLIENTE, id_cliente);
        List<Cartao> cartoes = cartaoRepository.findByContaClienteId(id_cliente);
        return cartoes.stream().map(this::toResponse).toList();
    }
    // get um cartao
    public CartaoResponse getCartaoById(Long id_cartao, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO, id_cartao);
        Cartao cartao = Validator.verificarCartaoExistente(cartaoRepository, id_cartao);
        log.info(ConstantUtils.CARTAO_ENCONTRADO, cartao.getId());
        securityUseCase.validateAccess(usuarioLogado, cartao.getConta().getCliente());
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return toResponse(cartao);
    }
    // get cartão por usuário
    public List<CartaoResponse> listarPorUsuario(Usuario usuario) {
        log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_USUARIO, usuario.getId());
        List<Cartao> cartoes = cartaoRepository.findByContaClienteUsuario(usuario);
        return cartoes.stream().map(this::toResponse).toList();
    }
    private CartaoResponse toResponse(Cartao cartao) {
        return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
                cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(), cartao.getLimite());
    }
    
}

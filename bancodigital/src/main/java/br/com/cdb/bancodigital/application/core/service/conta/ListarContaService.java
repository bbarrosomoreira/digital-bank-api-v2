package br.com.cdb.bancodigital.application.core.service.conta;

import br.com.cdb.bancodigital.application.core.domain.dto.response.ContaResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Conta;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoConta;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.in.conta.ListarContaUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.ContaRepository;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ListarContaService implements ListarContaUseCase {

    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final SecurityUseCase securityUseCase;

    public List<ContaResponse> getContas() {
        log.info(ConstantUtils.INICIO_BUSCA_CONTA);
        List<Conta> contas = contaRepository.findAll();
        return contas.stream().map(this::toResponse).toList();
    }
    // get conta por usuário
    public List<ContaResponse> getContaByUsuario(Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CONTA_POR_USUARIO, usuarioLogado.getId());
        List<Conta> contas = contaRepository.findByClienteUsuario(usuarioLogado);
        return contas.stream().map(this::toResponse).toList();
    }
    // get conta por cliente
    public List<ContaResponse> getContaByCliente(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CONTA_POR_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteRepository, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO);
        securityUseCase.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        List<Conta> contas = contaRepository.findByClienteId(id_cliente);
        return contas.stream().map(this::toResponse).toList();
    }
    // get uma conta
    public ContaResponse getContaById(Long id_conta, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CONTA);
        Conta conta = Validator.verificarContaExistente(contaRepository, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = conta.getCliente();
        securityUseCase.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return toResponse(conta);
    }
    private ContaResponse toResponse(Conta conta) {
        BigDecimal tarifa;
        switch (conta.getTipoConta()) {
            case CORRENTE, INTERNACIONAL -> {
                tarifa = conta.getTarifaManutencao();
            }
            case POUPANCA -> {
                tarifa = conta.getTaxaRendimento();
            }
            default -> throw new IllegalStateException(ConstantUtils.VALOR_INESPERADO + conta.getTipoConta());
        }
        ContaResponse response = new ContaResponse(conta.getId(), conta.getNumeroConta(), conta.getTipoConta(),
                conta.getMoeda(), conta.getSaldo(), conta.getDataCriacao(),
                tarifa);
        if (conta.getTipoConta().equals(TipoConta.INTERNACIONAL)) {
            response.setSaldoEmReais(conta.getSaldoEmReais());
        }
        return response;
    }
}

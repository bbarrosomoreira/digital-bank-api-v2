package br.com.cdb.bancodigital.application.core.service.conta;

import br.com.cdb.bancodigital.application.core.domain.dto.response.*;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Conta;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.in.conta.TransacoesUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.ContaRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class TransacoesService implements TransacoesUseCase {

    private final ContaRepository contaRepository;
    private final SecurityUseCase securityUseCase;

    // transferencia
    @Transactional
    public TransferenciaResponse transferir(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.TRANSFERENCIA, id_contaOrigem);
        Conta origem = Validator.verificarContaExistente(contaRepository, id_contaOrigem);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Conta destino = Validator.verificarContaExistente(contaRepository, id_contaDestino);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = origem.getCliente();
        securityUseCase.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Validator.verificarSaldoSuficiente(valor, origem.getSaldo());
        log.info(ConstantUtils.SALDO_SUFICIENTE);
        try {
            origem.transferir(destino, valor);
            contaRepository.save(origem);
            contaRepository.save(destino);
        } catch (ValidationException e) {
            log.error(ConstantUtils.ERRO_TRANSACAO_CONTA, origem.getId(), e);
            throw new ValidationException(ConstantUtils.ERRO_TRANSACAO_CONTA + origem.getId());
        }
        log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, origem.getId());
        return TransferenciaResponse.toTransferenciaResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);
    }
    // pix
    @Transactional
    public PixResponse pix(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.PIX, id_contaOrigem);
        Conta origem = Validator.verificarContaExistente(contaRepository, id_contaOrigem);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Conta destino = Validator.verificarContaExistente(contaRepository, id_contaDestino);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = origem.getCliente();
        securityUseCase.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Validator.verificarSaldoSuficiente(valor, origem.getSaldo());
        log.info(ConstantUtils.SALDO_SUFICIENTE);
        try {
            origem.pix(destino, valor);
            contaRepository.save(origem);
            contaRepository.save(destino);
        } catch (ValidationException e) {
            log.error(ConstantUtils.ERRO_TRANSACAO_CONTA, origem.getId(), e);
            throw new ValidationException(ConstantUtils.ERRO_TRANSACAO_CONTA + origem.getId());
        }
        log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, origem.getId());
        return PixResponse.toPixResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);
    }
    // get saldo
    public SaldoResponse getSaldo(Long id_conta, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_LEITURA_SALDO, id_conta);
        Conta conta = Validator.verificarContaExistente(contaRepository, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = conta.getCliente();
        securityUseCase.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return SaldoResponse.toSaldoResponse(conta);
    }
    // deposito
    @Transactional
    public DepositoResponse depositar(Long id_conta, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.DEPOSITO, id_conta);
        Conta conta = Validator.verificarContaExistente(contaRepository, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        try {
            conta.depositar(valor);
            contaRepository.save(conta);
        } catch (ValidationException e) {
            log.error(ConstantUtils.ERRO_TRANSACAO_CONTA, conta.getId(), e);
            throw new ValidationException(ConstantUtils.ERRO_TRANSACAO_CONTA + conta.getId());
        }
        log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, conta.getId());
        return DepositoResponse.toDepositoResponse(conta.getNumeroConta(), valor, conta.getSaldo());
    }
    // saque
    @Transactional
    public SaqueResponse sacar(Long id_conta, Usuario usuarioLogado, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.SAQUE, id_conta);
        Conta conta = Validator.verificarContaExistente(contaRepository, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = conta.getCliente();
        securityUseCase.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Validator.verificarSaldoSuficiente(valor, conta.getSaldo());
        log.info(ConstantUtils.SALDO_SUFICIENTE);
        try {
            conta.sacar(valor);
            contaRepository.save(conta);
        } catch (ValidationException e) {
            log.error(ConstantUtils.ERRO_TRANSACAO_CONTA, conta.getId(), e);
            throw new ValidationException(ConstantUtils.ERRO_TRANSACAO_CONTA + conta.getId());
        }
        log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, conta.getId());
        return SaqueResponse.toSaqueResponse(conta.getNumeroConta(), valor, conta.getSaldo());
    }
    // txmanutencao
    @Transactional
    public AplicarTxManutencaoResponse debitarTarifaManutencao(Long id_conta) {
        log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.APLICACAO_TARIFA_MANUTENCAO, id_conta);
        Conta cc = Validator.verificarContaExistente(contaRepository, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Validator.verificarSaldoSuficiente(cc.getTarifaManutencao(), cc.getSaldo());
        log.info(ConstantUtils.SALDO_SUFICIENTE);
        try {
            cc.aplicarTarifaManutencao();
            contaRepository.save(cc);
        } catch (ValidationException e) {
            log.error(ConstantUtils.ERRO_TRANSACAO_CONTA, cc.getId(), e);
            throw new ValidationException(ConstantUtils.ERRO_TRANSACAO_CONTA + cc.getId());
        }
        log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, cc.getId());
        return AplicarTxManutencaoResponse.toAplicarTxManutencaoResponse(cc.getNumeroConta(), cc.getTarifaManutencao(), cc.getSaldo());
    }
    // rendimento
    @Transactional
    public AplicarTxRendimentoResponse creditarRendimento(Long id_conta) {
        log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.APLICACAO_RENDIMENTO, id_conta);
        Conta cp = Validator.verificarContaExistente(contaRepository, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        try {
            cp.aplicarRendimento();
            contaRepository.save(cp);
        } catch (ValidationException e) {
            log.error(ConstantUtils.ERRO_TRANSACAO_CONTA, cp.getId(), e);
            throw new ValidationException(ConstantUtils.ERRO_TRANSACAO_CONTA + cp.getId());
        }
        log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, cp.getId());
        return AplicarTxRendimentoResponse.toAplicarTxRendimentoResponse(cp.getNumeroConta(), cp.getTaxaRendimento(), cp.getSaldo());

    }
    
    
}

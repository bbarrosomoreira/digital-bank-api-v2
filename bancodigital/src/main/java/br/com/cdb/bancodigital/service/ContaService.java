package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.cdb.bancodigital.model.*;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dao.CartaoDAO;
import br.com.cdb.bancodigital.dao.ClienteDAO;
import br.com.cdb.bancodigital.dao.ContaDAO;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.dto.response.AplicarTxManutencaoResponse;
import br.com.cdb.bancodigital.dto.response.AplicarTxRendimentoResponse;
import br.com.cdb.bancodigital.dto.response.ContaResponse;
import br.com.cdb.bancodigital.dto.response.DepositoResponse;
import br.com.cdb.bancodigital.dto.response.PixResponse;
import br.com.cdb.bancodigital.dto.response.SaldoResponse;
import br.com.cdb.bancodigital.dto.response.SaqueResponse;
import br.com.cdb.bancodigital.dto.response.TransferenciaResponse;

@Service
@AllArgsConstructor
@Slf4j
public class ContaService {

    private final ContaDAO contaDAO;
    private final ClienteDAO clienteDAO;
    private final CartaoDAO cartaoDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxaDAO;
    private final ConversorMoedasService conversorMoedasService;
    private final SecurityService securityService;

    // addConta de forma genérica
    @Transactional
    public ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {
        log.info(ConstantUtils.INICIO_ABERTURA_CONTA, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO);
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Conta contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
        log.info(ConstantUtils.CONTA_CRIADA, contaNova.getId());
        try {
            contaDAO.salvar(contaNova);
            log.info(ConstantUtils.CONTA_SALVA_BANCO, contaNova.getId());
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_SALVAR_CONTA, e.getMessage());
            throw new ValidationException(ConstantUtils.ERRO_ABRIR_CONTA_MENSAGEM_EXCEPTION);
        }
        log.info(ConstantUtils.SUCESSO_ABERTURA_CONTA, id_cliente);
        return toResponse(contaNova);
    }

    private Conta criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);
        PoliticaDeTaxas parametros = Validator.verificarPoliticaExitente(politicaDeTaxaDAO, cliente.getCategoria());
        log.info(ConstantUtils.POLITICA_TAXAS_ENCONTRADA);

        return switch (tipo) {
            case CORRENTE -> {
                yield criarContaCorrente(cliente, tipo, moeda, valorDeposito, parametros);
            }
            case POUPANCA -> {
                yield criarContaPoupanca(cliente, tipo, moeda, valorDeposito, parametros);
            }
            case INTERNACIONAL -> {
                yield criarContaInternacional(cliente, tipo, moeda, valorDeposito, parametros);
            }
        };
    }

    private Conta criarContaCorrente(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta cc = new Conta(cliente, tipo);
        cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
        cc.setMoeda(moeda);
        cc.setSaldo(valorDeposito);
        return cc;
    }

    private Conta criarContaPoupanca(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta cp = new Conta(cliente, tipo);
        cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
        cp.setMoeda(moeda);
        cp.setSaldo(valorDeposito);
        return cp;
    }

    private Conta criarContaInternacional(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta ci = new Conta(cliente, tipo);
        ci.setTarifaManutencao(parametros.getTarifaManutencaoContaInternacional());
        ci.setMoeda(moeda);
        ci.setSaldoEmReais(valorDeposito);
        BigDecimal saldoMoedaExtrangeira = conversorMoedasService.converterDeBrl(ci.getMoeda(), ci.getSaldoEmReais());
        ci.setSaldo(saldoMoedaExtrangeira);
        return ci;
    }

    // get contas
    public List<ContaResponse> getContas() {
        log.info(ConstantUtils.INICIO_BUSCA_CONTA);
        List<Conta> contas = contaDAO.buscarTodasContas();
        return contas.stream().map(this::toResponse).toList();
    }

    // get conta por usuário
    public List<ContaResponse> listarPorUsuario(Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CONTA_POR_USUARIO, usuarioLogado.getId());
        List<Conta> contas = contaDAO.buscarContaPorClienteUsuario(usuarioLogado);
        return contas.stream().map(this::toResponse).toList();
    }

    // get conta por cliente
    public List<ContaResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CONTA_POR_CLIENTE, id_cliente);
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO);
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        List<Conta> contas = contaDAO.buscarContaPorClienteId(id_cliente);
        return contas.stream().map(this::toResponse).toList();
    }

    // get uma conta
    public ContaResponse getContaById(Long id_conta, Usuario usuarioLogado) {
        log.info(ConstantUtils.INICIO_BUSCA_CONTA);
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = conta.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return toResponse(conta);
    }

    // deletar contas de cliente
    @Transactional
    public void deleteContasByCliente(Long id_cliente) {
        log.info(ConstantUtils.INICIO_DELETE_CONTA, id_cliente);
        List<Conta> contas = contaDAO.buscarContaPorClienteId(id_cliente);
        if (contas.isEmpty()) {
            log.info(ConstantUtils.CLIENTE_SEM_CONTAS, id_cliente);
            return;
        }
        for (Conta conta : contas) {
            try {
                Validator.verificarCartoesVinculados(cartaoDAO, conta);
                Validator.verificarSaldoRemanescente(conta);
                contaDAO.deletarContaPorId(conta.getId());
                log.info(ConstantUtils.SUCESSO_DELETE_CONTA, conta.getCliente().getId());

            } catch (DataIntegrityViolationException e) {
                log.error(ConstantUtils.ERRO_INESPERADO_DELETE_CONTA, conta.getId(), e);
                throw new ValidationException(ConstantUtils.ERRO_INESPERADO_DELETE_CONTA + conta.getId());
            }
        }
    }

    // transferencia
    @Transactional
    public TransferenciaResponse transferir(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.TRANSFERENCIA, id_contaOrigem);
        Conta origem = Validator.verificarContaExistente(contaDAO, id_contaOrigem);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Conta destino = Validator.verificarContaExistente(contaDAO, id_contaDestino);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = origem.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Validator.verificarSaldoSuficiente(valor, origem.getSaldo());
        log.info(ConstantUtils.SALDO_SUFICIENTE);
        try {
            origem.transferir(destino, valor);
            contaDAO.salvar(origem);
            contaDAO.salvar(destino);
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
        Conta origem = Validator.verificarContaExistente(contaDAO, id_contaOrigem);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Conta destino = Validator.verificarContaExistente(contaDAO, id_contaDestino);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = origem.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Validator.verificarSaldoSuficiente(valor, origem.getSaldo());
        log.info(ConstantUtils.SALDO_SUFICIENTE);
        try {
        origem.pix(destino, valor);
        contaDAO.salvar(origem);
        contaDAO.salvar(destino);
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
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = conta.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        return SaldoResponse.toSaldoResponse(conta);
    }

    // deposito
    @Transactional
    public DepositoResponse depositar(Long id_conta, BigDecimal valor) {
        log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.DEPOSITO, id_conta);
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        try {
        conta.depositar(valor);
        contaDAO.salvar(conta);
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
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Cliente cliente = conta.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Validator.verificarSaldoSuficiente(valor, conta.getSaldo());
        log.info(ConstantUtils.SALDO_SUFICIENTE);
        try {
        conta.sacar(valor);
        contaDAO.salvar(conta);
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
        Conta cc = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        Validator.verificarSaldoSuficiente(cc.getTarifaManutencao(), cc.getSaldo());
        log.info(ConstantUtils.SALDO_SUFICIENTE);
        try {
        cc.aplicarTarifaManutencao();
        contaDAO.salvar(cc);
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
        Conta cp = Validator.verificarContaExistente(contaDAO, id_conta);
        log.info(ConstantUtils.CONTA_ENCONTRADA);
        try {
        cp.aplicarRendimento();
        contaDAO.salvar(cp);
        } catch (ValidationException e) {
            log.error(ConstantUtils.ERRO_TRANSACAO_CONTA, cp.getId(), e);
            throw new ValidationException(ConstantUtils.ERRO_TRANSACAO_CONTA + cp.getId());
        }
        log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, cp.getId());
        return AplicarTxRendimentoResponse.toAplicarTxRendimentoResponse(cp.getNumeroConta(), cp.getTaxaRendimento(), cp.getSaldo());

    }

    //M
    public ContaResponse toResponse(Conta conta) {
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



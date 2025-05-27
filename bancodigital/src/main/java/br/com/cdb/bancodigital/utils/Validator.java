package br.com.cdb.bancodigital.utils;

import br.com.cdb.bancodigital.dao.*;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.model.*;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Status;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class Validator {

    private Validator() {
        throw new IllegalStateException("Utility class");
    }

    public static void validarCpfUnico(ClienteDAO clienteDAO, String cpf) {
        if (clienteDAO.existsByCpf(cpf))
            throw new ResourceAlreadyExistsException(ConstantUtils.CPF_JA_CADASTRADO);
    }
    public static void validarMaiorIdade(Cliente cliente) {
        if (cliente.isMenorDeIdade())
            throw new ValidationException(ConstantUtils.CLIENTE_MAIOR_18);
    }
    public static Cliente verificarClienteExistente(ClienteDAO clienteDAO, Long id_cliente) {
        return clienteDAO.buscarClienteporId(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException((ConstantUtils.ERRO_BUSCA_CLIENTE + id_cliente)));
    }
    public static Conta verificarContaExistente(ContaDAO contaDAO, Long id_conta) {
        return contaDAO.buscarContaPorId(id_conta)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ConstantUtils.ERRO_BUSCA_CONTA, id_conta)));
    }
    public static Cartao verificarCartaoExistente(CartaoDAO cartaoDAO, Long id_cartao) {
        return cartaoDAO.buscarCartaoById(id_cartao)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ConstantUtils.ERRO_BUSCA_CARTAO, id_cartao)));
    }
    public static Seguro verificarSeguroExistente(SeguroDAO seguroDAO, Long id_seguro) {
        return seguroDAO.buscarSeguroPorId(id_seguro)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ConstantUtils.ERRO_BUSCA_SEGURO, id_seguro)));
    }
    public static PoliticaDeTaxas verificarPoliticaExitente(PoliticaDeTaxasDAO politicaDeTaxasDAO, CategoriaCliente categoria) {
        return politicaDeTaxasDAO.findByCategoria(categoria)
                .orElseThrow(() -> new ResourceNotFoundException((ConstantUtils.ERRO_BUSCA_POLITICA_TAXAS + categoria)));
    }
    public static void verificarCartaoAtivo(Status status) {
        if (status.equals(Status.INATIVO))
            throw new InvalidInputParameterException(ConstantUtils.CARTAO_INATIVO_OPERACAO_BLOQUEADA);
    }
    public static void verificaSeTemFaturaAbertaDeCartaoCredito(Cartao cartao) {
        if (cartao.getTotalFatura().compareTo(BigDecimal.ZERO) > 0)
            throw new InvalidInputParameterException(ConstantUtils.CARTAO_FATURA_ABERTA);
    }
    public static void verificarSenhaCorreta(String senhaDigitada, String senhaCartao) {
        if (!senhaDigitada.equals(senhaCartao)) throw new ValidationException(ConstantUtils.SENHA_INCORRETA);
    }
    public static void verificarLimiteSuficiente(BigDecimal valor, BigDecimal limiteAtual) {
        if (valor.compareTo(limiteAtual) > 0)
            throw new InvalidInputParameterException(String.format(ConstantUtils.LIMITE_INSUFICIENTE, limiteAtual));
    }
    public static void verificarSaldoSuficiente(BigDecimal valor, BigDecimal saldo) {
        if (valor.compareTo(saldo) > 0)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SALDO_INSUFICIENTE);
    }
    public static void verificarSaldoRemanescente(Conta conta) {
        if (conta.getSaldo() != null && conta.getSaldo().compareTo(BigDecimal.ZERO) > 0)
            throw new InvalidInputParameterException(ConstantUtils.ERRO_SALDO_REMANESCENTE);
    }
}

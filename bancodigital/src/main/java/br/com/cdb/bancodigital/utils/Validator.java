package br.com.cdb.bancodigital.utils;

import br.com.cdb.bancodigital.dao.*;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Status;

import java.math.BigDecimal;

public class Validator {

    private Validator() {
        throw new IllegalStateException("Utility class");
    }

    public static void validarCpfUnico(ClienteDAO clienteDAO, String cpf) {
        if (clienteDAO.existsByCpf(cpf))
            throw new ResourceAlreadyExistsException("CPF já cadastrado no sistema.");
    }
    public static void validarMaiorIdade(Cliente cliente) {
        if (cliente.isMenorDeIdade())
            throw new ValidationException("Cliente deve ser maior de 18 anos para se cadastrar.");
    }
    public static Cliente verificarClienteExistente(ClienteDAO clienteDAO, Long id_cliente) {
        return clienteDAO.buscarClienteporId(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ConstantUtils.ERRO_CLIENTE_NAO_ENCONTRADO, id_cliente)));
    }
    public static Conta verificarContaExistente(ContaDAO contaDAO, Long id_conta) {
        return contaDAO.buscarContaPorId(id_conta)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
    }
    public static Cartao verificarCartaoExistente(CartaoDAO cartaoDAO, Long id_cartao) {
        return cartaoDAO.findCartaoById(id_cartao)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
    }
    public static PoliticaDeTaxas verificarPolitiaExitente(PoliticaDeTaxasDAO politicaDeTaxasDAO, CategoriaCliente categoria) {
        return politicaDeTaxasDAO.findByCategoria(categoria)
                .orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
    }
    public static void verificarCartaoAtivo(Status status) {
        if (status.equals(Status.INATIVO))
            throw new InvalidInputParameterException("Cartão desativado - operação bloqueada");
    }
    public static void verificaSeTemFaturaAbertaDeCartaoCredito(Cartao cartao) {
        if (cartao.getTotalFatura().compareTo(BigDecimal.ZERO) > 0)
            throw new InvalidInputParameterException("Cartão não pode ser desativado com fatura em aberto.");
    }
    public static void verificarSenhaCorreta(String senhaDigitada, String senhaCartao) {
        if (!senhaDigitada.equals(senhaCartao)) throw new ValidationException("A senha informada está incorreta!");
    }
    public static void verificarLimiteSuficiente(BigDecimal valor, BigDecimal limiteAtual) {
        if (valor.compareTo(limiteAtual) > 0)
            throw new InvalidInputParameterException("Limite insuficiente para esta transação. Limite atual: " + (limiteAtual));
    }
    public static void verificarSegurosVinculados(SeguroDAO seguroDAO, Cartao cartao) {
        Long id = cartao.getId();
        boolean existeSeguro = seguroDAO.existsByCartaoId(id);
        if (existeSeguro) {
            throw new InvalidInputParameterException("Cartão não pode ser excluído com seguros vinculados.");
        }
    }
}

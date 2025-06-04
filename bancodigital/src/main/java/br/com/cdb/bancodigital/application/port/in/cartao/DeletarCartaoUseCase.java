package br.com.cdb.bancodigital.application.port.in.cartao;

public interface DeletarCartaoUseCase {
    void deleteCartoesByCliente(Long id_cliente);
}

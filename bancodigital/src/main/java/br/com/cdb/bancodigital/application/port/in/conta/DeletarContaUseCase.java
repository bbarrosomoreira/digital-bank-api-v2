package br.com.cdb.bancodigital.application.port.in.conta;

public interface DeletarContaUseCase {
    void deleteContasByCliente(Long id_cliente);
}

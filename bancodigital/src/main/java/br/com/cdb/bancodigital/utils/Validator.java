package br.com.cdb.bancodigital.utils;

import br.com.cdb.bancodigital.dao.ClienteDAO;
import br.com.cdb.bancodigital.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.model.Cliente;

public class Validator {

    public static void validarCpfUnico(ClienteDAO clienteDAO, String cpf) {
        if (clienteDAO.existsByCpf(cpf))
            throw new ResourceAlreadyExistsException("CPF já cadastrado no sistema.");
    }
    public static void validarMaiorIdade(Cliente cliente) {
        if (cliente.isMenorDeIdade())
            throw new ValidationException("Cliente deve ser maior de 18 anos para se cadastrar.");
    }
}

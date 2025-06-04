package br.com.cdb.bancodigital.application.port.in.conta;

import br.com.cdb.bancodigital.application.core.domain.dto.response.ContaResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoConta;

import java.math.BigDecimal;

public interface AbrirContaUseCase {
    ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito);
}

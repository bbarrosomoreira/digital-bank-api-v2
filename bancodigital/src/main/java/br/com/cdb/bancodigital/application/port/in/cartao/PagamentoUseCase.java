package br.com.cdb.bancodigital.application.port.in.cartao;

import br.com.cdb.bancodigital.application.core.domain.dto.response.FaturaPagaResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.FaturaResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.PagamentoResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;

import java.math.BigDecimal;

public interface PagamentoUseCase {
    PagamentoResponse pagar(Long id_cartao, Usuario usuarioLogado, BigDecimal valor, String senha, String descricao);
    FaturaResponse getFatura(Long id_cartao, Usuario usuarioLogado);
    FaturaPagaResponse pagarFatura(Long id_cartao, Usuario usuarioLogado);
}

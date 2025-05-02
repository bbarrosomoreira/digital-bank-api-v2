package br.com.cdb.bancodigital.mapper;

import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.enums.Status;
import br.com.cdb.bancodigital.model.enums.TipoCartao;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class CartaoMapper implements RowMapper<Cartao> {

    @Override
    public Cartao mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Cartao cartao = new Cartao();
        cartao.setId(rs.getLong("id"));
        cartao.setTipoCartao(TipoCartao.fromString(rs.getString("tipo_cartao")));
        cartao.setNumeroCartao(rs.getString("numero_cartao"));
        cartao.setConta(null);
        cartao.setStatus(Status.fromString(rs.getString("status")));
        cartao.setSenha(rs.getString("senha"));
        cartao.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
        cartao.setDataVencimento(rs.getDate("data_vencimento").toLocalDate());
        cartao.setTaxaUtilizacao(rs.getBigDecimal("taxa_utilizacao"));
        cartao.setLimite(rs.getBigDecimal("limite"));
        cartao.setLimiteAtual(rs.getBigDecimal("limite_atual"));
        cartao.setTotalFatura(rs.getBigDecimal("total_fatura"));
        cartao.setTotalFaturaPaga(rs.getBigDecimal("total_fatura_paga"));

        return cartao;
    }
}

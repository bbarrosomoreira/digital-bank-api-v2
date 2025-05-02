package br.com.cdb.bancodigital.mapper;

import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.enums.Status;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class SeguroMapper implements RowMapper<Seguro> {

    @Override
    public Seguro mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Seguro seguro = new Seguro();
        seguro.setId(rs.getLong("id"));
        seguro.setTipoSeguro(TipoSeguro.fromString(rs.getString("tipo_seguro")));
        seguro.setNumApolice(rs.getString("num_apolice"));
        seguro.setCartao(null);
        seguro.setDataContratacao(rs.getDate("data_contratacao").toLocalDate());
        seguro.setValorApolice(rs.getBigDecimal("valor_apolice"));
        seguro.setDescricaoCondicoes(rs.getString("descricao_condicoes"));
        seguro.setPremioApolice(rs.getBigDecimal("premio_apolice"));
        seguro.setStatusSeguro(Status.fromString(rs.getString("status_seguro")));
        seguro.setDataAcionamento(rs.getDate("data_acionamento").toLocalDate());
        seguro.setValorFraude(rs.getBigDecimal("valor_fraude"));

        return seguro;
    }
}

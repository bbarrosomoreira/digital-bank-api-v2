package br.com.cdb.bancodigital.mapper;

import br.com.cdb.bancodigital.adapters.out.dao.CartaoDAO;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.application.core.domain.model.Cartao;
import br.com.cdb.bancodigital.application.core.domain.model.Seguro;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Status;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class SeguroMapper implements RowMapper<Seguro> {

    public final CartaoDAO cartaoDAO;

    @Override
    public Seguro mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Seguro seguro = new Seguro();
        seguro.setId(rs.getLong("id"));
        seguro.setTipoSeguro(TipoSeguro.fromString(rs.getString("tipo_seguro")));
        seguro.setNumApolice(rs.getString("num_apolice"));

        Long cartaoId = rs.getLong("cartao_id");
        Cartao cartao = cartaoDAO.findById(cartaoId)
                        .orElseThrow(()-> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CARTAO + cartaoId));
        seguro.setCartao(cartao);

        seguro.setDataContratacao(rs.getDate("data_contratacao").toLocalDate());
        seguro.setValorApolice(rs.getBigDecimal("valor_apolice"));
        seguro.setDescricaoCondicoes(rs.getString("descricao_condicoes"));
        seguro.setPremioApolice(rs.getBigDecimal("premio_apolice"));
        seguro.setStatusSeguro(Status.fromString(rs.getString("status_seguro")));
        seguro.setDataAcionamento(rs.getDate("data_acionamento") != null ? rs.getDate("data_acionamento").toLocalDate() : null);
        seguro.setValorFraude(rs.getBigDecimal("valor_fraude"));

        return seguro;
    }
}

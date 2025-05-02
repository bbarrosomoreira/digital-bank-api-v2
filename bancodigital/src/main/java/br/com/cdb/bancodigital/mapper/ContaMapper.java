package br.com.cdb.bancodigital.mapper;

import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ContaMapper implements RowMapper<Conta> {

    @Override
    public Conta mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Conta conta = new Conta();
        conta.setId(rs.getLong("id"));
        conta.setNumeroConta(rs.getString("numero_conta"));
        conta.setSaldo(rs.getBigDecimal("saldo"));
        conta.setMoeda(Moeda.fromString(rs.getString("moeda")));
        conta.setCliente(null);
        conta.setDataCriacao(rs.getDate("data_criacao").toLocalDate());
        conta.setTipoConta(TipoConta.fromString(rs.getString("tipo_conta")));
        conta.setTarifaManutencao(rs.getBigDecimal("tarifa_manutencao"));
        conta.setTaxaRendimento(rs.getBigDecimal("taxa_rendimento"));
        conta.setSaldoEmReais(rs.getBigDecimal("saldo_em_reais"));

        return conta;
    }
}

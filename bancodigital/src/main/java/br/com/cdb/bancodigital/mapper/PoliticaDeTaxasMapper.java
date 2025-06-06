package br.com.cdb.bancodigital.mapper;

import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class PoliticaDeTaxasMapper implements RowMapper<PoliticaDeTaxas> {

    @Override
    public PoliticaDeTaxas mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        PoliticaDeTaxas politicaDeTaxas = new PoliticaDeTaxas();
        politicaDeTaxas.setId(rs.getLong("id"));
        politicaDeTaxas.setCategoria(CategoriaCliente.fromString(rs.getString("categoria")));
        politicaDeTaxas.setTarifaManutencaoMensalContaCorrente(rs.getBigDecimal("tarifa_manutencao_mensal_conta_corrente"));
        politicaDeTaxas.setRendimentoPercentualMensalContaPoupanca(rs.getBigDecimal("rendimento_percentual_mensal_conta_poupanca"));
        politicaDeTaxas.setTarifaManutencaoContaInternacional(rs.getBigDecimal("tarifa_manutencao_conta_internacional"));
        politicaDeTaxas.setLimiteCartaoCredito(rs.getBigDecimal("limite_cartao_credito"));
        politicaDeTaxas.setLimiteDiarioDebito(rs.getBigDecimal("limite_diario_debito"));
        politicaDeTaxas.setTarifaSeguroViagem(rs.getBigDecimal("tarifa_seguro_viagem"));
        politicaDeTaxas.setTarifaSeguroFraude(rs.getBigDecimal("tarifa_seguro_fraude"));
        politicaDeTaxas.setValorApoliceFraude(rs.getBigDecimal("valor_apolice_fraude"));
        politicaDeTaxas.setValorApoliceViagem(rs.getBigDecimal("valor_apolice_viagem"));

        return politicaDeTaxas;
    }
}

package br.com.cdb.bancodigital.application.core.domain.mapper;

import br.com.cdb.bancodigital.adapter.output.dao.ClienteDAO;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.Conta;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoConta;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class ContaMapper implements RowMapper<Conta> {

    private final ClienteDAO clienteDAO;

    @Override
    public Conta mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Conta conta = new Conta();
        conta.setId(rs.getLong("id"));
        conta.setNumeroConta(rs.getString("numero_conta"));
        conta.setSaldo(rs.getBigDecimal("saldo"));
        conta.setMoeda(Moeda.fromString(rs.getString("moeda")));

        Long clienteId = rs.getLong("cliente_id");
        Cliente cliente = clienteDAO.findById(clienteId)
                        .orElseThrow(()-> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CLIENTE));
        conta.setCliente(cliente);

        conta.setDataCriacao(rs.getDate("data_criacao").toLocalDate());
        conta.setTipoConta(TipoConta.fromString(rs.getString("tipo_conta")));
        conta.setTarifaManutencao(rs.getBigDecimal("tarifa_manutencao"));
        conta.setTaxaRendimento(rs.getBigDecimal("taxa_rendimento"));
        conta.setSaldoEmReais(rs.getBigDecimal("saldo_em_reais"));

        return conta;
    }
}

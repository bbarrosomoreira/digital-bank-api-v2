package br.com.cdb.bancodigital.adapter.output.dao.rowMapper;

import br.com.cdb.bancodigital.adapter.output.dao.ClienteDAO;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Endereco;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class EnderecoClienteRowMapper implements RowMapper<Endereco> {

    private final ClienteDAO clienteDAO;

    @Override
    public Endereco mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getLong("id"));
        endereco.setCep(rs.getString("cep"));
        endereco.setRua(rs.getString("rua"));
        endereco.setNumero(rs.getInt("numero"));
        endereco.setComplemento(rs.getString("complemento"));
        endereco.setBairro(rs.getString("bairro"));
        endereco.setCidade(rs.getString("cidade"));
        endereco.setEstado(rs.getString("estado"));

        long clienteId = rs.getLong("cliente_id");
        Cliente cliente = clienteDAO.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CLIENTE));
        endereco.setCliente(cliente);

        return endereco;
    }
}

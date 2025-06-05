package br.com.cdb.bancodigital.application.core.domain.mapper;

import br.com.cdb.bancodigital.adapter.output.dao.ClienteDAO;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.EnderecoCliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class EnderecoClienteMapper implements RowMapper<EnderecoCliente> {

    private final ClienteDAO clienteDAO;

    @Override
    public EnderecoCliente mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        EnderecoCliente enderecoCliente = new EnderecoCliente();
        enderecoCliente.setId(rs.getLong("id"));
        enderecoCliente.setCep(rs.getString("cep"));
        enderecoCliente.setRua(rs.getString("rua"));
        enderecoCliente.setNumero(rs.getInt("numero"));
        enderecoCliente.setComplemento(rs.getString("complemento"));
        enderecoCliente.setBairro(rs.getString("bairro"));
        enderecoCliente.setCidade(rs.getString("cidade"));
        enderecoCliente.setEstado(rs.getString("estado"));

        long clienteId = rs.getLong("cliente_id");
        Cliente cliente = clienteDAO.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CLIENTE));
        enderecoCliente.setCliente(cliente);

        return enderecoCliente;
    }
}

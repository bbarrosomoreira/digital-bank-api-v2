package br.com.cdb.bancodigital.config;

import br.com.cdb.bancodigital.exceptions.custom.CommunicationException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@Order(1)
public class ConexaoPGadmin {

    private final DataSource dataSource;

    public ConexaoPGadmin(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public Connection getConnection() {
        try{
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new CommunicationException(ConstantUtils.ERRO_CONEXAO_BANCO);
        }
    }
}

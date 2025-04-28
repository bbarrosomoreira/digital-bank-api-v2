package br.com.cdb.bancodigitaljpa.config;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConexaoPGadmin {

    private final DataSource dataSource;

    public ConexaoPGadmin(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        try{
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco PostgreSQL", e);
        }
    }


}

package br.com.cdb.bancodigital.config;

import br.com.cdb.bancodigital.exceptions.custom.CommunicationException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@AllArgsConstructor
@Order(1)
@Slf4j
public class ConexaoPGadmin {

    private final DataSource dataSource;

    @PostConstruct
    public Connection getConnection() {
        log.info(ConstantUtils.INICIO_CONEXAO_BANCO);
        try{
            log.info(ConstantUtils.SUCESSO_CONEXAO_BANCO);
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error(ConstantUtils.ERRO_CONEXAO_BANCO);
            throw new CommunicationException(ConstantUtils.ERRO_CONEXAO_BANCO);
        }
    }
}

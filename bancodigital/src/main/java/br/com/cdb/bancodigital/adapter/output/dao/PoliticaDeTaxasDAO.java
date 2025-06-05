package br.com.cdb.bancodigital.adapter.output.dao;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.application.port.out.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.application.core.domain.mapper.PoliticaDeTaxasMapper;
import br.com.cdb.bancodigital.application.core.domain.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoliticaDeTaxasDAO implements PoliticaDeTaxasRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PoliticaDeTaxasMapper politicaDeTaxasMapper;

    public List<PoliticaDeTaxas> findAll() {
        log.info(ConstantUtils.INICIO_LISTAGEM_POLITICA_TAXAS);
        try {
            List<PoliticaDeTaxas> politicas = jdbcTemplate.query(SqlQueries.SQL_READ_ALL_POLITICA_DE_TAXAS, politicaDeTaxasMapper);
            log.info(ConstantUtils.SUCESSO_LISTAGEM_POLITICA_TAXAS);
            return politicas;
        } catch (SystemException e) {
            log.error(ConstantUtils.ERRO_LISTAGEM_POLITICA_TAXAS, e);
            throw new SystemException(ConstantUtils.ERRO_LISTAGEM_POLITICA_TAXAS);
        }
    }

    public Optional<PoliticaDeTaxas> findByCategoria(CategoriaCliente categoriaCliente) {
        log.info(ConstantUtils.INICIO_BUSCA_POLITICA_TAXAS, categoriaCliente);
        try {
            PoliticaDeTaxas politicaDeTaxas = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_POLITICA_DE_TAXAS_BY_CATEGORIA, politicaDeTaxasMapper, categoriaCliente.name());
            if (politicaDeTaxas == null) {
                log.warn(ConstantUtils.ERRO_POLITICA_TAXAS_NULA);
                return Optional.empty();
            }
            log.info(ConstantUtils.SUCESSO_BUSCA_POLITICA_TAXAS, categoriaCliente);
            return Optional.of(politicaDeTaxas);
        } catch (EmptyResultDataAccessException e) {
            log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_POLITICA_TAXAS + categoriaCliente, ConstantUtils.RETORNO_VAZIO);
            return Optional.empty();
        }
    }
}

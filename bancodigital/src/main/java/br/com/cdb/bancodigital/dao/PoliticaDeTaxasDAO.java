package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.mapper.PoliticaDeTaxasMapper;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PoliticaDeTaxasDAO{

    private final JdbcTemplate jdbcTemplate;
    private final PoliticaDeTaxasMapper politicaDeTaxasMapper;

    public List<PoliticaDeTaxas> findAll() {
        return jdbcTemplate.query(SqlQueries.SQL_READ_ALL_POLITICA_DE_TAXAS, politicaDeTaxasMapper);
    }

    public Optional<PoliticaDeTaxas> findByCategoria(CategoriaCliente categoriaCliente) {
        try {
            PoliticaDeTaxas politicaDeTaxas = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_POLITICA_DE_TAXAS_BY_CATEGORIA, politicaDeTaxasMapper, categoriaCliente.name());
            return Optional.of(politicaDeTaxas);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

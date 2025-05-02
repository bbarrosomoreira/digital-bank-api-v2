package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.mapper.PoliticaDeTaxasMapper;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
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
        String sql = "SELECT * FROM politica_de_taxas";
        return jdbcTemplate.query(sql, politicaDeTaxasMapper);
    }

    public Optional<PoliticaDeTaxas> findByCategoria(CategoriaCliente categoriaCliente) {
        String sql = "SELECT * FROM politica_de_taxas WHERE categoria = ?";
        try {
            PoliticaDeTaxas politicaDeTaxas = jdbcTemplate.queryForObject(sql, politicaDeTaxasMapper, categoriaCliente);
            return Optional.of(politicaDeTaxas);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

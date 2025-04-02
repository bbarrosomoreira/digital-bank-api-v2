package br.com.cdb.bancodigitaljpa.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.cdb.bancodigitaljpa.entity.Parametros;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.repository.ParametrosRepository;

@Component
public class ParametrosSeeder implements CommandLineRunner {
	
	@Autowired
	private ParametrosRepository parametrosRepository;
	
	@Override
    public void run(String... args) {
        if (parametrosRepository.count() == 0) { // Evita duplicação de dados
            parametrosRepository.save(new Parametros(CategoriaCliente.COMUM, 
                new BigDecimal("12.00"), new BigDecimal("0.005"), 
                new BigDecimal("1000.00"), new BigDecimal("500.00"), new BigDecimal("50.00")));

            parametrosRepository.save(new Parametros(CategoriaCliente.SUPER, 
                new BigDecimal("8.00"), new BigDecimal("0.007"), 
                new BigDecimal("5000.00"), new BigDecimal("2500.00"), new BigDecimal("50.00")));

            parametrosRepository.save(new Parametros(CategoriaCliente.PREMIUM, 
                new BigDecimal("0.00"), new BigDecimal("0.009"), 
                new BigDecimal("10000.00"), new BigDecimal("5000.00"), new BigDecimal("0.00")));
        }
    }
	
	
}

package br.com.cdb.bancodigital.application.core.service.politicaDeTaxas;

import br.com.cdb.bancodigital.application.core.domain.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.application.port.in.politicaDeTaxas.PoliticaDeTaxasUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PoliticaDeTaxasService implements PoliticaDeTaxasUseCase {

    private final PoliticaDeTaxasRepository politicaDeTaxasRepository;

    public PoliticaDeTaxas buscarParametosPorCategoria(CategoriaCliente categoria) {
        log.info(ConstantUtils.INICIO_BUSCA_POLITICA_TAXAS, categoria);
        try {
            PoliticaDeTaxas politica = politicaDeTaxasRepository.findByCategoria(categoria)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format(ConstantUtils.ERRO_BUSCA_POLITICA_TAXAS, categoria)
                    ));
            log.info(ConstantUtils.SUCESSO_BUSCA_POLITICA_TAXAS, categoria);
            return politica;
        } catch (ResourceNotFoundException ex) {
            log.error(ConstantUtils.ERRO_BUSCA_POLITICA_TAXAS, categoria);
            throw ex;
        }
    }

    public List<PoliticaDeTaxas> listarTodosParametros() {
        log.info(ConstantUtils.INICIO_LISTAGEM_POLITICA_TAXAS);
        try {
            List<PoliticaDeTaxas> lista = politicaDeTaxasRepository.findAll();
            log.info(ConstantUtils.SUCESSO_LISTAGEM_POLITICA_TAXAS);
            return lista;
        } catch (Exception ex) {
            log.error(ConstantUtils.ERRO_LISTAGEM_POLITICA_TAXAS);
            throw ex;
        }
    }
}

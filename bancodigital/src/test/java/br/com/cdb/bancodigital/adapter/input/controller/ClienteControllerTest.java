package br.com.cdb.bancodigital.adapter.input.controller;

import br.com.cdb.bancodigital.adapter.input.dto.EnderecoDTO;
import br.com.cdb.bancodigital.adapter.input.mapper.ClienteDTOMapper;
import br.com.cdb.bancodigital.adapter.input.dto.ClienteDTO;
import br.com.cdb.bancodigital.application.port.in.JwtUseCase;
import br.com.cdb.bancodigital.application.port.in.cliente.AtualizarClienteUseCase;
import br.com.cdb.bancodigital.application.port.in.cliente.CadastrarClienteUseCase;
import br.com.cdb.bancodigital.application.port.in.cliente.DeletarClienteUseCase;
import br.com.cdb.bancodigital.application.port.in.cliente.ListarClienteUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest (value = ClienteController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CadastrarClienteUseCase cadastrarClienteUseCase;

    @MockitoBean
    private ListarClienteUseCase listarClienteUseCase;

    @MockitoBean
    private AtualizarClienteUseCase atualizarClienteUseCase;

    @MockitoBean
    private DeletarClienteUseCase deletarClienteUseCase;

    @MockitoBean
    private ClienteDTOMapper clienteDTOMapper;

    @MockitoBean
    private JwtUseCase jwtUseCase;

    ClienteDTO clienteDTO;

    EnderecoDTO enderecoDTO;

    @BeforeEach
    public void setup() {
        // Configurações iniciais antes de cada teste, se necessário
        // Por exemplo, você pode inicializar mocks ou definir comportamentos esperados
        enderecoDTO = EnderecoDTO.builder()
                .numero(123)
                .complemento("Apto 456")
                .cep("12345678")
                .build();
        clienteDTO = ClienteDTO.builder()
                .nome("João da Silva")
                .cpf("12345678901")
                .dataNascimento(java.time.LocalDate.of(1990, 1, 1))
                .endereco(enderecoDTO)
                .build();
    }

    @Test
    public void testCadastrarCliente() throws Exception {
        // Implementar teste para o endpoint de cadastro de cliente
        mockMvc.perform(post("/cliente").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(clienteDTO)))
                 .andExpect(status().isCreated());
//                .andExpect(result -> {
//                     // Aqui você pode adicionar verificações adicionais, como o conteúdo da resposta
//                     // Exemplo: result.getResponse().getContentAsString().contains("sucesso");
//                 });
    }

}

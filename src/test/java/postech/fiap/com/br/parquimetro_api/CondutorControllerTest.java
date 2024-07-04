package postech.fiap.com.br.parquimetro_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;
import postech.fiap.com.br.parquimetro_api.controller.CondutorController;
import postech.fiap.com.br.parquimetro_api.controller.LocacaoController;
import postech.fiap.com.br.parquimetro_api.domain.condutor.CondutorDto;
import postech.fiap.com.br.parquimetro_api.domain.condutor.CondutorEntity;
import postech.fiap.com.br.parquimetro_api.domain.condutor.CondutorRepository;
import postech.fiap.com.br.parquimetro_api.domain.condutor.DadosDetalhamentoCondutorDto;
import postech.fiap.com.br.parquimetro_api.domain.endereco.DadosEndereco;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.VeiculoEntity;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CondutorController.class)
class CondutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CondutorRepository repository;

    @Test
    void deveCadastrarCondutorComSucesso() throws Exception {
        // Arrange: Crie um objeto CondutorDto de teste

        DadosEndereco dadosEndereco = new DadosEndereco("Rua dos Bobos", "Bairro Legal","12345678", "Cidade Maravilhosa",  "SP","Complemento","44");

        CondutorDto condutorDto = new CondutorDto("Marcos", "marcos@gmail.com","12345678",1L, dadosEndereco);

        CondutorEntity condutorEntity = new CondutorEntity(condutorDto);

        when(repository.save(any(CondutorEntity.class))).thenReturn(condutorEntity);

        DadosDetalhamentoCondutorDto dadosDetalhamentoCondutorDto=new DadosDetalhamentoCondutorDto(condutorEntity);

        // Act & Assert: Execute a requisição POST e verifique o status da resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/condutor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(condutorDto)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        verify(repository, times(1)).save(any(CondutorEntity.class));
    }
}
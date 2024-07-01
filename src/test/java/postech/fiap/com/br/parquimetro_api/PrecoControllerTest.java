package postech.fiap.com.br.parquimetro_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import postech.fiap.com.br.parquimetro_api.controller.PrecoController;
import postech.fiap.com.br.parquimetro_api.domain.preco.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrecoController.class)
class PrecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrecoRepository precoRepository;

    @Test
    void altera_comIdInexistente_retorna404() throws Exception {
        // Arrange
        var precoDto = new DadosAtualizacaoPrecoDto(5L, Tipo_Modalidade.HORA_CHEIA,20.0 );

        when(precoRepository.existsById(precoDto.id_preco())).thenReturn(false);

        // Act and Assert
        mockMvc.perform(put("/preco")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id_preco\":5,\"modalidade\":\"HORA_CHEIA\",\"valor\":20.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cadastrar() throws Exception {
        var novoPreco = new PrecoDto(
                Tipo_Modalidade.HORA_CHEIA,
                10.0
        );

        ObjectMapper objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString("/preco");

        mockMvc.perform(MockMvcRequestBuilders.post("/preco")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id_preco\":5,\"modalidade\":\"HORA_CHEIA\",\"valor\":10.0}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.modalidade").value("HORA_CHEIA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.valor").value(10.0));
    }


}
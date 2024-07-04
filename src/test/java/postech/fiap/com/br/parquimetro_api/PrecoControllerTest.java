package postech.fiap.com.br.parquimetro_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import postech.fiap.com.br.parquimetro_api.controller.PrecoController;
import postech.fiap.com.br.parquimetro_api.domain.preco.*;

import java.util.ArrayList;
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
        var precoDto = new DadosAtualizacaoPrecoDto(5L, Tipo_Modalidade.HORA_CHEIA, 20.0);

        when(precoRepository.existsById(precoDto.id_preco())).thenReturn(false);

        // Act and Assert
        mockMvc.perform(put("/precos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id_preco\":5,\"modalidade\":\"HORA_CHEIA\",\"valor\":20.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveCadastrarComSucesso() throws Exception {
        var novoPreco = new PrecoDto(
                Tipo_Modalidade.HORA_CHEIA,
                10.0
        );

        ObjectMapper objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString("/precos");

        mockMvc.perform(MockMvcRequestBuilders.post("/precos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id_preco\":5,\"modalidade\":\"HORA_CHEIA\",\"valor\":10.0}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.modalidade").value("HORA_CHEIA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.valor").value(10.0));
    }

    @Test
    void deveRetornarListaDePrecos() throws Exception {
        // Arrange: Crie uma lista de preços de teste
        List<PrecoEntity> precos = new ArrayList<>();
        precos.add(new PrecoEntity(1L,20D,Tipo_Modalidade.HORA_CHEIA));
        precos.add(new PrecoEntity(2L,30D,Tipo_Modalidade.HORA_CHEIA));

        // Configure o mock do repositório para retornar a lista de preços
        Page<PrecoEntity> page = new PageImpl<>(precos, PageRequest.of(0, 10), precos.size());
        when(precoRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act & Assert: Execute a requisição GET e verifique o status da resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/precos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveAtualizarPrecoComSucesso() throws Exception {
        // Arrange: Crie um objeto PrecoEntity de teste
        PrecoEntity preco = new PrecoEntity(1L,20D,Tipo_Modalidade.HORA_CHEIA);

        // Configure o mock do repositório para retornar o preço
        when(precoRepository.existsById(1L)).thenReturn(true);
        when(precoRepository.getReferenceById(1L)).thenReturn(preco);

        // Crie um objeto DadosAtualizacaoPrecoDto com os dados de atualização
        DadosAtualizacaoPrecoDto dadosAtualizacao = new DadosAtualizacaoPrecoDto(1L, Tipo_Modalidade.HORA_CHEIA, 10.0);

        ObjectMapper objectMapper = new ObjectMapper();

        // Act & Assert: Execute a requisição PUT e verifique o status da resposta
        mockMvc.perform(MockMvcRequestBuilders.put("/precos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizacao)))
                .andExpect(status().isOk());
    }


}
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
import postech.fiap.com.br.parquimetro_api.controller.VeiculoController;
import postech.fiap.com.br.parquimetro_api.domain.preco.*;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VeiculoController.class)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VeiculoRepository veiculoRepository;


    @Test
    void cadastrar() throws Exception {
        var novoVeiculo = new VeiculoDto("UNO","FIAT","EIA3092",Tipo_Veiculo.AUTOMOVEL );

        ObjectMapper objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(novoVeiculo);

        mockMvc.perform(post("/veiculo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placa\":\"EIA3092\",\"marca\":\"FIAT\",\"modelo\":\"UNO\",\"tipo_veiculo\":\"AUTOMOVEL\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.modelo").value("UNO"))
                .andExpect(jsonPath("$.marca").value("FIAT"))
                .andExpect(jsonPath("$.placa").value("EIA3092"))
                .andExpect(jsonPath("$.tipo_veiculo").value("AUTOMOVEL"));
    }
    @Test
        void altera_comIdInexistente_retorna404() throws Exception {
            // Arrange
            var dadosAtualizacaoDto = new DadosAtualizacaoVeiculosDto(1L, "UNO", "FIAT", "EIA3092", Tipo_Veiculo.AUTOMOVEL);

            when(veiculoRepository.existsById(dadosAtualizacaoDto.id_veiculo())).thenReturn(false);

            // Act and Assert
        mockMvc.perform(put("/veiculo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id_veiculo\":1,\"placa\":\"ABC4567\",\"marca\":\"FIAT\",\"modelo\":\"UNO\",\"ano\":2024}"))
                .andExpect(status().isNotFound());
        }
    }

package postech.fiap.com.br.parquimetro_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;
import postech.fiap.com.br.parquimetro_api.controller.PrecoController;
import postech.fiap.com.br.parquimetro_api.controller.VeiculoController;
import postech.fiap.com.br.parquimetro_api.domain.preco.*;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import java.util.Arrays;

//@WebMvcTest(VeiculoController.class)

@WebMvcTest(VeiculoController.class)
class VeiculoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private VeiculoRepository veiculoRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void deveCadastrarVeiculoComSucesso() throws Exception {
            // 1. Crie um VeiculoDto de teste
            VeiculoDto veiculoDto = new VeiculoDto("UNO", "FIAT", "EIA3092", Tipo_Veiculo.AUTOMOVEL);

            // 2. Crie um VeiculoEntity de teste
            VeiculoEntity veiculoEntity = new VeiculoEntity(veiculoDto);

            // 3. Configure o mock do veiculoRepository para retornar o veiculoEntity
            when(veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculoEntity);

            // 4. Converta o veiculoEntity para DadosDetalhamentoVeiculoDto
            DadosDetalhamentoVeiculoDto dadosDetalhamentoVeiculoDto = new DadosDetalhamentoVeiculoDto(veiculoEntity);

            // 5. Execute a requisição POST
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/veiculos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(veiculoDto)))
                    .andExpect(status().isCreated())
                    .andDo(MockMvcResultHandlers.print());

            // 6. Verifique se o veiculoRepository.save() foi chamado
            verify(veiculoRepository, times(1)).save(any(VeiculoEntity.class));
        }

    @Test
    void deveAtualizarVeiculoComSucesso() throws Exception {
        // 1. Crie um DadosAtualizacaoVeiculosDto de teste
        DadosAtualizacaoVeiculosDto dadosAtualizacaoVeiculosDto = new DadosAtualizacaoVeiculosDto(1L, "Gol", "VW", "ABC1234", Tipo_Veiculo.AUTOMOVEL);

        // 2. Crie um VeiculoEntity de teste
        VeiculoEntity veiculoEntity = new VeiculoEntity(1L, "Gol", "VW", "ABC1234", Tipo_Veiculo.AUTOMOVEL);

        // 3. Configure o mock do veiculoRepository para retornar o veiculoEntity
        when(veiculoRepository.existsById(1L)).thenReturn(true);
        when(veiculoRepository.getReferenceById(1L)).thenReturn(veiculoEntity);

        // 4. Converta o veiculoEntity para DadosDetalhamentoVeiculoDto
        DadosDetalhamentoVeiculoDto dadosDetalhamentoVeiculoDto = new DadosDetalhamentoVeiculoDto(veiculoEntity);

        // 5. Execute a requisição PUT
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizacaoVeiculosDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // 6. Verifique se o veiculoRepository.existsById() e veiculoRepository.getReferenceById() foram chamados
        verify(veiculoRepository, times(1)).existsById(1L);
        verify(veiculoRepository, times(1)).getReferenceById(1L);
    }

    @Test
    void deveListarVeiculosComSucesso() throws Exception {
        // 1. Crie uma lista de VeiculoEntity de teste
        VeiculoEntity veiculo1 = new VeiculoEntity(1L, "Gol", "VW", "ABC1234", Tipo_Veiculo.AUTOMOVEL);
        VeiculoEntity veiculo2 = new VeiculoEntity(2L, "Uno", "Fiat", "DEF5678", Tipo_Veiculo.AUTOMOVEL);
        List<VeiculoEntity> veiculos = Arrays.asList(veiculo1, veiculo2);

        // 2. Crie um PageImpl com os veiculos de teste
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("marca"));
        PageImpl<VeiculoEntity> page = new PageImpl<>(veiculos, pageRequest, veiculos.size());

        // 3. Configure o mock do veiculoRepository para retornar a página de veiculos
        when(veiculoRepository.findAll(any(Pageable.class))).thenReturn(page);

        // 4. Execute a requisição GET
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].placa").value("ABC1234"))
                .andExpect(jsonPath("$.content[1].placa").value("DEF5678"))
                .andDo(MockMvcResultHandlers.print());

        // 5. Verifique se o veiculoRepository.findAll() foi chamado
        verify(veiculoRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void deveDetalharVeiculoComSucesso() throws Exception {
        // 1. Crie um VeiculoEntity de teste
        Long id = 1L;
        VeiculoEntity veiculoEntity = new VeiculoEntity(id, "Gol", "VW", "ABC1234", Tipo_Veiculo.AUTOMOVEL);

        // 2. Configure o mock do veiculoRepository para retornar o veiculoEntity
        when(veiculoRepository.existsById(id)).thenReturn(true);
        when(veiculoRepository.getReferenceById(id)).thenReturn(veiculoEntity);

        // 3. Converta o veiculoEntity para DadosDetalhamentoVeiculoDto
        DadosDetalhamentoVeiculoDto dadosDetalhamentoVeiculoDto = new DadosDetalhamentoVeiculoDto(veiculoEntity);

        // 4. Execute a requisição GET
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/veiculos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // 5. Verifique se o veiculoRepository.existsById() e veiculoRepository.getReferenceById() foram chamados
        verify(veiculoRepository, times(1)).existsById(id);
        verify(veiculoRepository, times(1)).getReferenceById(id);
    }

}







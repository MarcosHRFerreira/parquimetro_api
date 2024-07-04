package postech.fiap.com.br.parquimetro_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import postech.fiap.com.br.parquimetro_api.controller.LocacaoController;
import postech.fiap.com.br.parquimetro_api.domain.condutor.CondutorEntity;
import postech.fiap.com.br.parquimetro_api.domain.locacao.*;
import org.springframework.web.util.UriComponentsBuilder;
import postech.fiap.com.br.parquimetro_api.domain.preco.PrecoEntity;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.Tipo_Veiculo;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.VeiculoEntity;
import postech.fiap.com.br.parquimetro_api.exception.NaoEncontradoException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@WebMvcTest(LocacaoController.class)
class LocacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocacaoService locacaoService;

    @MockBean
    private LocacaoRepository locacaoRepository;

    @MockBean
    private LocacaoEntity locacaoEntity;

    @MockBean
    private CondutorEntity condutorEntity;

    @MockBean
    private VeiculoEntity veiculoEntity;

    @MockBean
    private PrecoEntity precoEntity;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveGravarLocacaoComSucesso() throws Exception {
        // 1. Crie um LocacaoDto de teste

        var dataEntrada = LocalDateTime.now();
        var dataEncerramento = LocalDateTime.now();

        LocacaoDto locacaoDto = new LocacaoDto(dataEntrada, null, 5L, 0D, Tipo_Pagamento.CARTAO_CREDITO, Status_Pagamento.PENDENTE, Tipo_Periodo.FIXO, 1L, 1L, 1L);

        //DadosAtualizacaoLocacao dadosAtualizacaoLocacao = new DadosAtualizacaoLocacao(1L,dataEntrada,null,5L,0D, Tipo_Pagamento.CARTAO_CREDITO,Status_Pagamento.PENDENTE, Tipo_Periodo.FIXO, 1L,1L,1L);
        //  DadosAtualizacaoLocacao dadosAtualizacaoLocacao = new DadosAtualizacaoLocacao();

        LocacaoEntity locacao = new LocacaoEntity(1l, dataEntrada, null, 5L, Tipo_Periodo.FIXO, 0D, Tipo_Pagamento.CARTAO_CREDITO, Status_Pagamento.PENDENTE, condutorEntity, veiculoEntity, precoEntity);

        // 2. Crie um objeto de retorno para o locacaoService.gravar()
        Object retornoGravar = locacao;

        // 3. Configure o mock do locacaoService para retornar o objeto de retorno
        when(locacaoService.gravar(locacaoDto)).thenReturn(locacao);

        // 4. Execute a requisição POST
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locacaoDto)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        // 5. Verifique se o locacaoService.gravar() foi chamado
        verify(locacaoService, times(1)).gravar(locacaoDto);
    }

    @Test
    void deveAlterarLocacaoComSucesso() throws Exception {
        // 1. Crie um LocacaoDto de teste

        var dataEntrada = LocalDateTime.now();
        var dataEncerramento = LocalDateTime.now();

        //LocacaoDto locacaoDto = new LocacaoDto(dataEntrada, null, 5L, 0D, Tipo_Pagamento.CARTAO_CREDITO, Status_Pagamento.PENDENTE, Tipo_Periodo.FIXO, 1L, 1L, 1L);

        DadosAtualizacaoLocacao dadosAtualizacaoLocacao = new DadosAtualizacaoLocacao(2L,dataEntrada,null,5L,0D, Tipo_Pagamento.CARTAO_CREDITO,Status_Pagamento.PENDENTE, Tipo_Periodo.FIXO, 1L,1L,1L);


        LocacaoEntity locacao = new LocacaoEntity(2l, dataEntrada, null, 5L, Tipo_Periodo.FIXO, 0D, Tipo_Pagamento.CARTAO_CREDITO, Status_Pagamento.PENDENTE, condutorEntity, veiculoEntity, precoEntity);

        // 2. Crie um objeto de retorno para o locacaoService.gravar()
        Object retornoGravar = locacao;

        // 3. Configure o mock do locacaoService para retornar o objeto de retorno
        when(locacaoService.alterar(dadosAtualizacaoLocacao)).thenReturn(locacao);

        // 4. Execute a requisição Put
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizacaoLocacao)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(locacao)));

        // 5. Verifique se o locacaoService.alterar() foi chamado
        verify(locacaoService, times(1)).alterar(dadosAtualizacaoLocacao);
    }

    @Test
    void deveEncerrarLocacao() throws Exception {
        // Dados de teste
        var dataEntrada = LocalDateTime.now();
        DadosAtualizacaoLocacao dadosAtualizacaoLocacao = new DadosAtualizacaoLocacao(2L,dataEntrada,null,5L,0D, Tipo_Pagamento.CARTAO_CREDITO,Status_Pagamento.PENDENTE, Tipo_Periodo.FIXO, 1L,1L,1L);
        // ... preencha os dados do objeto

        LocacaoEntity locacao = new LocacaoEntity(2l, dataEntrada, null, 5L, Tipo_Periodo.FIXO, 0D, Tipo_Pagamento.CARTAO_CREDITO, Status_Pagamento.PENDENTE, condutorEntity, veiculoEntity, precoEntity);
        // ... preencha os dados da locação encerrada

        // Simula o comportamento do LocacaoService
        when(locacaoService.encerraLocacao(dadosAtualizacaoLocacao)).thenReturn(locacao);

        // Executa a requisição PUT
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/locacoes/encerrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizacaoLocacao)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(locacao)));
    }
    @Test
    void deveListarLocacoes() throws Exception {
        //1. Dados de teste

        List<LocacaoEntity> locacoes = Arrays.asList(
                new LocacaoEntity(2l, LocalDateTime.now(), null, 5L, Tipo_Periodo.FIXO, 0D, Tipo_Pagamento.CARTAO_CREDITO, Status_Pagamento.PENDENTE, condutorEntity, veiculoEntity, precoEntity),
                new LocacaoEntity(3l, LocalDateTime.now(), null, 5L, Tipo_Periodo.FIXO, 0D, Tipo_Pagamento.CARTAO_CREDITO, Status_Pagamento.PENDENTE, condutorEntity, veiculoEntity, precoEntity)
        );

        // 2. Crie um PageImpl com os veiculos de teste
        Pageable paginacao = PageRequest.of(0, 10);
        Page<LocacaoEntity> page = new PageImpl<>(locacoes, paginacao, locacoes.size());


        // 3. Configure o mock do veiculoRepository para retornar a página de veiculos
        when(locacaoRepository.findAll(any(Pageable.class))).thenReturn(page);

        // 4. Execute a requisição GET
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/locacoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(locacoes.size()));

        // 5. Verifique se o locacaoRepository.findAll() foi chamado
        verify(locacaoRepository, times(1)).findAll(any(Pageable.class));
    }


}
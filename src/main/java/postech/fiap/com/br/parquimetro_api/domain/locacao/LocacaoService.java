package postech.fiap.com.br.parquimetro_api.domain.locacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postech.fiap.com.br.parquimetro_api.domain.condutor.CondutorEntity;
import postech.fiap.com.br.parquimetro_api.domain.condutor.CondutorRepository;
import postech.fiap.com.br.parquimetro_api.domain.preco.PrecoEntity;
import postech.fiap.com.br.parquimetro_api.domain.preco.PrecoRepository;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.VeiculoEntity;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.VeiculoRepository;
import postech.fiap.com.br.parquimetro_api.exception.CondicaoIncorretaException;
import postech.fiap.com.br.parquimetro_api.exception.DatasException;
import postech.fiap.com.br.parquimetro_api.exception.NaoEncontradoException;
import postech.fiap.com.br.parquimetro_api.infra.EmailService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class LocacaoService {

    private final LocacaoRepository locacaoRepository;
    private final CondutorRepository condutorRepository;
    private final VeiculoRepository veiculoRepository;
    private final PrecoRepository precoRepository;


    @Autowired
    private EmailService emailService;


    public LocacaoService(LocacaoRepository locacaoRepository, CondutorRepository condutorRepository,
                          VeiculoRepository veiculoRepository, PrecoRepository precoRepository) {
        this.locacaoRepository = locacaoRepository;
        this.condutorRepository = condutorRepository;
        this.veiculoRepository = veiculoRepository;
        this.precoRepository = precoRepository;
    }

    @Transactional
    //public LocacaoEntity gravar(LocacaoDto locacaoDto, DadosAtualizacaoLocacao dadosAtualizacaoLocacao) {
    public LocacaoEntity gravar(LocacaoDto locacaoDto) {

        try {

            CondutorEntity condutor = getCondutor(locacaoDto.id_condutor());
            VeiculoEntity veiculo = getVeiculo(locacaoDto.id_veiculo());

            PrecoEntity preco = getPreco(getIdPreco(locacaoDto.tipo_periodo()));

            LocacaoEntity locacao = new LocacaoEntity();
            locacao.setData_entrada(locacaoDto.data_entrada());
            locacao.setDuracao(locacaoDto.duaracao());

            if (locacaoDto.tipo_periodo() == Tipo_Periodo.FIXO) {
                locacao.setDuracao(12L);
                locacao.setValor_cobrado(preco.getValor());
            }
            if ((locacaoDto.tipo_periodo() == Tipo_Periodo.HORA) && (locacaoDto.duaracao() == 0)) {
                locacao.setDuracao(1L);
                locacao.setValor_cobrado(preco.getValor() * 1L);
            }
            if (isDataEntradaValida(locacaoDto.data_entrada().toLocalDate())) {
                throw new DatasException("Verificar a Data_Entrada, ela não pode ser maior ou menor que a data atual");
            }
            if ((locacaoDto.tipo_pagamento() == Tipo_Pagamento.PIX) && (locacaoDto.tipo_periodo() != Tipo_Periodo.FIXO)) {
                throw new CondicaoIncorretaException("Pix somente para período fixo");
            }
            if ((locacaoDto.tipo_periodo() == Tipo_Periodo.FIXO) && (locacaoDto.duaracao() == 0)) {
                throw new CondicaoIncorretaException("Para período fixo informar a duração");
            }

            locacao.setTipo_periodo(locacaoDto.tipo_periodo());
            locacao.setTipo_pagamento(locacaoDto.tipo_pagamento());
            locacao.setStatus_pagamento((locacaoDto.status_pagamento()));
            locacao.setCondutorEntity(condutor);
            locacao.setVeiculoEntity(veiculo);
            locacao.setPrecoEntity(preco);
            locacao.setData_encerramento(null);

            return locacaoRepository.save(locacao);

        } catch (DatasException e) {
            // Tratar a exceção de data inválida
            throw new DatasException("Data de entrada inválida. Verifique a data e tente novamente.");
        } catch (CondicaoIncorretaException e) {
            // Tratar a exceção de condição incorreta
            throw new CondicaoIncorretaException(e.getMessage());
        } catch (Exception e) {
            // Tratar outras exceções que podem ocorrer durante a gravação
            throw new RuntimeException("Erro ao gravar locação.", e);
        }

    }

    @Transactional
    public LocacaoEntity alterar(DadosAtualizacaoLocacao dadosAtualizacaoLocacao) {

        try {

            LocacaoEntity locacao = locacaoRepository.findById(dadosAtualizacaoLocacao.id_locacao())
                    .orElseThrow(() -> new NaoEncontradoException("Locação não encontrada"));

            CondutorEntity condutor = getCondutor(dadosAtualizacaoLocacao.id_condutor());
            VeiculoEntity veiculo = getVeiculo(dadosAtualizacaoLocacao.id_veiculo());


            PrecoEntity preco = getPreco(getIdPreco(dadosAtualizacaoLocacao.tipo_periodo()));

            if (dadosAtualizacaoLocacao.tipo_periodo() == Tipo_Periodo.HORA) {
                locacao.setValor_cobrado(preco.getValor() * locacao.getDuracao());
                locacao.setDuracao(dadosAtualizacaoLocacao.duaracao());
            }

            if (dadosAtualizacaoLocacao.tipo_periodo() == Tipo_Periodo.FIXO) {
                locacao.setValor_cobrado(preco.getValor());
                locacao.setDuracao(12L);
            }
            locacao.setTipo_periodo(dadosAtualizacaoLocacao.tipo_periodo());

            locacao.setData_encerramento(null);
            locacao.setId_locacao(dadosAtualizacaoLocacao.id_locacao());
            locacao.setData_entrada(dadosAtualizacaoLocacao.data_entrada());
            locacao.setTipo_pagamento(dadosAtualizacaoLocacao.tipo_pagamento());
            locacao.setStatus_pagamento((dadosAtualizacaoLocacao.status_pagamento()));
            locacao.setCondutorEntity(condutor);
            locacao.setVeiculoEntity(veiculo);
            locacao.setPrecoEntity(preco);

            return locacaoRepository.save(locacao);
        }catch (NaoEncontradoException e){
            // Tratar a exceção de locação não encontrada
            throw new NaoEncontradoException("Locação não encontrada para atualização.");
        } catch (Exception e) {
            // Tratar outras exceções que podem ocorrer durante a atualização
            throw new RuntimeException("Erro ao atualizar locação.", e);
        }

    }

    private PrecoEntity getPreco(Long id) {
        return precoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Id do Preco informado não existe!"));
    }

    private CondutorEntity getCondutor(Long id) {
        return condutorRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Id do Condutor informado não existe!"));
    }

    private VeiculoEntity getVeiculo(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Id do veiculo informado não existe!"));
    }

    private boolean isDataEntradaValida(LocalDate dataEntrada) {
        return dataEntrada == null || dataEntrada.isAfter(LocalDate.now()) || dataEntrada.isBefore(LocalDate.now());
    }

    private boolean isDataSaidaValida(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        return dataSaida == null || dataSaida.isBefore(dataEntrada);
    }

    @Transactional
    public LocacaoEntity encerraLocacao(DadosAtualizacaoLocacao dadosAtualizacaoLocacao) {
        LocacaoEntity locacao = locacaoRepository.findById(dadosAtualizacaoLocacao.id_locacao())
                .orElseThrow(() -> new NaoEncontradoException("Locação não encontrada"));

        locacao.setData_encerramento(dadosAtualizacaoLocacao.data_encerramento());
        locacao.setStatus_pagamento(dadosAtualizacaoLocacao.status_pagamento());

        locacaoRepository.save(locacao);

        try {
            emailService.sendEmail(locacao.getCondutorEntity().getEmail(), "Recibo de Pagamento", "Recibo de pagamento de estacionamento para veiculo : " +
                    locacao.getVeiculoEntity().getMarca() + " - " + locacao.getVeiculoEntity().getModelo() + "- Placa " +
                    locacao.getVeiculoEntity().getPlaca() + " - no Valor : R$ " + locacao.getValor_cobrado());

        } catch (Exception e) {
            // Trate a exceção de envio de email
            throw new RuntimeException("Erro ao enviar o email de recibo de pagamento", e);
        }

        return locacao;

    }

    public Long getIdPreco(Tipo_Periodo tipoPeriodo) {
        if (tipoPeriodo == Tipo_Periodo.FIXO) {
            return 2L;
        } else if (tipoPeriodo == Tipo_Periodo.HORA) {
            return 1L;
        } else {
            // Tratar caso o tipo de período seja inválido
            throw new IllegalArgumentException("Tipo de período inválido.");
        }
    }


}

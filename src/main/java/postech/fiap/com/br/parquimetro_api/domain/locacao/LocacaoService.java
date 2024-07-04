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

        CondutorEntity condutor = getCondutor(locacaoDto.id_condutor());
        VeiculoEntity veiculo = getVeiculo(locacaoDto.id_veiculo());
        PrecoEntity preco = getPreco(locacaoDto.id_preco());

        LocacaoEntity locacao = new LocacaoEntity();
        locacao.setData_entrada(locacaoDto.data_entrada());
        locacao.setDuracao(locacaoDto.duaracao());

        if (locacaoDto.tipo_periodo() == Tipo_Periodo.FIXO) {
            locacao.setValor_cobrado(preco.getValor() * locacao.getDuracao());
        }

        if ((locacaoDto.tipo_periodo() == Tipo_Periodo.HORA) && (locacaoDto.duaracao() == 0)) {
            locacao.setDuracao(Long.valueOf(1));
            locacao.setValor_cobrado(preco.getValor() * Long.valueOf(1));
        } else {
            locacao.setValor_cobrado(preco.getValor() * locacaoDto.duaracao());
        }
        locacao.setTipo_periodo(locacaoDto.tipo_periodo());
        locacao.setTipo_pagamento(locacaoDto.tipo_pagamento());
        locacao.setStatus_pagamento((locacaoDto.status_pagamento()));
        locacao.setCondutorEntity(condutor);
        locacao.setVeiculoEntity(veiculo);
        locacao.setPrecoEntity(preco);

        if (isDataEntradaValida(locacaoDto.data_entrada().toLocalDate())) {
            throw new DatasException("Verificar a Data_Entrada, ela não pode ser maior ou menor que a data atual");
        }
            if(isDataSaidaValida(locacaoDto.data_entrada(), locacaoDto.data_encerramento()))
            {
                throw new DatasException("Verificar a Data_Encerramento, não pode ser menor que a data atual");
            }

        if ((locacaoDto.tipo_pagamento() == Tipo_Pagamento.PIX) && (locacaoDto.tipo_periodo() != Tipo_Periodo.FIXO)) {
            throw new CondicaoIncorretaException("Pix somente para período fixo");
        }
        if ((locacaoDto.tipo_periodo() == Tipo_Periodo.FIXO) && (locacaoDto.duaracao() == 0)) {
            throw new CondicaoIncorretaException("Para período fixo informar a duração");
        }
        return locacaoRepository.save(locacao);
    }

    @Transactional
    public LocacaoEntity alterar(DadosAtualizacaoLocacao dadosAtualizacaoLocacao) {

        LocacaoEntity locacao = locacaoRepository.findById(dadosAtualizacaoLocacao.id_locacao())
                .orElseThrow(() -> new NaoEncontradoException("Locação não encontrada"));

        CondutorEntity condutor = getCondutor(dadosAtualizacaoLocacao.id_condutor());
        VeiculoEntity veiculo = getVeiculo(dadosAtualizacaoLocacao.id_veiculo());
        PrecoEntity preco = getPreco(dadosAtualizacaoLocacao.id_preco());

        if (dadosAtualizacaoLocacao.tipo_periodo() == Tipo_Periodo.FIXO) {
            locacao.setValor_cobrado(preco.getValor() * locacao.getDuracao());
        } else {
            locacao.setValor_cobrado(Double.valueOf(0));
        }
        locacao.setTipo_periodo(dadosAtualizacaoLocacao.tipo_periodo());

        if (dadosAtualizacaoLocacao.tipo_periodo() == Tipo_Periodo.FIXO) {
            locacao.setData_encerramento(dadosAtualizacaoLocacao.data_entrada().plusHours(locacao.getDuracao()));
        } else {
            locacao.setData_encerramento(dadosAtualizacaoLocacao.data_encerramento());
        }

        locacao.setId_locacao(dadosAtualizacaoLocacao.id_locacao());
        locacao.setData_entrada(dadosAtualizacaoLocacao.data_entrada());
        locacao.setDuracao(dadosAtualizacaoLocacao.duaracao());
        locacao.setTipo_pagamento(dadosAtualizacaoLocacao.tipo_pagamento());
        locacao.setStatus_pagamento((dadosAtualizacaoLocacao.status_pagamento()));
        locacao.setCondutorEntity(condutor);
        locacao.setVeiculoEntity(veiculo);
        locacao.setPrecoEntity(preco);

        return locacaoRepository.save(locacao);
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
                    locacao.getVeiculoEntity().getPlaca() + " - no Valor : R$ " + locacao.getValor_cobrado() );

        } catch (Exception e) {
            // Trate a exceção de envio de email
            throw new RuntimeException("Erro ao enviar o email de recibo de pagamento", e);
        }

        return locacao;

    }



}

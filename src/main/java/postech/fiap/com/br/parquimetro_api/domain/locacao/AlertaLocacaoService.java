package postech.fiap.com.br.parquimetro_api.domain.locacao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import postech.fiap.com.br.parquimetro_api.infra.EmailService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Configuration
public class AlertaLocacaoService {

     String subtitulo ="Alerta de Locação";
     String mensagem1= "O tempo de locação está vencendo, para o veiculo  - ";;
     String mensagem2= "O tempo estenderá automaticamente o estacionamento\n" +
             "por mais uma hora, a menos que o condutor desligue o registro. - ";
     String mensagem;
     String descricao_veiculo;

    @Autowired
    private  LocacaoRepository locacaoRepository;

    @Autowired
    private EmailService emailService;


   // @Scheduled(fixedRateString = "${alerta.locacao.fixedRate}")

    Logger logger = LoggerFactory.getLogger(AlertaLocacaoService.class);

     @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)

    public void verificarTempoEstacionamento() {

        try {
            var dateTime=LocalDateTime.now();
            logger.info("Verificando se existe periodo a vencer {}", dateTime);

            List<LocacaoEntity> locacoes = locacaoRepository.findAllAbertas();
            for (LocacaoEntity locacao : locacoes) {
                LocalDateTime dataEntrada = locacao.getData_entrada();
                Long duracao = locacao.getDuracao();
                Tipo_Periodo tipoPeriodo = locacao.getTipo_periodo();

                System.out.println("id-locacao" + locacao.getId_locacao());

                // Calcular a data de saída
                LocalDateTime dataSaida = dataEntrada.plusHours(duracao);

                // Verificar se o tempo restante é menor ou igual a 5 minutos
                long tempoRestante = ChronoUnit.SECONDS.between(LocalDateTime.now(), dataSaida);

                descricao_veiculo=locacao.getVeiculoEntity().getMarca() + " - " + locacao.getVeiculoEntity().getModelo() + "- Placa " + locacao.getVeiculoEntity().getPlaca() ;

                // Verificar se o tempo já expirou
                if (tempoRestante <= 0) {
                    // O tempo já expirou, não enviar mais alertas
                    continue;
                }
                //verificando se o tempo restante da locação é menor ou igual a 300 segundos, ou seja, 5 minutos.
                if (tempoRestante <= 300) {

                    if (Tipo_Periodo.HORA.equals(locacao.getTipo_periodo())) {

                        locacao.setDuracao(locacao.getDuracao() + 1);

                        locacao.setValor_cobrado(locacao.getPrecoEntity().getValor() * locacao.getDuracao());

                        locacaoRepository.save(locacao);

                        mensagem =  mensagem2  + descricao_veiculo;
                        enviarAlerta(locacao.getCondutorEntity().getEmail(), subtitulo, mensagem );
                    }else {
                        mensagem =  mensagem1  + descricao_veiculo;
                        enviarAlerta(locacao.getCondutorEntity().getEmail(), subtitulo, mensagem );
                    }
                }
            }
        }catch ( Exception e){
            throw new RuntimeException("Erro ao verificar o tempo de estacionamento.", e);
        }
    }

    public void enviarAlerta(String emailDestinatario,String subtitulo, String mensagem) {
        emailService.sendEmail(emailDestinatario, subtitulo,mensagem );
    }
}


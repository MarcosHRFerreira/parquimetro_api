package postech.fiap.com.br.parquimetro_api.domain.locacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

     String subtitulo ="Renovação do período da vaga do estacionamento";
     String mensagem1= "O tempo foi extendido automaticamente \n" +
             "por mais uma hora, a menos que o condutor desligue o registro. - ";
     String mensagem2 = "Faltam 15 minutos para vencer o prazo do estacionamento ";
     String descricao_veiculo;

    @Autowired
    private  LocacaoRepository locacaoRepository;

    @Autowired
    private EmailService emailService;

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
                //Verifica 15 minutos antes do vencimento, dispara o email.
                if (tempoRestante <= 900 && locacao.getAviso15minutos()==true) {
                    enviarAlerta(locacao.getCondutorEntity().getEmail(), "Alerta de Vencimento do Estacionamento", mensagem2 + " " + descricao_veiculo);
                    locacao.setAviso15minutos(false);
                    locacaoRepository.save(locacao);
                }
                //verificando se o tempo restante da locação é menor ou igual a 60 segundos, ou seja, 1 minuto.
                if (tempoRestante <= 60) {
                    // Se o periodo for HORA, acrescenta mais uma hora na duração e ativo o aviso previo de 15 minutos para o vencimento.
                    if (Tipo_Periodo.HORA.equals(locacao.getTipo_periodo())) {
                        locacao.setDuracao(locacao.getDuracao() + 1);
                        locacao.setValor_cobrado(locacao.getPrecoEntity().getValor() * locacao.getDuracao());
                        locacao.setAviso15minutos(true);

                        locacaoRepository.save(locacao);

                        // Dispara o email para o aviso de renovação
                        enviarAlerta(locacao.getCondutorEntity().getEmail(), subtitulo, mensagem1 + " " + descricao_veiculo);
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


package postech.fiap.com.br.parquimetro_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ParquimetroApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParquimetroApiApplication.class, args);
	}

}

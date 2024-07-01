package postech.fiap.com.br.parquimetro_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AlertaLocacaoExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // Registrar o erro em um log
        System.err.println("Erro : " + ex.getMessage());

        // Retornar uma resposta de erro genérica
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro : " + ex.getMessage());
    }
}

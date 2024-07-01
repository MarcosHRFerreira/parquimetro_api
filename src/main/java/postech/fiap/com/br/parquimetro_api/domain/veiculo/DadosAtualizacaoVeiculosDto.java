package postech.fiap.com.br.parquimetro_api.domain.veiculo;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoVeiculosDto(
        @NotNull
        Long id_veiculo,
        String modelo,
        String marca,
        String placa,
        Tipo_Veiculo tipo_veiculo) {
}

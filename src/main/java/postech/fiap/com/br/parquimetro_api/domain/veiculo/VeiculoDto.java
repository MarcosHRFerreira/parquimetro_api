package postech.fiap.com.br.parquimetro_api.domain.veiculo;

public record VeiculoDto(
        String modelo,
        String marca,
        String placa,
        Tipo_Veiculo tipo_veiculo) {
}

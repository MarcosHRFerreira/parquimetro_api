package postech.fiap.com.br.parquimetro_api.domain.veiculo;

public record DadosDetalhamentoVeiculoDto(
        Long id_veiculo,
        String modelo,
        String marca,
        String placa,
        Tipo_Veiculo tipo_veiculo) {

    public DadosDetalhamentoVeiculoDto(VeiculoEntity veiculo){
        this(veiculo.getId_veiculo(),
                veiculo.getModelo(),
                veiculo.getMarca(),
                veiculo.getPlaca(),
                veiculo.getTipo_veiculo());
    }

}

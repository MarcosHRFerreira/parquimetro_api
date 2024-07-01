package postech.fiap.com.br.parquimetro_api.domain.veiculo;

import jakarta.validation.constraints.NotNull;

public record DadosListagemVeiculoDto(
        @NotNull
        Long id_veiculo,
        String modelo,
        String marca,
        String placa,
        Tipo_Veiculo tipo_veiculo) {

    public DadosListagemVeiculoDto(VeiculoEntity veiculoEntity){
           this(veiculoEntity.getId_veiculo(),
                   veiculoEntity.getModelo(),
                   veiculoEntity.getMarca(),
                   veiculoEntity.getPlaca(),
                   veiculoEntity.getTipo_veiculo());
    }

}

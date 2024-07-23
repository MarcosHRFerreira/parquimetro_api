package postech.fiap.com.br.parquimetro_api.domain.veiculo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name="VeiculoEntity")
@Table(name="veiculo")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class VeiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_veiculo;

    @NotEmpty
    @NotNull
    private String modelo;

    @NotEmpty
    @NotNull
    private String marca;

    @NotEmpty
    @NotNull
    private String placa;

    @Enumerated(EnumType.STRING)
    private Tipo_Veiculo tipo_veiculo;

    public VeiculoEntity(){
    }
    public VeiculoEntity(VeiculoDto dados){

        if (dados.modelo() != null) {
            this.modelo = dados.modelo().toUpperCase();
        }
        if (dados.marca() != null) {
            this.marca = dados.marca().toUpperCase();
        }
        if (dados.placa() != null) {
            this.placa = dados.placa().toUpperCase();
        }
        this.tipo_veiculo=dados.tipo_veiculo();

    }

    public void atualizarInformacoes(DadosAtualizacaoVeiculosDto dados) {
        if (dados.modelo() != null) {
            this.modelo = dados.modelo().toUpperCase();
        }
        if (dados.marca() != null) {
            this.marca = dados.marca().toUpperCase();
        }
        if (dados.placa() != null) {
            this.placa = dados.placa().toUpperCase();
        }
        if (dados.tipo_veiculo() != null) {
            this.tipo_veiculo = dados.tipo_veiculo();
        }

    }

}

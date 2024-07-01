package postech.fiap.com.br.parquimetro_api.domain.veiculo;

import jakarta.persistence.*;
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
    private String modelo;
    private String marca;
    private String placa;

    @Enumerated(EnumType.STRING)
    private Tipo_Veiculo tipo_veiculo;

    public VeiculoEntity(){
    }

    public VeiculoEntity(VeiculoDto dados){
        this.modelo=dados.modelo();
        this.marca=dados.marca();
        this.placa=dados.placa();
        this.tipo_veiculo=dados.tipo_veiculo();
    }

    public void atualizarInformacoes(DadosAtualizacaoVeiculosDto dados) {
        if (dados.modelo() != null) {
            this.modelo = dados.modelo();
        }
        if (dados.marca() != null) {
            this.marca = dados.marca();
        }
        if (dados.placa() != null) {
            this.placa = dados.placa();
        }
        if (dados.tipo_veiculo() != null) {
            this.tipo_veiculo = dados.tipo_veiculo();
        }

    }

}

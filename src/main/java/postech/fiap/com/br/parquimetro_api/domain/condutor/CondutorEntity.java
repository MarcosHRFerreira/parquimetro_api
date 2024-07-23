package postech.fiap.com.br.parquimetro_api.domain.condutor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import postech.fiap.com.br.parquimetro_api.domain.endereco.Endereco;

@Table(name = "condutor")
@Entity(name = "CondutorEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CondutorEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_condutor;

    @NotEmpty
    @NotNull
    private String nome;

    @NotEmpty
    @NotNull
    private String email;

    @NotEmpty
    @NotNull
    private String telefone;

    private Long id_veiculo;

    @Embedded
    private Endereco endereco;

    public CondutorEntity(CondutorDto dados){

        if (dados.nome() != null) {
            this.nome = dados.nome().toUpperCase();
        }
        if (dados.email() != null) {
            this.email = dados.email().toLowerCase();
        }
        this.telefone=dados.telefone();
        this.id_veiculo=dados.id_veiculo();
        this.endereco=new Endereco(dados.endereco());
    }

    public void atualizarInformacoes(DadosAtualizacaoCondutor dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome().toUpperCase();
        }
        if (dados.email() != null) {
            this.email = dados.email().toLowerCase();
        }
        if (dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if (dados.id_veiculo() != null) {
            this.id_veiculo = dados.id_veiculo();
        }
        if (dados.endereco() != null) {
            this.endereco=(dados.endereco());
        }
    }
}

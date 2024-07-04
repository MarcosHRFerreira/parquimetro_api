package postech.fiap.com.br.parquimetro_api.domain.condutor;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    private String nome;

    @NotEmpty
    private String email;

    @NotEmpty
    private String telefone;

    private Long id_veiculo;

    @Embedded
    private Endereco endereco;

//    @ManyToOne
//    @JoinColumn(name = "id_veiculo", referencedColumnName = "id_veiculo")
//    private CondutorEntity condutorEntity;


    public CondutorEntity(CondutorDto dados){

        this.nome=dados.nome();
        this.email=dados.email();
        this.telefone=dados.telefone();
        this.id_veiculo=dados.id_veiculo();
        this.endereco=new Endereco(dados.endereco());
    }

    public void atualizarInformacoes(DadosAtualizacaoCondutor dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.email() != null) {
            this.email = dados.email();
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
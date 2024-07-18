package postech.fiap.com.br.parquimetro_api.domain.preco;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name="PrecoEntity")
@Table(name="preco")
@Getter
@Setter

@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class PrecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_preco;

    private Double valor;

    @Enumerated(EnumType.STRING)
    private Tipo_Modalidade modalidade;

    // Construtor vazio
    public PrecoEntity() {
    }

    public PrecoEntity(PrecoDto dados){
        this.modalidade= dados.modalidade();
        this.valor=dados.valor();
    }

    public void atualizarInformacoes(DadosAtualizacaoPrecoDto dados) {
        if(dados.modalidade()!=null){
            this.modalidade=dados.modalidade();
        }
        if(dados.valor()!= null){
            this.valor=dados.valor();
        }

    }


}

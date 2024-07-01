package postech.fiap.com.br.parquimetro_api.domain.preco;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import postech.fiap.com.br.parquimetro_api.domain.condutor.CondutorEntity;

public record DadosListagemPrecoDto(
        Long id_preco,
        Double valor,
        Tipo_Modalidade tipo_modalidade) {

    public DadosListagemPrecoDto(PrecoEntity precoEntity){
        this(precoEntity.getId_preco(),
                precoEntity.getValor(),
                precoEntity.getModalidade());
    }
}


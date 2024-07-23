package postech.fiap.com.br.parquimetro_api.domain.preco;

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


package postech.fiap.com.br.parquimetro_api.domain.preco;

public record DadosDetalhamentoPrecoDto(
            Long id_preco,
            Tipo_Modalidade modalidade,
            Double valor){

        public DadosDetalhamentoPrecoDto(PrecoEntity preco){
            this(preco.getId_preco(),
                    preco.getModalidade(),
                    preco.getValor() );
        }


}

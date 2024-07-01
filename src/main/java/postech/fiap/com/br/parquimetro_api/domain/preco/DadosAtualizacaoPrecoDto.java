package postech.fiap.com.br.parquimetro_api.domain.preco;

public record DadosAtualizacaoPrecoDto(
        Long id_preco,
        Tipo_Modalidade modalidade,
        Double valor
) {
}

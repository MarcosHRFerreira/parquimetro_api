package postech.fiap.com.br.parquimetro_api.domain.condutor;

import postech.fiap.com.br.parquimetro_api.domain.endereco.DadosEndereco;

public record CondutorDto(
        String nome,
        String email,
        String telefone,
        Long id_veiculo,
        DadosEndereco endereco) {
}

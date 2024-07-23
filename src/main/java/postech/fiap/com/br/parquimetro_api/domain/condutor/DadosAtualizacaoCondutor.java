package postech.fiap.com.br.parquimetro_api.domain.condutor;

import jakarta.validation.constraints.NotNull;
import postech.fiap.com.br.parquimetro_api.domain.endereco.Endereco;

public record DadosAtualizacaoCondutor(
        @NotNull
        Long id_condutor,
        String nome,
        String email,
        String telefone,
        Long id_veiculo,
        Endereco endereco){
}

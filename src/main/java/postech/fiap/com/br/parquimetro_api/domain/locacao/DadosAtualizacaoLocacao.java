package postech.fiap.com.br.parquimetro_api.domain.locacao;

import java.time.LocalDateTime;

public record DadosAtualizacaoLocacao(
        Long id_locacao,
        LocalDateTime data_entrada,
        LocalDateTime data_encerramento,
        Long duaracao,
        Double valor_cobrado,
        Tipo_Pagamento tipo_pagamento,
        Status_Pagamento status_pagamento,
        Tipo_Periodo tipo_periodo,
        Long id_condutor,
        Long id_veiculo,
        Long id_preco)
         {
}

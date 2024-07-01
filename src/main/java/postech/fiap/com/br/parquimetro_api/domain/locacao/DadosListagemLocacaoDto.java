package postech.fiap.com.br.parquimetro_api.domain.locacao;

import java.time.LocalDateTime;

public record DadosListagemLocacaoDto(
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
     public DadosListagemLocacaoDto(LocacaoEntity locacaoEntity){
         this(locacaoEntity.getId_locacao(),
                 locacaoEntity.getData_entrada(),
                 locacaoEntity.getData_encerramento(),
                 locacaoEntity.getDuracao(),
                 locacaoEntity.getValor_cobrado(),
                 locacaoEntity.getTipo_pagamento(),
                 locacaoEntity.getStatus_pagamento(),
                 locacaoEntity.getTipo_periodo(),
                 locacaoEntity.getCondutorEntity().getId_condutor(),
                 locacaoEntity.getVeiculoEntity().getId_veiculo(),
                 locacaoEntity.getPrecoEntity().getId_preco());
     }
}

package postech.fiap.com.br.parquimetro_api.domain.condutor;

import postech.fiap.com.br.parquimetro_api.domain.endereco.Endereco;

public record DadosDetalhamentoCondutorDto(
        Long id_condutor,
        String nome,
        String email,
        String telefone,
        Long id_veiculo,
        Endereco endereco){

    public DadosDetalhamentoCondutorDto(CondutorEntity estacionamento){
        this(estacionamento.getId_condutor(),
                estacionamento.getNome(),
                estacionamento.getEmail(),
                estacionamento.getTelefone(),
                estacionamento.getId_veiculo(),
                estacionamento.getEndereco()  );
    }
}

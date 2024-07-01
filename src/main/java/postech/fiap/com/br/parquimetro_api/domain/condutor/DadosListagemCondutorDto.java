package postech.fiap.com.br.parquimetro_api.domain.condutor;

public record DadosListagemCondutorDto(
        Long id_condutor,
        String nome,
        String email,
        String telefone,
        Long id_veiculo)
{
      public DadosListagemCondutorDto(CondutorEntity condutorEntity){
              this(condutorEntity.getId_condutor(),
                      condutorEntity.getNome(),
                      condutorEntity.getEmail(),
                      condutorEntity.getTelefone(),
                      condutorEntity.getId_veiculo());
      }
}

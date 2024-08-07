package postech.fiap.com.br.parquimetro_api.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import postech.fiap.com.br.parquimetro_api.ValidacaoException;
import postech.fiap.com.br.parquimetro_api.domain.condutor.*;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.VeiculoRepository;
import postech.fiap.com.br.parquimetro_api.exception.NaoEncontradoException;

@RestController
@RequestMapping("condutores")
public class CondutorController {

    @Autowired
    private CondutorRepository repository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@Valid @RequestBody CondutorDto dados, UriComponentsBuilder uriBuilder){

        try {
            var condutor = new CondutorEntity(dados);

            if (veiculoRepository.existsById(dados.id_veiculo())) {
                throw new ValidacaoException("Id do Veiculo se encontra cadastrado!");
            }

            repository.save(condutor);

            var uri = uriBuilder.path("/condutor/{id}").buildAndExpand(condutor.getId_condutor()).toUri();

            return ResponseEntity.created(uri).body(new DadosDetalhamentoCondutorDto(condutor));
        }
        catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity altera(@RequestBody @Valid DadosAtualizacaoCondutor dados){

        try {
            if (!repository.existsById(dados.id_condutor())) {
                throw new ValidacaoException("Id do Condutor informado não existe!");
            }
            var condutor=repository.getReferenceById(dados.id_condutor());
            condutor.atualizarInformacoes(dados);
            return ResponseEntity.ok(new DadosDetalhamentoCondutorDto(condutor));
        }catch (ValidacaoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemCondutorDto>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListagemCondutorDto::new);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {

        repository.findById(id).orElseThrow(() -> new NaoEncontradoException("Condutor não encontrado"));

        repository.deleteById(id);

        return ResponseEntity.noContent().build();


    }


}

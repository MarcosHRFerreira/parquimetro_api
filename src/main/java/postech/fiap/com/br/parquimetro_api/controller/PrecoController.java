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
import postech.fiap.com.br.parquimetro_api.domain.preco.*;

@RestController
@RequestMapping("preco")
public class PrecoController {

    @Autowired
    PrecoRepository precoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody PrecoDto dados , UriComponentsBuilder uriBuilder) {

        try {
            var preco = new PrecoEntity(dados);
            precoRepository.save(preco);

            var uri = uriBuilder.path("/preco/{id}").buildAndExpand(preco.getId_preco()).toUri();

            return ResponseEntity.created(uri).body(new DadosDetalhamentoPrecoDto(preco));
        }catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping
    public ResponseEntity<Page<DadosListagemPrecoDto>> listar(@PageableDefault(size =10 ) Pageable paginacao){
           var page = precoRepository.findAll(paginacao).map(DadosListagemPrecoDto::new);
           return ResponseEntity.ok(page);

    }
    @PutMapping
    @Transactional
    public ResponseEntity altera(@RequestBody @Valid DadosAtualizacaoPrecoDto dados) {

        try {
            if (!precoRepository.existsById(dados.id_preco())) {
                throw new ValidacaoException("Id do Preço informado não existe!");
            }
            var preco = precoRepository.getReferenceById(dados.id_preco());
            preco.atualizarInformacoes(dados);
            return ResponseEntity.ok(new DadosDetalhamentoPrecoDto(preco));
        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}

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
import postech.fiap.com.br.parquimetro_api.domain.locacao.*;

@RestController
@RequestMapping("locacoes")
public class LocacaoController {

    @Autowired
    LocacaoRepository locacaoRepository;

    @Autowired
    LocacaoService locacaoService;

    @PostMapping
    @Transactional

    public ResponseEntity gravar(@RequestBody @Valid LocacaoDto locacaoDto, UriComponentsBuilder uriComponentsBuilder) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locacaoService.gravar(locacaoDto));
    }

    @PutMapping
    @Transactional

    public ResponseEntity alterar(@RequestBody DadosAtualizacaoLocacao dadosAtualizacaoLocacao, UriComponentsBuilder uriComponentsBuilder) {
        return ResponseEntity.status(HttpStatus.OK).body(locacaoService.alterar(dadosAtualizacaoLocacao));
    }

    @PutMapping("/encerrar")
    @Transactional
    public ResponseEntity encerraLocacao(@RequestBody DadosAtualizacaoLocacao dadosAtualizacaoLocacao, UriComponentsBuilder uriComponentsBuilder) {
        return ResponseEntity.status(HttpStatus.OK).body(locacaoService.encerraLocacao(dadosAtualizacaoLocacao));
    }
    @GetMapping
    public ResponseEntity<Page<DadosListagemLocacaoDto>> listar(@PageableDefault(size = 10) Pageable paginacao) {
        var page = locacaoRepository.findAll(paginacao).map(DadosListagemLocacaoDto::new);
        return ResponseEntity.ok(page);

    }

}

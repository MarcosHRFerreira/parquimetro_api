package postech.fiap.com.br.parquimetro_api.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import postech.fiap.com.br.parquimetro_api.ValidacaoException;
import postech.fiap.com.br.parquimetro_api.domain.veiculo.*;

import java.util.List;

@RestController
@RequestMapping("veiculos")
public class VeiculoController {

    @Autowired
    VeiculoRepository veiculoRepository;

    public VeiculoController(VeiculoRepository veiculoRepository) {
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody VeiculoDto veiculoDto, UriComponentsBuilder uriBuilder) {
        try {
            var veiculo = new VeiculoEntity(veiculoDto);
            veiculoRepository.save(veiculo);
            var uri = uriBuilder.path("/veiculo/{id}").buildAndExpand(veiculo.getId_veiculo()).toUri();
            return ResponseEntity.created(uri).body(new DadosDetalhamentoVeiculoDto(veiculo));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping
    @Transactional
    public ResponseEntity altera(@RequestBody @Valid DadosAtualizacaoVeiculosDto dados) {
        try {
            if (!veiculoRepository.existsById(dados.id_veiculo())) {
                throw new ValidacaoException("Id do veiculo informado não existe!");
            }
            var veiculo = veiculoRepository.getReferenceById(dados.id_veiculo());
            veiculo.atualizarInformacoes(dados);
            return ResponseEntity.ok(new DadosDetalhamentoVeiculoDto(veiculo));
        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemVeiculoDto>> listar(@PageableDefault(size = 10, sort = {"marca"}) Pageable paginacao) {
        var page = veiculoRepository.findAll(paginacao).map(DadosListagemVeiculoDto::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        try {
            if (!veiculoRepository.existsById(id)) {
                throw new ValidacaoException("Id do veiculo informado não existe!");
            }
            var veiculo = veiculoRepository.getReferenceById(id);
            return ResponseEntity.ok(new DadosDetalhamentoVeiculoDto(veiculo));
        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
//    @GetMapping
//    public ResponseEntity<List<DadosListagemVeiculoDto>> listar() {
//        var veiculos = veiculoRepository.findAll().stream()
//                .map(DadosListagemVeiculoDto::new)
//                .toList();
//        return ResponseEntity.ok(veiculos);
//    }

}

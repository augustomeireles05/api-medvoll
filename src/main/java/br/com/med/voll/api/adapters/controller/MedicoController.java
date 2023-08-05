package br.com.med.voll.api.adapters.controller;

import br.com.med.voll.api.domain.medico.DadosAtualizacaoMedico;
import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.domain.medico.DadosListagemMedico;
import br.com.med.voll.api.usecase.medico.MedicoUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoUseCase medicoUseCase;

    @PostMapping
    public ResponseEntity<DadosCadastroMedico> save(@RequestBody @Valid DadosCadastroMedico dados) {
        return medicoUseCase.save(dados);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        return medicoUseCase.update(dados);
    }

    @GetMapping("/listAll")
    public ResponseEntity<Page<DadosListagemMedico>> listAll(@PageableDefault(size = 10, sort={"nome"})Pageable page) {
        Page<DadosListagemMedico> medicos = medicoUseCase.listAll(page);
        return ResponseEntity.ok().body(medicos);
    }

    @GetMapping("/listAllActive")
    public ResponseEntity<Page<DadosListagemMedico>> listAllActive(@PageableDefault(size = 10, page = 0, sort= {"nome"}) Pageable page) {
        Page<DadosListagemMedico> medicos = medicoUseCase.listAllActive(page);
        return ResponseEntity.ok().body(medicos);
    }

    @DeleteMapping("/desactive/{id}")
    public ResponseEntity desactive(@PathVariable Long id) {
        return medicoUseCase.desactive(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return medicoUseCase.delete(id);
    }
}

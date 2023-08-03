package br.com.med.voll.api.controller;

import br.com.med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.domain.paciente.DadosListagemPaciente;
import br.com.med.voll.api.usecase.paciente.PacienteUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteUseCase pacienteUseCase;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosCadastroPaciente> save(@RequestBody @Valid DadosCadastroPaciente dados) {
        return pacienteUseCase.save(dados);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosAtualizacaoPaciente> update(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
        return pacienteUseCase.update(dados);
    }

    @GetMapping("/listAll")
    public ResponseEntity<Page<DadosListagemPaciente>> listAll(@PageableDefault(size = 10, sort={"nome"})Pageable pageable) {
        Page<DadosListagemPaciente> pacientes = pacienteUseCase.listAll(pageable);
        return ResponseEntity.ok().body(pacientes);
    }

    @GetMapping("/listAllActive")
    public ResponseEntity<Page<DadosListagemPaciente>> listAllActive(@PageableDefault(size = 10, page = 0, sort = {"nome"}) Pageable pageable) {
        Page<DadosListagemPaciente> pacientes = pacienteUseCase.listAllActive(pageable);
        return ResponseEntity.ok().body(pacientes);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pacienteUseCase.delete(id);
    }

}

package br.com.med.voll.api.adapters.controller;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.consulta.DadosListagemConsulta;
import br.com.med.voll.api.usecase.consulta.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("consultas")
public class ConsultaController {


    @Autowired
    private ConsultaService service;

    @PostMapping
    public ResponseEntity schedule(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        return service.schedule(dados);
    }

    @GetMapping("/listAll")
    public ResponseEntity<Page<DadosListagemConsulta>> listAll(@PageableDefault(size = 10)Pageable page) {
        Page<DadosListagemConsulta> consultas = service.listAll(page);
        return ResponseEntity.ok().body(consultas);
    }
}

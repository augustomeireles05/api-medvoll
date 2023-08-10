package br.com.med.voll.api.adapters.controller;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.usecase.consulta.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("consultas")
public class ConsultaController {


    @Autowired
    private ConsultaService service;

    @PostMapping
    public ResponseEntity schedule(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        return service.schedule(dados);
    }
}

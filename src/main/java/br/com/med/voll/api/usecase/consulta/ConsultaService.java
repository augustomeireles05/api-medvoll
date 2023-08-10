package br.com.med.voll.api.usecase.consulta;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.http.ResponseEntity;

public interface ConsultaService {

    ResponseEntity schedule(DadosAgendamentoConsulta dados);
}

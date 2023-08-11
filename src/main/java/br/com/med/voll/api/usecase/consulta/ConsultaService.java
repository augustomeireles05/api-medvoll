package br.com.med.voll.api.usecase.consulta;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.consulta.DadosListagemConsulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ConsultaService {

    ResponseEntity schedule(DadosAgendamentoConsulta dados);

    Page<DadosListagemConsulta> listAll(Pageable page);
}

package br.com.med.voll.api.usecase.paciente;

import br.com.med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.domain.paciente.DadosListagemPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface PacienteUseCase {
    ResponseEntity save(DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder);

    ResponseEntity update(DadosAtualizacaoPaciente dados);

    Page<DadosListagemPaciente> listAll(Pageable pageable);

    Page<DadosListagemPaciente> listAllActive(Pageable pageable);

    ResponseEntity desactive(Long id);

    ResponseEntity delete(Long id);

    ResponseEntity getPacienteById(Long id);
}

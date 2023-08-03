package br.com.med.voll.api.usecase.paciente;

import br.com.med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.domain.paciente.DadosListagemPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PacienteUseCase {
    ResponseEntity<DadosCadastroPaciente> save(DadosCadastroPaciente dados);

    ResponseEntity<DadosAtualizacaoPaciente> update(DadosAtualizacaoPaciente dados);

    Page<DadosListagemPaciente> listAll(Pageable pageable);

    Page<DadosListagemPaciente> listAllActive(Pageable pageable);

    void delete(Long id);
}

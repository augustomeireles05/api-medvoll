package br.com.med.voll.api.domain.chainofresponsibility.paciente;

import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.repository.medico.MedicoRepository;
import br.com.med.voll.api.repository.paciente.PacienteRepository;
import org.springframework.http.ResponseEntity;

public interface PacienteHandlerValidation {

    ResponseEntity validate(DadosCadastroPaciente dados, PacienteRepository repository);

    PacienteHandlerValidation setNext(PacienteHandlerValidation nextHandler);
}
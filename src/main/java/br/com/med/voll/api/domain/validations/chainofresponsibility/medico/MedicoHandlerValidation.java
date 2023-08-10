package br.com.med.voll.api.domain.validations.chainofresponsibility.medico;

import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.infrastructure.integration.repository.medico.MedicoRepository;
import org.springframework.http.ResponseEntity;

public interface MedicoHandlerValidation {

    ResponseEntity validate(DadosCadastroMedico dados, MedicoRepository medicoRepository);
    MedicoHandlerValidation setNext(MedicoHandlerValidation nextHandler);
}

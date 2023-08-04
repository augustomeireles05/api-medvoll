package br.com.med.voll.api.domain.chainofresponsibility;

import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.repository.medico.MedicoRepository;
import org.springframework.http.ResponseEntity;

public interface HandlerValidation {

    ResponseEntity validate(DadosCadastroMedico dados, MedicoRepository medicoRepository);
    HandlerValidation setNext(HandlerValidation nextHandler);
}

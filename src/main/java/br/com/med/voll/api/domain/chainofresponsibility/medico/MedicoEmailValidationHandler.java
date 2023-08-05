package br.com.med.voll.api.domain.chainofresponsibility.medico;


import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.exception.DadosCadastroResponseError;
import br.com.med.voll.api.repository.medico.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_EMAIL;

@Component
public class MedicoEmailValidationHandler implements MedicoHandlerValidation {
    private MedicoHandlerValidation nextHandler;

    @Override
    public ResponseEntity validate(DadosCadastroMedico dados, MedicoRepository medicoRepository) {
        if (medicoRepository.existsByEmail(dados.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosCadastroResponseError(ERROR_MESSAGE_DUPLICATE_EMAIL));
        }
        return nextHandler != null ? nextHandler.validate(dados, medicoRepository) : null;
    }

    @Override
    public MedicoHandlerValidation setNext(MedicoHandlerValidation nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }
}

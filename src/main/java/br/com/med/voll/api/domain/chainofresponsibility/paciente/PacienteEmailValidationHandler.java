package br.com.med.voll.api.domain.chainofresponsibility.paciente;

import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.exception.DadosCadastroResponseError;
import br.com.med.voll.api.repository.paciente.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_EMAIL;

@Component
public class PacienteEmailValidationHandler implements PacienteHandlerValidation {

    private PacienteHandlerValidation nextHandler;

    @Override
    public ResponseEntity validate(DadosCadastroPaciente dados, PacienteRepository repository) {
        if(repository.existsByEmail(dados.email()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosCadastroResponseError(ERROR_MESSAGE_DUPLICATE_EMAIL));
        return nextHandler != null ? nextHandler.validate(dados, repository) : null;
    }

    @Override
    public PacienteHandlerValidation setNext(PacienteHandlerValidation nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }
}

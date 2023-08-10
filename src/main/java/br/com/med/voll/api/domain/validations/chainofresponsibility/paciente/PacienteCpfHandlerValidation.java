package br.com.med.voll.api.domain.validations.chainofresponsibility.paciente;

import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.infrastructure.exception.DadosCadastroResponseError;
import br.com.med.voll.api.infrastructure.integration.repository.paciente.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_CPF;

@Component
public class PacienteCpfHandlerValidation implements PacienteHandlerValidation {

    @Override
    public ResponseEntity validate(DadosCadastroPaciente dados, PacienteRepository repository) {
        if(repository.existsByCpf(dados.cpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosCadastroResponseError(ERROR_MESSAGE_DUPLICATE_CPF));
        }
        return null;
    }

    @Override
    public PacienteHandlerValidation setNext(PacienteHandlerValidation nextHandler) {
        return this;
    }
}

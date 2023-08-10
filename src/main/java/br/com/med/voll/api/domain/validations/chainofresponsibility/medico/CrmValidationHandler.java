package br.com.med.voll.api.domain.validations.chainofresponsibility.medico;

import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.infrastructure.exception.DadosCadastroResponseError;
import br.com.med.voll.api.infrastructure.integration.repository.medico.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_CRM;

@Component
public class CrmValidationHandler implements MedicoHandlerValidation {

    @Override
    public ResponseEntity validate(DadosCadastroMedico dados, MedicoRepository medicoRepository) {
        if (medicoRepository.existsByCrm(dados.crm())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosCadastroResponseError(ERROR_MESSAGE_DUPLICATE_CRM));
        }
        return null; // No conflict, move to the next handler
    }

    @Override
    public MedicoHandlerValidation setNext(MedicoHandlerValidation nextHandler) {
        return this; // This handler is the last one in the chain
    }
}

package br.com.med.voll.api.domain.chainofresponsibility;

import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.repository.medico.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_CRM;

@Component
public class CrmValidationHandler implements HandlerValidation {

    @Override
    public ResponseEntity validate(DadosCadastroMedico dados, MedicoRepository medicoRepository) {
        if (medicoRepository.existsByCrm(dados.crm())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ERROR_MESSAGE_DUPLICATE_CRM);
        }
        return null; // No conflict, move to the next handler
    }

    @Override
    public HandlerValidation setNext(HandlerValidation nextHandler) {
        return this; // This handler is the last one in the chain
    }
}

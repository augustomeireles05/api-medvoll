package br.com.med.voll.api.usecase.medico.impl;

import br.com.med.voll.api.domain.chainofresponsibility.medico.CrmValidationHandler;
import br.com.med.voll.api.domain.chainofresponsibility.medico.MedicoEmailValidationHandler;
import br.com.med.voll.api.domain.chainofresponsibility.medico.MedicoHandlerValidation;
import br.com.med.voll.api.domain.medico.*;
import br.com.med.voll.api.exception.DadosCadastroResponseError;
import br.com.med.voll.api.exception.NotFoundException;
import br.com.med.voll.api.repository.medico.MedicoRepository;
import br.com.med.voll.api.usecase.medico.MedicoUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static br.com.med.voll.api.utils.Constants.ERROR_SAVE_MEDICO;
import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_EMAIL;
import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_CRM;
import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_NOT_FOUND_MEDICO;

@Component
@Slf4j
public class MedicoUseCaseImpl implements MedicoUseCase {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    @Transactional
    public ResponseEntity save(DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        try {
            MedicoHandlerValidation emailHandler = new MedicoEmailValidationHandler();
            MedicoHandlerValidation crmHandler = new CrmValidationHandler();
            emailHandler.setNext(crmHandler);

            ResponseEntity<?> validationResponse = emailHandler.validate(dados, medicoRepository);
            if (validationResponse != null) {
                return validationResponse;
            }

            Medico medico = new Medico(dados);
            medicoRepository.save(medico);

            URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
            return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
        } catch (Exception e) {
            log.error(ERROR_SAVE_MEDICO, e.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity update(DadosAtualizacaoMedico dados) {
        try {
            Optional<Medico> optional = medicoRepository.findById(dados.id());

            if (optional.isPresent()) {
                Medico medicoExistente = optional.get();

                // Check for duplicate email
                if (!medicoExistente.getEmail().equals(dados.email()) && medicoRepository.existsByEmail(dados.email())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosCadastroResponseError(ERROR_MESSAGE_DUPLICATE_EMAIL));
                }

                // Check for duplicate CRM
                if (!medicoExistente.getCrm().equals(dados.crm()) && medicoRepository.existsByCrm(dados.crm())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosCadastroResponseError(ERROR_MESSAGE_DUPLICATE_CRM));
                }

                medicoExistente.updateInfoMedico(dados);
                Medico medico = medicoRepository.save(medicoExistente);
                return ResponseEntity.status(HttpStatus.OK).body(new DadosDetalhamentoMedico(medico));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error(ERROR_SAVE_MEDICO, e.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity getMedicoById(Long id) {
        try {
            Medico medico = medicoRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_MEDICO, id)));
            return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
        } catch(NotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Page<DadosListagemMedico> listAll(Pageable page) {
        Page<Medico> medicos = medicoRepository.findAll(page);
        return medicos.map(DadosListagemMedico::new);
    }

    @Override
    public Page<DadosListagemMedico> listAllActive(Pageable page) {
        Page<Medico> medicos = medicoRepository.findAllByAtivoTrue(page);
        return medicos.map(DadosListagemMedico::new);
    }

    @Override
    @Transactional
    public ResponseEntity desactive(Long id) {
        Medico referenceById = medicoRepository.getReferenceById(id);
        referenceById.delete();
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional
    public ResponseEntity delete(Long id) {
        medicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

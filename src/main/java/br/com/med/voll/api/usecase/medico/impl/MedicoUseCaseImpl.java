package br.com.med.voll.api.usecase.medico.impl;

import br.com.med.voll.api.domain.chainofresponsibility.CrmValidationHandler;
import br.com.med.voll.api.domain.chainofresponsibility.EmailValidationHandler;
import br.com.med.voll.api.domain.chainofresponsibility.HandlerValidation;
import br.com.med.voll.api.domain.medico.DadosAtualizacaoMedico;
import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.domain.medico.DadosListagemMedico;
import br.com.med.voll.api.domain.medico.Medico;
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

import java.util.Optional;

import static br.com.med.voll.api.utils.Constants.ERROR_SAVE_MEDICO;

@Component
@Slf4j
public class MedicoUseCaseImpl implements MedicoUseCase {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    @Transactional
    public ResponseEntity save(DadosCadastroMedico dados) {
        try {
            HandlerValidation emailHandler = new EmailValidationHandler();
            HandlerValidation crmHandler = new CrmValidationHandler();
            emailHandler.setNext(crmHandler);

            return emailHandler.validate(dados, medicoRepository);
        } catch (Exception e) {
            log.error(ERROR_SAVE_MEDICO, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @Override
    @Transactional
    public ResponseEntity<DadosAtualizacaoMedico> update(DadosAtualizacaoMedico dados) {
        try {
            Optional<Medico> optional = medicoRepository.findById(dados.id());

            if (optional.isPresent()) {
                Medico medicoExistente = optional.get();

                // Check for duplicate email
                if (!medicoExistente.getEmail().equals(dados.email()) && medicoRepository.existsByEmail(dados.email())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }

                // Check for duplicate CRM
                if (!medicoExistente.getCrm().equals(dados.crm()) && medicoRepository.existsByCrm(dados.crm())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }

                medicoExistente.updateInfoMedico(dados);
                medicoRepository.save(medicoExistente);
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error(ERROR_SAVE_MEDICO, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<Void> delete(Long id) {
        try {
            Optional<Medico> optional = medicoRepository.findById(id);

            if (optional.isPresent()) {
                Medico medicoExistente = optional.get();
                medicoExistente.delete();
                medicoRepository.save(medicoExistente);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error(ERROR_SAVE_MEDICO, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

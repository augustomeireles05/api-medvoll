package br.com.med.voll.api.usecase.paciente.impl;

import br.com.med.voll.api.domain.chainofresponsibility.paciente.PacienteCpfHandlerValidation;
import br.com.med.voll.api.domain.chainofresponsibility.paciente.PacienteEmailValidationHandler;
import br.com.med.voll.api.domain.chainofresponsibility.paciente.PacienteHandlerValidation;
import br.com.med.voll.api.domain.paciente.*;
import br.com.med.voll.api.exception.DadosCadastroResponseError;
import br.com.med.voll.api.exception.NotFoundException;
import br.com.med.voll.api.repository.paciente.PacienteRepository;
import br.com.med.voll.api.usecase.paciente.PacienteUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_EMAIL;
import static br.com.med.voll.api.utils.Constants.ERROR_SAVE_PACIENTE;
import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_DUPLICATE_CPF;
import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_NOT_FOUND_PACIENTE;

@Slf4j
@Component
public class PacienteUseCaseImpl implements PacienteUseCase {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    @Transactional
    public ResponseEntity save(DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
        try {
            PacienteHandlerValidation emailHandler = new PacienteEmailValidationHandler();
            PacienteHandlerValidation cpfHandler = new PacienteCpfHandlerValidation();
            emailHandler.setNext(cpfHandler);

            ResponseEntity validationResponse = emailHandler.validate(dados, pacienteRepository);
            if(validationResponse != null)
                return validationResponse;

            Paciente paciente = new Paciente(dados);
            pacienteRepository.save(paciente);

            var uri = uriBuilder.path("pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
            return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));

        } catch(Exception e) {
            log.error(ERROR_SAVE_PACIENTE, e.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity update(DadosAtualizacaoPaciente dados) {
        try {
            Optional<Paciente> optionalPaciente = pacienteRepository.findById(dados.id());

            if(optionalPaciente.isPresent()) {
                Paciente pacienteExistente = optionalPaciente.get();

                if(!pacienteExistente.getEmail().equals(dados.email()) && pacienteRepository.existsByEmail(dados.email()))
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosCadastroResponseError(ERROR_MESSAGE_DUPLICATE_EMAIL));

                if(!pacienteExistente.getCpf().equals(dados.cpf()) && pacienteRepository.existsByCpf(dados.cpf()))
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosCadastroResponseError(ERROR_MESSAGE_DUPLICATE_CPF));

                pacienteExistente.updateInfoPaciente(dados);
                Paciente paciente = pacienteRepository.save(pacienteExistente);
                return ResponseEntity.status(HttpStatus.OK).body(new DadosDetalhamentoPaciente(paciente));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch(Exception e) {
            log.error(ERROR_SAVE_PACIENTE, e.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity getPacienteById(Long id) {
        try {
            Paciente paciente = pacienteRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_PACIENTE, id)));
            return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Page<DadosListagemPaciente> listAll(Pageable pageable) {
        Page<Paciente> pacientes = pacienteRepository.findAll(pageable);
        return pacientes.map(DadosListagemPaciente::new);
    }

    @Override
    public Page<DadosListagemPaciente> listAllActive(Pageable pageable) {
        Page<Paciente> pacientes = pacienteRepository.findAllByAtivoTrue(pageable);
        return pacientes.map(DadosListagemPaciente::new);
    }

    @Override
    @Transactional
    public ResponseEntity desactive(Long id) {
        Paciente referenceById = pacienteRepository.getReferenceById(id);
        referenceById.delete();
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional
    public ResponseEntity delete(Long id) {
        pacienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

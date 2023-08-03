package br.com.med.voll.api.usecase.paciente.impl;

import br.com.med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.domain.paciente.DadosListagemPaciente;
import br.com.med.voll.api.domain.paciente.Paciente;
import br.com.med.voll.api.exception.DatabaseAccessException;
import br.com.med.voll.api.repository.paciente.PacienteRepository;
import br.com.med.voll.api.usecase.paciente.PacienteUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static br.com.med.voll.api.utils.Constants.ERROR_SAVE_PACIENTE;

@Slf4j
@Component
public class PacienteUseCaseImpl implements PacienteUseCase {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public ResponseEntity<DadosCadastroPaciente> save(DadosCadastroPaciente dados) {
        try {
            Paciente paciente = new Paciente(dados);
            pacienteRepository.save(paciente);
        } catch(Exception e) {
            log.error(ERROR_SAVE_PACIENTE, e.getCause());
            throw new DatabaseAccessException(e.getMessage() + " " + PacienteRepository.class.getSimpleName());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<DadosAtualizacaoPaciente> update(DadosAtualizacaoPaciente dados) {
        try {
            Optional<Paciente> optional = pacienteRepository.findById(dados.id());

            if(optional.isPresent()) {
                Paciente pacienteExistente = optional.get();
                pacienteExistente.updateInfoPaciente(dados);
                pacienteRepository.save(pacienteExistente);

                return ResponseEntity.status(HttpStatus.OK).build();

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
         log.error(ERROR_SAVE_PACIENTE, e.getCause());
         throw new DatabaseAccessException(e.getMessage() + " " + PacienteRepository.class.getSimpleName());
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
    public void delete(Long id) {
        Paciente referenceById = pacienteRepository.getReferenceById(id);
        referenceById.delete();
    }

}

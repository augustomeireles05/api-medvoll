package br.com.med.voll.api.usecase.medico.impl;

import br.com.med.voll.api.domain.medico.DadosAtualizacaoMedico;
import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.domain.medico.DadosListagemMedico;
import br.com.med.voll.api.domain.medico.Medico;
import br.com.med.voll.api.exception.DatabaseAccessException;
import br.com.med.voll.api.repository.medico.MedicoRepository;
import br.com.med.voll.api.usecase.medico.MedicoUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static br.com.med.voll.api.utils.Constants.ERROR_SAVE_MEDICO;

@Component
@Slf4j
public class MedicoUseCaseImpl implements MedicoUseCase {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public ResponseEntity<DadosCadastroMedico> save(DadosCadastroMedico dados) {
        try {
            Medico medico = new Medico(dados);
            medicoRepository.save(medico);
        } catch (Exception e) {
            log.error(ERROR_SAVE_MEDICO, e.getCause());
            throw new DatabaseAccessException(e.getMessage() + " " + MedicoRepository.class.getSimpleName());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<DadosAtualizacaoMedico> update(DadosAtualizacaoMedico dados) {
        try {
            Optional<Medico> optional = medicoRepository.findById(dados.id());

            if(optional.isPresent()) {
                Medico medicoExistente = optional.get();
                medicoExistente.updateInfoMedico(dados);
                medicoRepository.save(medicoExistente);

                return ResponseEntity.status(HttpStatus.OK).build();

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch(Exception e) {
            log.error(ERROR_SAVE_MEDICO, e.getCause());
            throw new DatabaseAccessException(e.getMessage() + " " + MedicoRepository.class.getSimpleName());
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
    public ResponseEntity delete(Long id) {
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
            log.error(ERROR_SAVE_MEDICO, e.getCause());
            throw new DatabaseAccessException(e.getMessage() + " " + MedicoRepository.class.getSimpleName());
        }
    }
}

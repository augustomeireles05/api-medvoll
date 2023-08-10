package br.com.med.voll.api.domain.validations.strategy.consulta.impl;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.validations.strategy.consulta.AgendamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.ValidacaoException;
import br.com.med.voll.api.infrastructure.integration.repository.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.CANNOT_SCHEDULE_WITH_DELETED_PATIENT;

/**
 * Verifica se o paciente  da consulta está ativo. Se não tiver, joga uma exception
 */

@Component
public class CheckPacienteIsActiveValidation implements AgendamentoConsultaValidation {

    @Autowired
    private PacienteRepository repository;

    @Override
    public void validate(DadosAgendamentoConsulta dados) {
        var pacienteIsActive = repository.findAtivoById(dados.idPaciente());
        if(!pacienteIsActive)
            throw new ValidacaoException(CANNOT_SCHEDULE_WITH_DELETED_PATIENT);
    }
}

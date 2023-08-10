package br.com.med.voll.api.domain.validations.strategy.consulta.impl;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.validations.strategy.consulta.AgendamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.ValidacaoException;
import br.com.med.voll.api.infrastructure.integration.repository.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.CANNOT_SCHEDULE_WITH_DELETED_DOCTOR;

/**
 * Verifica se o médico que está sendo marcado está ativo. Se não estiver, joga uma exception
 */

@Component
public class CheckMedicoIsActiveValidation implements AgendamentoConsultaValidation {

    @Autowired
    private MedicoRepository repository;

    @Override
    public void validate(DadosAgendamentoConsulta dados) {
        //escolha de médico opcional. Se o id do médico vir nulo, ele sai dessa validação.
        if(dados.idMedico() == null) {
            return;
        }

        var medicoIsActive = repository.findAtivoById(dados.idMedico());

        if(!medicoIsActive)
            throw new ValidacaoException(CANNOT_SCHEDULE_WITH_DELETED_DOCTOR);
    }
}

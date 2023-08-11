package br.com.med.voll.api.domain.validations.strategy.consulta.impl.agendamento;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.validations.strategy.consulta.AgendamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.ValidacaoException;
import br.com.med.voll.api.infrastructure.integration.repository.consulta.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.DOCTOR_ALREADY_BOOKED_AT_THIS_TIME;

/**
 * Verifica se o médico possui uma outra consulta agendada no mesmo horário que está tendo essa tentativa de marcação de consulta.
 */

@Component
public class CheckIfMedicoHasAnotherAppointmentAtTheSameTimeValidation implements AgendamentoConsultaValidation {

    @Autowired
    private ConsultaRepository repository;

    @Override
    public void validate(DadosAgendamentoConsulta dados) {
        var medicoPossuiOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndData(dados.idMedico(), dados.data());

        if(medicoPossuiOutraConsultaNoMesmoHorario)
            throw new ValidacaoException(DOCTOR_ALREADY_BOOKED_AT_THIS_TIME);

    }
}

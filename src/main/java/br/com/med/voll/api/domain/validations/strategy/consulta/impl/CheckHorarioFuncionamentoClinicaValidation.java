package br.com.med.voll.api.domain.validations.strategy.consulta.impl;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.validations.strategy.consulta.AgendamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

import static br.com.med.voll.api.utils.Constants.NON_OPERATIONAL_HOURS_ERROR;

/**
 * O horário de funcionamento da clínica é de segunda a sábado, das 07:00 às 19:00;
 */

@Component
public class CheckHorarioFuncionamentoClinicaValidation implements AgendamentoConsultaValidation {

    @Override
    public void validate(DadosAgendamentoConsulta dados) {
        var dataConsulta = dados.data();

        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAberturaDaClinica = dataConsulta.getHour() < 7;
        var depoisDoEncerramentoDaClinica = dataConsulta.getHour() > 18; //a última consulta é as 18h e não poderemos ter consulta às 19.

        if(domingo || antesDaAberturaDaClinica || depoisDoEncerramentoDaClinica)
            throw new ValidacaoException(NON_OPERATIONAL_HOURS_ERROR);

    }
}

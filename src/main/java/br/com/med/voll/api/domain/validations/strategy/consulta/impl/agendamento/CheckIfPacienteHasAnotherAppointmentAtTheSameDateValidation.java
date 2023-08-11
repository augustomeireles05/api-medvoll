package br.com.med.voll.api.domain.validations.strategy.consulta.impl.agendamento;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.validations.strategy.consulta.AgendamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.ValidacaoException;
import br.com.med.voll.api.infrastructure.integration.repository.consulta.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static br.com.med.voll.api.utils.Constants.PATIENT_ALREADY_HAS_APPOINTMENT_ON_THIS_DAY;

/**
 * Verifica se o paciente que está tentando agendar uma consulta já possui uma consulta agendada naquela data.
 * Nessa regra de negócio, o paciente só pode ter uma consulta por dia.
 */

@Component
public class CheckIfPacienteHasAnotherAppointmentAtTheSameDateValidation implements AgendamentoConsultaValidation {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public void validate(DadosAgendamentoConsulta dados) {
        var primeiroHorario = dados.data().withHour(7);
        var ultimoHorario = dados.data().withHour(18);
        var pacientePossuiOutraConsultaNoDia = consultaRepository.existsByPacienteIdAndDataBetween(dados.idPaciente(), primeiroHorario, ultimoHorario);

        if(pacientePossuiOutraConsultaNoDia)
            throw new ValidacaoException(PATIENT_ALREADY_HAS_APPOINTMENT_ON_THIS_DAY);

    }
}

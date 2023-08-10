package br.com.med.voll.api.domain.validations.strategy.consulta.impl;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.validations.strategy.consulta.AgendamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Essa classe terá por finalidade realizar a validação que tem por objetivo verificar se o agendamento está sendo validado pelo menos com o mínimo 30 minutos de antecedência ao horário/data da consulta agendada.
 */

@Component
public class CheckHorarioAntecedenciaValidation implements AgendamentoConsultaValidation {

    @Override
    public void validate(DadosAgendamentoConsulta dados) {
        var dataConsulta = dados.data();
        var agora = LocalDateTime.now();

        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if(diferencaEmMinutos < 30)
            throw new ValidacaoException("A consulta deve ser agendada com antecedência mínima de 30 minutos.");

    }
}

package br.com.med.voll.api.domain.validations.strategy.consulta;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public interface AgendamentoConsultaValidation {
    void validate(DadosAgendamentoConsulta dados);
}

package br.com.med.voll.api.domain.validations.strategy.consulta;

import br.com.med.voll.api.domain.consulta.DadosCancelamentoConsulta;

public interface CancelamentoConsultaValidation {

    void validate(DadosCancelamentoConsulta dados);
}

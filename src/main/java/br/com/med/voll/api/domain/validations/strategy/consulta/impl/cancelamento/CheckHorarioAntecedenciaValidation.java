package br.com.med.voll.api.domain.validations.strategy.consulta.impl.cancelamento;

import br.com.med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import br.com.med.voll.api.domain.validations.strategy.consulta.CancelamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.ValidacaoException;
import br.com.med.voll.api.infrastructure.integration.repository.consulta.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

import static br.com.med.voll.api.utils.Constants.ERROR_CANCEL_CONSULTA;

@Component("ValidadorHorarioAntecedenciaCancelamento")
public class CheckHorarioAntecedenciaValidation implements CancelamentoConsultaValidation {

    @Autowired
    private ConsultaRepository consultaRepository;


    @Override
    public void validate(DadosCancelamentoConsulta dados) {
        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

        if(diferencaEmHoras < 24)
            throw new ValidacaoException(ERROR_CANCEL_CONSULTA);

    }
}

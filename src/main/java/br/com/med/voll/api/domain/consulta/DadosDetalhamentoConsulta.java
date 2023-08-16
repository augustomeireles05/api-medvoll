package br.com.med.voll.api.domain.consulta;

import java.time.LocalDateTime;

import static br.com.med.voll.api.utils.Constants.STANDARD_PATTERN_TIMESTAMP_WITHOUT_SECONDS;
public record DadosDetalhamentoConsulta(Long id, Long idMedico, Long idPaciente, LocalDateTime data, String dataHoraSolicitacaoAgendamento) {

    public DadosDetalhamentoConsulta(Consulta consulta) {
        this(
            consulta.getId(),
            consulta.getMedico().getId(),
            consulta.getPaciente().getId(),
            consulta.getData(),
            consulta.getDataHoraSolicitacaoAgendamento().format(STANDARD_PATTERN_TIMESTAMP_WITHOUT_SECONDS)
        );
    }
}

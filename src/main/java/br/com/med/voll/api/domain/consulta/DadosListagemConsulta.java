package br.com.med.voll.api.domain.consulta;

import br.com.med.voll.api.domain.medico.DadosDetalhamentoMedico;
import br.com.med.voll.api.domain.medico.DadosDetalhamentoMedicoParaDetalheConsulta;
import br.com.med.voll.api.domain.paciente.DadosDetalhamentoPaciente;
import br.com.med.voll.api.domain.paciente.DadosDetalhamentoPacienteParaDetalheConsulta;

import java.time.LocalDateTime;

public record DadosListagemConsulta(
        Long id,
        DadosDetalhamentoMedicoParaDetalheConsulta medico,
        DadosDetalhamentoPacienteParaDetalheConsulta paciente,
        LocalDateTime data
) {
    public DadosListagemConsulta(Consulta consulta) {
        this(
            consulta.getId(),
            new DadosDetalhamentoMedicoParaDetalheConsulta(consulta.getMedico()),
            new DadosDetalhamentoPacienteParaDetalheConsulta(consulta.getPaciente()),
            consulta.getData()
        );
    }
}

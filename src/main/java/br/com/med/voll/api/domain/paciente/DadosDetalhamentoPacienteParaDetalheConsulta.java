package br.com.med.voll.api.domain.paciente;

import br.com.med.voll.api.domain.medico.DadosDetalhamentoMedicoParaDetalheConsulta;

public record DadosDetalhamentoPacienteParaDetalheConsulta(Long id, String nome, String cpf, String telefone) {

    public DadosDetalhamentoPacienteParaDetalheConsulta(Paciente paciente) {
        this(
            paciente.getId(),
            paciente.getNome(),
            paciente.getCpf(),
            paciente.getTelefone()
        );
    }
}

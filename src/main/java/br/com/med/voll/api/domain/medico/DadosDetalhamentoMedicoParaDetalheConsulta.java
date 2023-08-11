package br.com.med.voll.api.domain.medico;

public record DadosDetalhamentoMedicoParaDetalheConsulta(Long id, String nome, String email, String telefone, String crm, Especialidade especialidade) {

    public DadosDetalhamentoMedicoParaDetalheConsulta(Medico medico) {
        this(
                medico.getId(),
                medico.getNome(),
                medico.getEmail(),
                medico.getCrm(),
                medico.getTelefone(),
                medico.getEspecialidade()
        );
    }
}



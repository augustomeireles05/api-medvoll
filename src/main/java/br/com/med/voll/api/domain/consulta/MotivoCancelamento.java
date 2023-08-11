package br.com.med.voll.api.domain.consulta;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MotivoCancelamento {

    PACIENTE_DESISTIU(1, "Paciente desistiu"),
    MEDICO_CANCELOU(2, "Médico cancelou"),
    OUTROS(2, "Outros");

    private int id;
    private String descricao;

}

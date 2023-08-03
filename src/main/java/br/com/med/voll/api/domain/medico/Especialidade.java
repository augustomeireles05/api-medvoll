package br.com.med.voll.api.domain.medico;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Especialidade {

    ORTOPEDIA(1, "Ortopedista"),
    CARDIOLOGIA(2, "Cardiologista"),
    GINECOLOGIA(3, "Ginecologista"),
    DERMATOLOGIA(4, "Dermatologista");

    private int id;
    private final String descricao;

}

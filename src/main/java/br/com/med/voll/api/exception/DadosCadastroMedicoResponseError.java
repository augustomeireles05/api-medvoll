package br.com.med.voll.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DadosCadastroMedicoResponseError {
    public String errorMessage;
}
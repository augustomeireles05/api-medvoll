package br.com.med.voll.api.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(
        @NotBlank(message = "Logradouro não pode estar em branco nem ser nulo.")
        String logradouro,
        @NotBlank(message = "Bairro não pode estar em branco nem ser nulo.")
        String bairro,
        @NotBlank(message = "CEP não pode estar em branco nem ser nulo.")
        @Pattern(regexp = "\\d{8}")
        String cep,
        @NotBlank(message = "Cidade não pode estar em branco nem ser nulo.")
        String cidade,
        @NotBlank(message = "UF não pode estar em branco nem ser nulo.")
        String uf,
        String complemento,
        String numero
) {
}

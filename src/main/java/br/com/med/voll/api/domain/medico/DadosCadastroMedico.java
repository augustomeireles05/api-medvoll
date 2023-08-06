package br.com.med.voll.api.domain.medico;

import br.com.med.voll.api.domain.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroMedico(
        @NotBlank(message = "Nome não pode estar em branco nem ser nulo.")
        String nome,
        @NotBlank(message = "Email não pode estar em branco nem ser nulo.")
        @Email
        String email,
        @NotBlank(message = "Telefone não pode estar em branco nem ser nulo.")
        String telefone,
        @NotBlank(message = "CRM não pode estar em branco nem ser nulo.")
        @Pattern(regexp = "\\d{4,6}")
        String crm,
        @NotNull(message = "Especialidade não pode ser nulo.")
        Especialidade especialidade,
        @NotNull
        @Valid
        DadosEndereco endereco
) {
}

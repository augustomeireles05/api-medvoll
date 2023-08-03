package br.com.med.voll.api.domain.paciente;

import br.com.med.voll.api.domain.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroPaciente(
        @NotBlank(message = "Nome do paciente não pode estar em branco nem ser nulo.")
        String nome,
        @NotBlank(message = "Email do paciente não pode estar em branco nem ser nulo.")
        String email,
        @NotBlank(message = "Telefone do paciente não pode estar em branco nem ser nulo.")
        String telefone,
        @NotBlank(message = "CPF do paciente não pode estar em branco nem ser nulo.")
        String cpf,
        @NotNull @Valid
        DadosEndereco endereco
) {
}

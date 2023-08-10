package br.com.med.voll.api.domain.consulta;

import br.com.med.voll.api.domain.medico.Especialidade;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * @@JsonAlias serve para mapear "apelidos" alternativos para campos que serão recebidos do JSON, sendo possível atribuir múltiplos alias.
 * Logo, além de podermos passar o nome da forma atribuída ao atributo, também podemos montar o JSON no insomnia pelos seus "apelidos"
 *
 * @@JsonFormat serve para personalizar o padrão e campos do tipo data. Por padrão devemos colocar no insomnia da seguinte forma: "YYYY-MM-DDTHH:MM:SS" onde o T separa data de hora.
 * Através da anotation @JsonFormat, conseguimos criar o padrão desejado. No caso abaixo, foi-se construído o devido padrão personalizado: "dd/MM/yyyy HH:mm". Isso pode ser feito diretamente através da DTO. Note que no atributo pattern indicamos o padrão de formatação esperado, seguintdo as regras definidas pelo padrão de datas do Java.
 * Essas informações de padrão pode ser utilizada nas classes DTO que representam as informações que a API devolve, para que o JSON devolvido pela requisição seja formatado de acordo com o pattern configurado.
 * Além disso, ela não se restringe apenas à classe LocalDateTime, podendo também ser utilizada em atributos do tipo LocalDate ou LocalTime.
 * A partir dai, conseguimos adaptar o padrão que será recebido pelo JSON da request POST de consultas.
 * @param idMedico
 * @param idPaciente
 * @param data
 */
public record DadosAgendamentoConsulta(

        @JsonAlias({"id_medico", "cod_medico"})
        Long idMedico,

        @JsonAlias("cod_paciente")
        @NotNull(message = "O id do paciente não pode ser nulo.")
        Long idPaciente,

        @NotNull(message = "A data não pode ser nula.")
        @Future(message = "A data deve ser marcada no futuro.")
        LocalDateTime data,

        Especialidade especialidade
) {
}

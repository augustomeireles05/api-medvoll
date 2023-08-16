package br.com.med.voll.api.domain.consulta;

import br.com.med.voll.api.domain.medico.Medico;
import br.com.med.voll.api.domain.paciente.Paciente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Table(name = "consultas")
@Entity(name = "Consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    private LocalDateTime data;

    @Column(name = "motivo_cancelamento")
    @Enumerated(EnumType.STRING)
    private MotivoCancelamento motivoCancelamento;

    @Column(name = "dt_hr_solicitacao_agendamento", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataHoraSolicitacaoAgendamento = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

    public void delete(MotivoCancelamento motivo) {
        this.motivoCancelamento = motivo;
    }
}

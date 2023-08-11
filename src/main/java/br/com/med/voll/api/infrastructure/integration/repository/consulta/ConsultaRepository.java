package br.com.med.voll.api.infrastructure.integration.repository.consulta;

import br.com.med.voll.api.domain.consulta.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    /**
     * essa consulta utiliza o padrão de select realizado pelo próprio jpa de acordo com sua própria nomenclatura.
     * Por debaixo dos panos, essa nomenclatura verifica se existe (verdadeiro ou falso) uma consulta cujo o id do médico e a data passada já existe na base de dados, dentro da tabela consultas.
     * @param idMedico
     * @param data
     * @return
     */
    boolean existsByMedicoIdAndData(Long idMedico, LocalDateTime data);

    boolean existsByPacienteIdAndDataBetween(Long idPaciente, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);

    @Query("""
           SELECT c
           FROM Consulta c
           INNER JOIN FETCH c.paciente p
           INNER JOIN FETCH c.medico m
           WHERE p.cpf = :cpf
           """)
    Page<Consulta> getConsultaPacienteByCpf(@Param("cpf") String cpf, Pageable page);
}

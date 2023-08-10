package br.com.med.voll.api.infrastructure.integration.repository.medico;

import br.com.med.voll.api.domain.medico.Especialidade;
import br.com.med.voll.api.domain.medico.Medico;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable page);

    boolean existsByEmail(String email);

    boolean existsByCrm(String crm);

    /***
     * @OBJETIVO: Select na base que traz de forma aleatória um médico cuja especialidade foi passada no JSON (sem o id de um determinado médico) e a data não esteja ocupada.
     *
     * @EXPLICAÇÕES:
     * Obtem um médico de forma aleatória. Não trará sempre o mesmo médico;
     * limit 1 traz apenas um médico no select realizado;
     * m.id not in() = traz os médicos cujo id não esteja dentro do subselect;
     * O subselect traz o id do médico que tem consulta na data. Porém o NOT IN nega essa afirmação do subselect, fazendo com que a busca traga apenas médicos cujo id dele(médico) não tenham consulta na data passada pelo cliente(no parâmetro).
     *
     * @param especialidade
     * @param data
     */
    @Query("""
            SELECT m from Medico m 
            WHERE 
            m.ativo = true
            AND
            m.especialidade = :especialidade
            AND
            m.id not in(
                SELECT c.medico.id from Consulta c 
                WHERE
                c.data = :data
            )
            ORDER BY rand()
            limit 1
            
            """)
    Medico findRandomAvailableMedicoByDate(Especialidade especialidade, LocalDateTime data);

    @Query("""
            SELECT m.ativo
            FROM Medico m
            WHERE 
            m.id = :id
            """)
    Boolean findAtivoById(Long id);
}

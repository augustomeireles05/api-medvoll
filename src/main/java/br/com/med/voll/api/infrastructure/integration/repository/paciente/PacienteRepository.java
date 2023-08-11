package br.com.med.voll.api.infrastructure.integration.repository.paciente;

import br.com.med.voll.api.domain.paciente.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Page<Paciente> findAllByAtivoTrue(Pageable pageable);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    @Query("""
            SELECT p.ativo
            FROM Paciente p
            WHERE 
            p.id = :id
            """)
    boolean findAtivoById(Long id);
}

package br.com.med.voll.api.infrastructure.integration.repository.paciente;

import br.com.med.voll.api.domain.paciente.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Page<Paciente> findAllByAtivoTrue(Pageable pageable);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}

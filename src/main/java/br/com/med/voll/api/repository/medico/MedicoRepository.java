package br.com.med.voll.api.repository.medico;

import br.com.med.voll.api.domain.medico.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable page);

    boolean existsByEmail(String email);

    boolean existsByCrm(String crm);
}

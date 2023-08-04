package br.com.med.voll.api.usecase.medico;

import br.com.med.voll.api.domain.medico.DadosAtualizacaoMedico;
import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.domain.medico.DadosListagemMedico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface MedicoUseCase {

    ResponseEntity<DadosCadastroMedico> save(DadosCadastroMedico dados);

    ResponseEntity<DadosAtualizacaoMedico> update(DadosAtualizacaoMedico dados);

    Page<DadosListagemMedico> listAll(Pageable page);

    Page<DadosListagemMedico> listAllActive(Pageable page);

    ResponseEntity delete(Long id);
}

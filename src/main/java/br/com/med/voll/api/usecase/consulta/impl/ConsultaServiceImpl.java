package br.com.med.voll.api.usecase.consulta.impl;

import br.com.med.voll.api.domain.consulta.*;
import br.com.med.voll.api.domain.medico.Medico;
import br.com.med.voll.api.domain.validations.strategy.consulta.AgendamentoConsultaValidation;
import br.com.med.voll.api.domain.validations.strategy.consulta.CancelamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.*;
import br.com.med.voll.api.infrastructure.integration.repository.consulta.ConsultaRepository;
import br.com.med.voll.api.infrastructure.integration.repository.medico.MedicoRepository;
import br.com.med.voll.api.infrastructure.integration.repository.paciente.PacienteRepository;
import br.com.med.voll.api.usecase.consulta.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;


import static br.com.med.voll.api.utils.Constants.NOT_EXIST_ID_MEDICO;
import static br.com.med.voll.api.utils.Constants.NOT_EXIST_ID_PACIENTE;
import static br.com.med.voll.api.utils.Constants.NO_AVAILABLE_DOCTOR_FOR_CHOSEN_DATE;
import static br.com.med.voll.api.utils.Constants.ERROR_MESSAGE_NOT_FOUND_CONSULTA;
import static br.com.med.voll.api.utils.Constants.REQUIRED_SPECIALITY;
import static br.com.med.voll.api.utils.Constants.ERROR_DETELE_CONSULTA;
import static br.com.med.voll.api.utils.Constants.DATE_PATTERN_WITH_HOUR;

@Service
public class ConsultaServiceImpl implements ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<AgendamentoConsultaValidation> validations;

    @Autowired
    private List<CancelamentoConsultaValidation> validadoresCancelamento;

    @Override
    @Transactional
    public ResponseEntity schedule(DadosAgendamentoConsulta dados) {

        if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico()))
            throw new ValidacaoException(NOT_EXIST_ID_MEDICO);

        if(!pacienteRepository.existsById(dados.idPaciente()))
            throw new ValidacaoException(NOT_EXIST_ID_PACIENTE);

        // Inject all existing validations, verifying each one individually and applying them if necessary.
        // We are applying the Single Responsibility Principle (S.O.L.I.D) principles.
            // S -> Each class has a unique responsibility.
            // O -> This class is closed for modification but open for extension. We can create other validations without modifying this class. Only implement the interface.
            // D -> This class depends on abstraction (List<AgendamentoConsultaValidation>), but not on the specific validations that exist.
        validations.forEach(v -> v.validate(dados));

        var medico = chooseMedico(dados);
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());

        //Validação que verifica se não tiver nenhum médico livre na data escolhida
        if(medico == null)
            throw new ValidacaoException(NO_AVAILABLE_DOCTOR_FOR_CHOSEN_DATE);

        //conversão de data para o formato padrão antes de salvar
        DateTimeFormatter formatter = DATE_PATTERN_WITH_HOUR;
        LocalDateTime dataConsulta = LocalDateTime.parse(dados.data().format(formatter), formatter);

        Consulta consulta = new Consulta(null, medico, paciente, dataConsulta, null, LocalDateTime.now());
        consultaRepository.save(consulta);

        return ResponseEntity.ok(new DadosDetalhamentoConsulta(consulta));
    }

    @Override
    public ResponseEntity getConsultaById(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_CONSULTA, id)));
            return ResponseEntity.ok().body(new DadosDetalhamentoConsulta(consulta));
    }

    @Override
    public ResponseEntity<Page<DadosListagemConsulta>> getConsultaByCpfPaciente(String cpf, Pageable page) {
        var consultasPaciente = consultaRepository.getConsultaPacienteByCpf(cpf, page);

        if (consultasPaciente.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PageImpl<>(Collections.emptyList(), page, 0));

        Page<DadosListagemConsulta> dadosConsultas = consultasPaciente.map(DadosListagemConsulta::new);
        return ResponseEntity.ok().body(dadosConsultas);
    }

    @Override
    public Page<DadosListagemConsulta> listAll(Pageable page) {
        Page<Consulta> consultas = consultaRepository.findAll(page);
        return consultas.map(DadosListagemConsulta::new);
    }

    @Override
    @Transactional
    public ResponseEntity delete(DadosCancelamentoConsulta dados) {

        if(!consultaRepository.existsById(dados.idConsulta()))
            throw new ValidacaoException(ERROR_DETELE_CONSULTA);

        validadoresCancelamento.forEach(v -> v.validate(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.delete(dados.motivo());
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica se o médico foi escolhido e retorna o id do médico escolhido diretamente da base.
     * Caso o médico tenha vindo nulo, então a especialidade foi escolhida. Logo verifica se a especialidade tbm não foi escolhida.
     * Caso a especialidade não tenha sido escolhida, lança-se uma exception, uma vez que a especialidade && id do médico não pode ser nulo.
     * Por fim, caso não entrar em nenhuma dessas validações, retorna um médico cuja especialidade é passada pelo cliente e onde o médico não tenha agendamento naquela data.
     *
     * @param dados
     * @return
     */
    private Medico chooseMedico(DadosAgendamentoConsulta dados) {
        //se entrar é porque foi escolhido um médico e portanto carrego tal médico no banco de dados.
        if(dados.idMedico() != null)
            return medicoRepository.getReferenceById(dados.idMedico());

        //se não caiu no if acima, pode está chegando null o id do médico.
        //se caiu como nulo, então a especialidade está sendo preenchida.
        //se a especialidade tiver nula, então lançamos uma exception
        if(dados.especialidade() == null )
            throw new ValidacaoException(REQUIRED_SPECIALITY);

        //se não entrou nos dois ifs acima, então eu preciso escolher uma especialidade no banco de dados um médico aleatório dessa especialidade.
        return medicoRepository.findRandomAvailableMedicoByDate(dados.especialidade(), dados.data());
    }
}

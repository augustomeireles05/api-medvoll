package br.com.med.voll.api.usecase.consulta.impl;

import br.com.med.voll.api.domain.DadosEndereco;
import br.com.med.voll.api.domain.consulta.*;
import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.domain.medico.Especialidade;
import br.com.med.voll.api.domain.medico.Medico;
import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.domain.paciente.Paciente;
import br.com.med.voll.api.domain.validations.strategy.consulta.CancelamentoConsultaValidation;
import br.com.med.voll.api.infrastructure.exception.NotFoundException;
import br.com.med.voll.api.infrastructure.exception.ValidacaoException;
import br.com.med.voll.api.infrastructure.integration.repository.consulta.ConsultaRepository;
import br.com.med.voll.api.infrastructure.integration.repository.medico.MedicoRepository;
import br.com.med.voll.api.infrastructure.integration.repository.paciente.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ConsultaServiceImplTest {

    @InjectMocks
    private ConsultaServiceImpl consultaServiceImpl;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private List<CancelamentoConsultaValidation> validadoresCancelamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    @DisplayName("Deve agendar consulta com dados válidos")
//    void scheduleCenario1() {
//        // Arrange
//        DadosAgendamentoConsulta dados = createSampleDadosAgendamentoConsulta();
//        Medico medico = new Medico();
//        Paciente paciente = new Paciente();
//        when(medicoRepository.existsById(1L)).thenReturn(true);
//        when(pacienteRepository.existsById(2L)).thenReturn(true);
//        when(medicoRepository.getReferenceById(1L)).thenReturn(medico);
//        when(pacienteRepository.getReferenceById(2L)).thenReturn(paciente);
//
//        // Act
//        ResponseEntity response = consultaServiceImpl.schedule(dados);
//
//        // Assert
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        verify(consultaRepository).save(any(Consulta.class));
//    }

    @Test
    @DisplayName("Deve lançar exceção ao agendar consulta com médico inexistente")
    void scheduleCenario2() {
        // Given
        DadosAgendamentoConsulta dados = createSampleDadosAgendamentoConsulta();
        mockMedicoExistence(false);

        // Act & Assert
        assertThrows(ValidacaoException.class, () -> consultaServiceImpl.schedule(dados));
    }

    @Test
    @DisplayName("Deve lançar exceção ao agendar consulta com paciente inexistente")
    void scheduleCenario3() {
        // Given
        DadosAgendamentoConsulta dados = createSampleDadosAgendamentoConsulta();
        when(medicoRepository.existsById(1l)).thenReturn(true);
        when(pacienteRepository.existsById(2l)).thenReturn(false);

        // Act & Assert
        assertThrows(ValidacaoException.class, () -> consultaServiceImpl.schedule(dados));
    }

    @Test
    @DisplayName("Deve retornar detalhes da consulta por ID quando médico associado existir")
    void getConsultaByIdCenario1() {
        // Given
        Long consultaId = 1l;
        Consulta consulta = createSampleConsulta(consultaId);
        mockConsultaExistence(consultaId, consulta);

        // When
        ResponseEntity responseEntity = consultaServiceImpl.getConsultaById(consultaId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        DadosDetalhamentoConsulta dadosDetalhamentoConsulta = (DadosDetalhamentoConsulta) responseEntity.getBody();
        assertEquals(consultaId, dadosDetalhamentoConsulta.id());
        assertEquals(2l, dadosDetalhamentoConsulta.idMedico());
        assertEquals(3l, dadosDetalhamentoConsulta.idPaciente());
        assertEquals(consulta.getData(), dadosDetalhamentoConsulta.data());
    }

    @Test
    @DisplayName("Testa lançamento de NotFoundException para consulta não encontrada")
    public void getConsultaByIdCenario2() {
        Long consultaId = 1l;
        mockConsultaExistence(consultaId, null);

        assertThrows(NotFoundException.class, () -> consultaServiceImpl.getConsultaById(consultaId));
    }

    @Test
    @DisplayName("Consultas encontradas para o CPF do paciente")
    public void getConsultaByCpfPacienteCenario1() {
        String cpf = "12345678900";
        Page<Consulta> consultas = createSampleConsultaPage();
        mockConsultaPacienteByCpf(cpf, consultas);

        ResponseEntity<Page<DadosListagemConsulta>> response = consultaServiceImpl.getConsultaByCpfPaciente(cpf, Pageable.unpaged());

        // Verifique o status e os dados na resposta
        assertResponse(response, HttpStatus.OK, consultas.map(DadosListagemConsulta::new));
    }

    @Test
    @DisplayName("Nenhuma consulta encontrada para o CPF do paciente")
    public void getConsultaByCpfPacienteCenario2() {
        String cpf = "12345678900";
        mockConsultaPacienteByCpf(cpf, Page.empty());

        ResponseEntity<Page<DadosListagemConsulta>> response = consultaServiceImpl.getConsultaByCpfPaciente(cpf, Pageable.unpaged());

        assertResponse(response, HttpStatus.NOT_FOUND, new PageImpl<>(Collections.emptyList()));
    }

    @Test
    @DisplayName("Deve listar todas as consultas com sucesso")
    void listAllCenario1() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        Page<Consulta> consultas = createSampleConsultaPage();
        when(consultaRepository.findAll(pageable)).thenReturn(consultas);

        // Act
        Page<DadosListagemConsulta> result = consultaServiceImpl.listAll(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertEquals(consultas.getTotalElements(), result.getTotalElements());

        verify(consultaRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há consultas")
    void listAllCenario2() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        Page<Consulta> consultasVazias = Page.empty();
        when(consultaRepository.findAll(pageable)).thenReturn(consultasVazias);

        // Act
        Page<DadosListagemConsulta> result = consultaServiceImpl.listAll(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertTrue(result.getContent().isEmpty());

        verify(consultaRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve realizar o cancelamento da consulta com sucesso")
    void deleteCenario1() {
        // Arrange
        DadosCancelamentoConsulta dados = createSampleDadosCancelamentoConsulta();
        Consulta consulta = createSampleConsulta(dados.idConsulta());
        when(consultaRepository.existsById(dados.idConsulta())).thenReturn(true);
        when(consultaRepository.getReferenceById(dados.idConsulta())).thenReturn(consulta);

        // Act
        ResponseEntity response = consultaServiceImpl.delete(dados);

        // Assert
        assertEquals(ResponseEntity.noContent().build(), response);

        verify(consultaRepository).getReferenceById(dados.idConsulta());
        verify(consultaRepository).existsById(dados.idConsulta());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar uma consulta inexistente")
    void deleteCenario2() {
        // Arrange
        DadosCancelamentoConsulta dados = createSampleDadosCancelamentoConsulta();
        when(consultaRepository.existsById(dados.idConsulta())).thenReturn(false);

        // Act & Assert
        assertThrows(ValidacaoException.class, () -> consultaServiceImpl.delete(dados));

        verify(validadoresCancelamento, never()).forEach(any());
        verify(consultaRepository).existsById(dados.idConsulta());
    }

    // MOCKS
    private void mockMedicoExistence(boolean medicoExists) {
        when(medicoRepository.existsById(1l)).thenReturn(medicoExists);
    }

    private void mockConsultaExistence(Long consultaId, Consulta consulta) {
        when(consultaRepository.findById(consultaId)).thenReturn(Optional.ofNullable(consulta));
    }

    private void mockConsultaPacienteByCpf(String cpf, Page<Consulta> consultas) {
        when(consultaRepository.getConsultaPacienteByCpf(cpf, Pageable.unpaged())).thenReturn(consultas);
    }

    // Método utilitário para criar um Page de Consulta fictícia
    private Page<Consulta> createSampleConsultaPage() {
        List<Consulta> consultas = new ArrayList<>();
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        consultas.add(new Consulta(1L, createSampleMedico(), createSamplePaciente(), proximaSegundaAs10, null, LocalDateTime.now()));
        return new PageImpl<>(consultas);
    }

    private Medico createSampleMedico() {
        return cadastrarMedico("Dr. Médico", "medico@test.com", "123456", Especialidade.CARDIOLOGIA);
    }

    private Paciente createSamplePaciente() {
        return cadastrarPaciente("Alice Paciente", "paciente@test.com", "987654321");
    }

    private DadosAgendamentoConsulta createSampleDadosAgendamentoConsulta() {
        return new DadosAgendamentoConsulta(1l, 2l, LocalDateTime.now().plusHours(1), Especialidade.CARDIOLOGIA);
    }

    private Consulta createSampleConsulta(Long consultaId) {
        Consulta consulta = new Consulta();
        consulta.setId(consultaId);

        Medico medico = createSampleMedico();
        medico.setId(2l);
        consulta.setMedico(medico);

        Paciente paciente = createSamplePaciente();
        paciente.setId(3l);
        consulta.setPaciente(paciente);

        LocalDateTime dataConsulta = LocalDateTime.now().plusHours(1);
        consulta.setData(dataConsulta);
        consulta.setDataHoraSolicitacaoAgendamento(LocalDateTime.now());

        return consulta;
    }

    // Método utilitário para verificar a resposta da entidade ResponseEntity
    private <T> void assertResponse(ResponseEntity<T> response, HttpStatus expectedStatus, T expectedBody) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new Medico(dadosMedico(nome, email, crm, especialidade));
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf) {
        return new Paciente(dadosCadastroPaciente(nome, email, cpf));
    }

    private DadosCancelamentoConsulta createSampleDadosCancelamentoConsulta() {
        return new DadosCancelamentoConsulta(1L, MotivoCancelamento.OUTROS);
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(
                nome,
                email,
                "19977772222",
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private DadosCadastroPaciente dadosCadastroPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                "19999990000",
                cpf,
                dadosEndereco()
        );
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "Rua xpto",
                "Vila Leopoudo",
                "00000000",
                "São Paulo",
                "SP",
                null,
                "1032"
        );
    }

}
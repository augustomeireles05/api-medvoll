package br.com.med.voll.api.infrastructure.integration.repository.medico;

import br.com.med.voll.api.domain.DadosEndereco;
import br.com.med.voll.api.domain.consulta.Consulta;
import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.domain.medico.Especialidade;
import br.com.med.voll.api.domain.medico.Medico;
import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.domain.paciente.Paciente;
import br.com.med.voll.api.infrastructure.integration.repository.consulta.ConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ConsultaRepositoryTest {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void setUp() {
        Medico medico = cadastrarMedico("Medico Teste", "medico@teste.com", "123456", Especialidade.CARDIOLOGIA);
        Paciente paciente = cadastrarPaciente("Paciente Teste", "paciente@teste.com", "11122233344");

        LocalDateTime dataConsulta1 = getNextFutureDateTime();
        LocalDateTime dataConsulta2 = getNextFutureDateTime();

        cadastrarConsulta(medico, paciente, dataConsulta1);
        cadastrarConsulta(medico, paciente, dataConsulta2);
    }

    @Test
    @DisplayName("Deve retornar consultas para o paciente com o CPF correspondente.")
    void getConsultaPacienteByCpfCenario1() {
        // Given
        String cpf = "11122233344";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "data"));

        // When
        Page<Consulta> consultas = consultaRepository.getConsultaPacienteByCpf(cpf, pageable);

        // Then
        assertThat(consultas).isNotEmpty(); // garanto que a lista de consultas não está vazia.
        assertThat(consultas.getTotalElements()).isEqualTo(2); // verifico se o nº total de elementos na tabela de consulta é igual a 2, dadas pelo setUp().
        assertThat(consultas.getContent()).hasSize(2); // verifica se o conteúdo da lista consultas tem tamanho igual a 2, dadas pelo setUp().
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há consultas para o paciente com o CPF correspondente.")
    void getConsultaPacienteByCpfCenario2() {
        // Given
        String cpf = "99988877766";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "data"));

        // When
        Page<Consulta> consultas = consultaRepository.getConsultaPacienteByCpf(cpf, pageable);

        // Then
        assertThat(consultas).isEmpty(); //verifica se a lista de consultas está vazia.
        assertThat(consultas.getTotalElements()).isEqualTo(0); // verifica se o nºtotal de elementos nas consultas é igual a zero, pois o cpf passado não se encontra na base.
    }

    /**
     * método que cria uma data sempre no futuro.
     * @return
     */
    private LocalDateTime getNextFutureDateTime() {
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        int hour = ThreadLocalRandom.current().nextInt(8, 18); // Between 8 AM and 5 PM
        int minute = ThreadLocalRandom.current().nextInt(0, 60);

        return nextMonday.atTime(hour, minute);
    }




    /**
     * Método que cadastra consulta na base de teste.
     * @param medico
     * @param paciente
     * @param data
     */
    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
        em.persist(new Consulta(null, medico, paciente, data, null, LocalDateTime.now()));
    }

    /**
     * Método que cadastra médico na base de teste.
     * @param nome
     * @param email
     * @param crm
     * @param especialidade
     * @return
     */
    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
        em.persist(medico);
        return medico;
    }

    /**
     * Método que cadastra paciente na base de teste.
     * @param nome
     * @param email
     * @param cpf
     * @return
     */
    private Paciente cadastrarPaciente(String nome, String email, String cpf) {
        var paciente = new Paciente(dadosCadastroPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }

    /**
     * Método que guarda o cadastro de médico para simulações em classes que necessitá-las.
     * @param nome
     * @param email
     * @param crm
     * @param especialidade
     * @return
     */
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

    /**
     * Método que guarda o cadastro de paciente para simulações em classes que necessitá-las.
     * @param nome
     * @param email
     * @param cpf
     * @return
     */
    private DadosCadastroPaciente dadosCadastroPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                "19999990000",
                cpf,
                dadosEndereco()
        );
    }

    /**
     * Método que guarda o cadastro de endereço para simulações em classes que necessitá-las.
     * @return
     */
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

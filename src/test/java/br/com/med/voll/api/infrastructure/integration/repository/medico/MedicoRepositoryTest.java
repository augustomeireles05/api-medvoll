package br.com.med.voll.api.infrastructure.integration.repository.medico;

import br.com.med.voll.api.domain.DadosEndereco;
import br.com.med.voll.api.domain.consulta.Consulta;
import br.com.med.voll.api.domain.medico.DadosCadastroMedico;
import br.com.med.voll.api.domain.medico.Especialidade;
import br.com.med.voll.api.domain.medico.Medico;
import br.com.med.voll.api.domain.paciente.DadosCadastroPaciente;
import br.com.med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @@AutoConfigureTestDatabase: Config que ensina ao spring utilizar o DB que de fato foi utilizado na aplicação.
 * @(replace = AutoConfigureTestDatabase.Replace.NONE) => não substitua as configurações do meu DB para utilizar algum DB em memória
 * @@ActiveProfiles("test"): Utiliza o application-test.yml. o "test" vem do próprio nome do arq application, i.e., é o -test
 * @criacaoDB em seguida, deve-se criar um database com o mesmo nome definido para a url do application-test.yml para que o test possa trabalhar em cima dessa nova db.
 *
 * @OBS: é importante frisar que ao final de todo run de métodos test, o spring automaticamente faz o rollback para que registros criados na base não interfira em outros métodos de teste.
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;


    @Test
    @DisplayName("Deve devolver null quando único médico cadastrado não está disponível na data.")
    void findRandomAvailableMedicoByDateCenario1() {
        // Given ou arrange
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        var medico = cadastrarMedico("Medico Teste", "medicoteste@email.com", "555123", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente Teste", "pacienteteste@email.com", "11122233344");
        cadastrarConsulta(medico, paciente, proximaSegundaAs10);

        // When ou act
        var medicoLivre = medicoRepository.findRandomAvailableMedicoByDate(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        // then ou assert
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deve devolver médico quando ele estiver disponível na data.")
    void findRandomAvailableMedicoByDateCenario2() {
        // Given ou arrange
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        Medico medico = cadastrarMedico("Medico Teste", "medicoteste@email.com", "555123", Especialidade.CARDIOLOGIA);

        // When ou act
        var medicoLivre = medicoRepository.findRandomAvailableMedicoByDate(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        // then ou assert
        assertThat(medicoLivre).isEqualTo(medico);
    }

    @Test
    @DisplayName("Deve devolver null quando médico da especialidade não estiver disponível na data.")
    void findRandomAvailableMedicoByDateCenario3() {
        // Given
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        Medico medico = cadastrarMedico("Medico Teste", "medicoteste@email.com", "555123", Especialidade.ORTOPEDIA);

        // When
        var medicoLivre = medicoRepository.findRandomAvailableMedicoByDate(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        // Then
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deve devolver médico quando ele estiver disponível na data, mas da especialidade errada.")
    void findRandomAvailableMedicoByDateCenario4() {
        // Given
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        Medico medicoCardiologista = cadastrarMedico("Medico Cardiologista", "medicocardio@email.com", "555124", Especialidade.CARDIOLOGIA);
        Paciente paciente = cadastrarPaciente("Paciente Teste", "pacienteteste@email.com", "11122233344");
        cadastrarConsulta(medicoCardiologista, paciente, proximaSegundaAs10);

        // When
        var medicoLivre = medicoRepository.findRandomAvailableMedicoByDate(Especialidade.ORTOPEDIA, proximaSegundaAs10);

        // Then
        assertThat(medicoLivre).isNotEqualTo(medicoCardiologista);
    }

    @Test
    @DisplayName("Deve devolver null quando único médico cadastrado está ocupado na data e especialidade correspondente.")
    void findRandomAvailableMedicoByDateCenario5() {
        // Given
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        var medicoCardiologista = cadastrarMedico("Medico Cardiologista", "medicocardio@email.com", "555124", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente Teste", "pacienteteste@email.com", "11122233344");
        var paciente2 = cadastrarPaciente("Paciente Teste 2", "pacienteteste2@email.com", "22222233344");
        cadastrarConsulta(medicoCardiologista, paciente, proximaSegundaAs10);
        cadastrarConsulta(medicoCardiologista, paciente2, proximaSegundaAs10);

        // When
        var medicoLivre = medicoRepository.findRandomAvailableMedicoByDate(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        // Then
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deve devolver null quando não há médicos disponíveis na data.")
    void findRandomAvailableMedicoByDateCenario6() {
        // Given
        var dataNoPassado = LocalDateTime.now().minusDays(1);

        // When
        var medicoLivre = medicoRepository.findRandomAvailableMedicoByDate(Especialidade.CARDIOLOGIA, dataNoPassado);

        // Then
        assertThat(medicoLivre).isNull();
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
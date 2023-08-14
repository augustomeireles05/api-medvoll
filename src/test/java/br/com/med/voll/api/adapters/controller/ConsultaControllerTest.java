package br.com.med.voll.api.adapters.controller;

import br.com.med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import br.com.med.voll.api.domain.medico.Especialidade;
import br.com.med.voll.api.usecase.consulta.ConsultaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static br.com.med.voll.api.utils.Constants.STANDARD_PATTERN_TIMESTAMP_WITHOUT_SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockBean
    private ConsultaService consultaService;

    @Test
    @DisplayName("Deve devolver código http 400 quando informações estão inválidas.")
    @WithMockUser("Considera para o spring security que estamos logados - evita erro 403.")
    void schedule_cenario1() throws Exception {

        //Disparo uma requisição para o endereço especificado via método post SEM LEVAR NENHUM BODY, pegue o resultado, pegue o response, jogue na variável response e verifico se status = status do bad request.
        MockHttpServletResponse response = mvc.perform(post("/consultas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * @OBS: podemos levar o json desenhando o mesmo através do .content("{}") e dentro das chaves passar o json. Entretanto não é recomendado pois é muito trabalhoso.
     * @.contentType(): notifica ao cabeçalho da requisição que estamos enviando um objeto json
     * @throws Exception
     */

    @Test
    @DisplayName("Deve devolver código http 200 quando informações estão válidas.")
    @WithMockUser
    void schedule_cenario2() throws Exception {
        var dataRequisicao = LocalDateTime.now().format(STANDARD_PATTERN_TIMESTAMP_WITHOUT_SECONDS);
        var dataConsulta = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;

        var responseEntity = ResponseEntity.ok(new DadosDetalhamentoConsulta(null, 10l, 1l, dataConsulta, dataRequisicao));
        when(consultaService.schedule(any())).thenReturn(responseEntity);

        var response = mvc
                .perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosAgendamentoConsultaJson.write(
                                        new DadosAgendamentoConsulta(10l, 1l, dataConsulta, especialidade)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                new DadosDetalhamentoConsulta(null, 10l, 1l, dataConsulta, dataRequisicao)
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}
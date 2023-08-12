package br.com.med.voll.api.adapters.config.springdoc;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    /**
     * Expoe um objeto do tipo OpenAPI. Será carregado pelo spring doc e seguirá com as configurações determinadas no return do método.
     * @return
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                .addSecuritySchemes("bearer-key",
                                    new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    )
                            )
                .info(new Info()
                    .title("DOCUMENTAÇÃO API VOLL MED")
                    .description("API Rest da aplicação Voll.med, contendo as funcionalidades de CRUD de médicos e de pacientes, além de agendamento, capturas e cancelamento de consultas")
                    .contact(new Contact()
                                .name("Contato do desenvolvedor")
                                .email("augustomeireles05@gmail.com"))
                        .license(new License()
                                .name("Github")
                                .url("https://github.com/augustomeireles05/api-medvoll")
                                )
                    );
    }
}

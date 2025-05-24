package com.broers.prueba_tecnica.helpers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(
                title = "DESARROLLO DE LA PRUEBA TECNICA",
                description = "API REST Backend",
                termsOfService = "",
                version = "1.0.0",
                contact = @Contact(
                        name = "Nilson Ruiz Marimoon",
                        url = "",
                        email = "www.linkedin.com/in/nilson-ruiz-ing2027"
                ),
                license = @License(
                        name = "Licencia Institucional",
                        url = "www.linkedin.com/in/nilson-ruiz-ing2027"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor de Desarrollo",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Servidor de Producción",
                        url = "http://academico.example.com"
                )
        },
        security = {
                @SecurityRequirement(name = "JWT")
        }
)
@SecurityScheme(
        name = "JWT",
        description = "JWT token de autenticación. Ejemplo: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
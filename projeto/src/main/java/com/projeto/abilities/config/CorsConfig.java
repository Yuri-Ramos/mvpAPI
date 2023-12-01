package com.projeto.abilities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {

      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowedOrigins("*");
      }
    };
  }
}

// Este código é uma configuração para habilitar o Cross-Origin Resource Sharing
// (CORS) em um projeto Spring Boot. O CORS é um mecanismo de segurança que
// restringe as solicitações de recursos de um domínio diferente do domínio que
// o recurso está hospedado.

// Aqui, uma classe de configuração chamada "CorsConfig" é criada com a anotação
// @Configuration, indicando que ela fornece configurações para o aplicativo
// Spring.

// Em seguida, um método chamado "corsConfigurer()" é criado, que retorna uma
// instância de "WebMvcConfigurer". Essa instância é usada para configurar o
// comportamento do CORS.

// No método "addCorsMappings", o "CorsRegistry" é usado para adicionar as
// configurações de CORS. O método "addMapping" especifica o padrão de URL a ser
// correspondido e as permissões para a solicitação. Neste exemplo, o padrão de
// URL é definido como "/*" para corresponder a todos os endpoints do
// aplicativo. Em seguida, as solicitações HTTP GET, POST, PUT e DELETE são
// permitidas por meio do método "allowedMethods". O cabeçalho de origem é
// permitido com "allowedHeaders", e qualquer origem é permitida com
// "allowedOrigins".

// Por fim, a anotação "@Bean" é usada para indicar que o método
// "corsConfigurer()" retorna um objeto que deve ser registrado como um bean no
// contexto do Spring.

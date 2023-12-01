package com.projeto.abilities.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.abilities.service.UserServices.UserDetailsServiceImplements;
import com.projeto.abilities.utils.AuthenticationEntryPointJwt;
import com.projeto.abilities.utils.AuthenticationTokenFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImplements userDetailsService;

    @Autowired
    private AuthenticationEntryPointJwt unauthorizedHandler;

    // Esse código define um bean chamado authenticationJwtTokenFilter, que retorna
    // uma nova instância de AuthenticationTokenFilter. Esse filtro é utilizado para
    // interceptar todas as requisições e verificar se o token de autenticação está
    // presente e é válido. Se o token for válido, o filtro adiciona o objeto
    // Authentication no contexto de segurança do Spring.
    @Bean
    public AuthenticationTokenFilter authenticationJwtTokenFilter() {
        return new AuthenticationTokenFilter();
    }

    // Este código cria um bean do tipo DaoAuthenticationProvider, que fornece a
    // implementação padrão do Spring Security para autenticação de usuários por
    // meio de um UserDetailsService e um codificador de senhas. Em seguida, o
    // método setUserDetailsService() define o serviço de detalhes do usuário e o
    // método setPasswordEncoder() define o codificador de senha. O bean é retornado
    // no final do método.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // Este método é responsável por criar e retornar uma instância do
    // AuthenticationManager, que é usado para autenticar usuários. Ele recebe uma
    // instância de AuthenticationConfiguration como parâmetro e chama o método
    // getAuthenticationManager() para obter o AuthenticationManager configurado.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // This function returns a new instance of the BCryptPasswordEncoder class,
    // which is a class that implements the PasswordEncoder interface.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/abilities").permitAll()
                .requestMatchers(HttpMethod.GET, "/abilities/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/signin").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/refreshtoken").permitAll()
                .requestMatchers("/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest()
                .authenticated().and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl()).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
                throws IOException, ServletException {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, Object> body = new HashMap<>();
            body.put("status", HttpServletResponse.SC_FORBIDDEN);
            body.put("error", "Forbidden");
            body.put("message", "Access Denied");
            body.put("path", request.getServletPath());

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        }
    }
}

// Este código define a configuração de segurança da aplicação. Ele configura um
// filtro de autenticação para processar os tokens JWT, define um provedor de
// autenticação que utiliza uma implementação personalizada de
// UserDetailsService e um encoder de senha BCrypt, e configura as permissões de
// acesso a diferentes URLs da API. Além disso, o código define uma classe
// interna que trata as exceções de acesso negado.
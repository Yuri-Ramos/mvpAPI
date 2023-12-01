package com.projeto.abilities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.abilities.dto.UserDTOs.LoginRequestDTO;
import com.projeto.abilities.dto.UserDTOs.RefreshTokenRequestDTO;
import com.projeto.abilities.dto.UserDTOs.RefreshTokenResponseDTO;
import com.projeto.abilities.dto.UserDTOs.RoleRequestDTO;
import com.projeto.abilities.dto.UserDTOs.SignupRegisterResponseDTO;
import com.projeto.abilities.dto.UserDTOs.SignupRequestDTO;
import com.projeto.abilities.dto.UserDTOs.SignupResponseDTO;
import com.projeto.abilities.dto.UserDTOs.UserLoggedResponseDTO;
import com.projeto.abilities.exception.AccountException;
import com.projeto.abilities.exception.ApiError;
import com.projeto.abilities.exception.RefreshTokenException;
import com.projeto.abilities.exception.UserException;
import com.projeto.abilities.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth", description = "Autentificação de usuario")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Este é um endpoint REST que autentica um usuário com base em um objeto de
    // requisição de login, retornando um token de acesso e um objeto de resposta
    // contendo informações do usuário autenticado em caso de sucesso, ou um erro
    // apropriado em caso de falha.
    @PostMapping("/signin")
    @Operation(summary = "Sign In Service", description = "Sign In Service", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Singned In!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignupResponseDTO.class))),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            SignupResponseDTO signupResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok().header("Authorization", signupResponse.getAccessToken()).body(signupResponse);
        } catch (AccountException e) {
            return ResponseEntity.unprocessableEntity().body(
                    new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getLocalizedMessage()));
        }
    }

    // Esse código define uma rota POST para registrar um novo usuário, validando o
    // corpo da requisição com a classe SignupRequestDTO e retornando uma resposta
    // 201 caso seja bem-sucedido ou um erro 422 caso contrário.
    @PostMapping("/signup")
    @Operation(summary = "Register In Service", description = "register In Service", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully Register In!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignupRegisterResponseDTO.class))),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
        try {
            SignupRegisterResponseDTO response = authService.registerUser(signUpRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.unprocessableEntity().body(
                    new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getLocalizedMessage()));
        }
    }

    // Essa é uma API protegida por autenticação de token e autorização de acesso
    // para usuários com perfil de administrador, onde é possível adicionar novas
    // roles a um usuário específico através de uma requisição PUT.
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/new-roles/{idUsuario}")
    @SecurityRequirement(name = "token")
    @Operation(summary = "Add news Roles", description = "add news Roles, Admin Only", responses = {
            @ApiResponse(responseCode = "201", description = "Roles Register In!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignupRegisterResponseDTO.class))),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "403", ref = "forbidden"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> newRoles(@Valid @RequestBody RoleRequestDTO rolesIn, @PathVariable Long idUsuario) {
        try {
            SignupRegisterResponseDTO response = authService.newRoles(rolesIn, idUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AccountException e) {
            return ResponseEntity.unprocessableEntity().body(
                    new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getLocalizedMessage()));
        }
    }

    // Este é um endpoint que permite a atualização do token de autenticação para um
    // novo período de validade, por meio do envio de um pedido contendo o token de
    // atualização.
    @PostMapping("/refreshtoken")
    @Operation(summary = "Refresh Token", description = "Refresh Token", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Refresh Token!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> refreshtoken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        try {
            RefreshTokenResponseDTO response = authService.refreshtoken(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Este é um endpoint que permite que um usuário faça logout do sistema. Ele
    // requer uma autenticação válida através do token JWT, e retorna uma resposta
    // HTTP com o código 200 caso o logout seja bem-sucedido.
    @PostMapping("/signout")
    @SecurityRequirement(name = "token")
    @Operation(summary = "Signout In Service", description = "Signout In Service", responses = {
            @ApiResponse(responseCode = "200", description = "Log out successful!", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<String> logoutUser() {
        return ResponseEntity.ok(authService.logoutUser());
    }

    // Essa é uma API que retorna informações do usuário logado, sendo acessível
    // tanto por usuários com a permissão "USER" quanto por aqueles com a permissão
    // "ADMIN". A resposta é um objeto UserLoggedResponseDTO em caso de sucesso ou
    // um objeto ApiError em caso de erro.
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/logged")
    @SecurityRequirement(name = "token")
    @Operation(summary = "Get Logged User", description = "Get Logged User", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully get User!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserLoggedResponseDTO.class))),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> findLoggedUser() {
        try {
            return ResponseEntity.ok(authService.findLoggedUser());
        } catch (UserException e) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity",
                            e.getLocalizedMessage()));
        }
    }

}

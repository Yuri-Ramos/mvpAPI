package com.projeto.abilities.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projeto.abilities.dto.UserAbilityDTOs.UserAbilityDTO;
import com.projeto.abilities.dto.UserAbilityDTOs.UserAbilityRequestDTO;
import com.projeto.abilities.exception.AbilityException;
import com.projeto.abilities.model.UserAbility;
import com.projeto.abilities.service.UserAbilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/userAbility")
public class UserAbilityController {

    @Autowired
    public UserAbilityService userAbilityService;

    @GetMapping
    @Operation(summary = "Search UserAbility", description = "Search UserAbility", responses = {
            @ApiResponse(responseCode = "200", description = "UserAbility found!"),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<List<UserAbilityDTO>> findAll() {
        return ResponseEntity.ok(userAbilityService.findAll());
    }

    @PostMapping()
    @SecurityRequirement(name = "token")
    @Operation(summary = "Create UserAbility", description = "Create UserAbility", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully UserAbility created!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAbilityRequestDTO.class))),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> insert(@Valid @RequestBody UserAbilityRequestDTO userAbility) {

        try {
            UserAbility us = userAbilityService.insertUserAbility(userAbility);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(us.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(userAbility);

        } catch (AbilityException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }

    }

    @PutMapping("{id}")
    @SecurityRequirement(name = "token")
    @Operation(summary = "Update UserAbility", description = "Update UserAbility", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully UserAbility updated!", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<UserAbilityDTO> update(@PathVariable Long id,
            @Valid @RequestBody UserAbility userAbility) {
        UserAbilityDTO userAbilityDTO = userAbilityService.updateUserAbility(id, userAbility);
        if (userAbilityDTO != null) {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(userAbilityDTO.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(userAbilityDTO);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("{id}")
    @SecurityRequirement(name = "token")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete UserAbility", description = "Delete UserAbility", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully UserAbility deleted!"),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        Boolean response = userAbilityService.delete(id);
        System.out.println(response);
        if (response != true) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}

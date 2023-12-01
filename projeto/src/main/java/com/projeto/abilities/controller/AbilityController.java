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

import com.projeto.abilities.dto.AbilityDTOs.AbilityRequestDTO;
import com.projeto.abilities.dto.AbilityDTOs.AbilityResponseDTO;
import com.projeto.abilities.exception.AbilityException;
import com.projeto.abilities.exception.ApiError;
import com.projeto.abilities.service.AbilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/abilities")
public class AbilityController {

    @Autowired
    private AbilityService abilityService;

    @GetMapping
    @Operation(summary = "Search Ability", description = "Search Ability", responses = {
            @ApiResponse(responseCode = "200", description = "Ability found!"),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<List<AbilityResponseDTO>> findAll() {
        return ResponseEntity.ok(abilityService.findAllAbilities());
    }

    @GetMapping("{id}")
    @Operation(summary = "Search Ability", description = "Search Ability", responses = {
            @ApiResponse(responseCode = "200", description = "Ability found!"),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> findById(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(abilityService.findAbilityById(id));
        } catch (AbilityException ex) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity",
                            ex.getLocalizedMessage()));
        }
    }

    @PostMapping()
    @SecurityRequirement(name = "token")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Ability", description = "Create Ability", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Ability created!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AbilityResponseDTO.class))),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> createAbility(@Valid @RequestBody AbilityRequestDTO abilityRequest) {

        try {
            AbilityResponseDTO response = abilityService.insertAbility(abilityRequest);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(response.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(response);
        } catch (AbilityException ex) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity",
                            ex.getLocalizedMessage()));
        }
    }

    @PutMapping("{id}")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Ability", description = "Update Ability", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Ability updated!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AbilityResponseDTO.class))),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<Object> updateAbility(@PathVariable Long id,
            @Valid @RequestBody AbilityRequestDTO abilityRequest) {

        try {
            AbilityResponseDTO response = abilityService.updateAbility(id, abilityRequest);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(response.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(response);
        } catch (AbilityException ex) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity",
                            ex.getLocalizedMessage()));
        }
    }

    @DeleteMapping("{id}")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Ability", description = "Delete Ability", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Ability deleted!"),
            @ApiResponse(responseCode = "400", ref = "BadRequest"),
            @ApiResponse(responseCode = "401", ref = "badcredentials"),
            @ApiResponse(responseCode = "422", ref = "unprocessableEntity"),
            @ApiResponse(responseCode = "500", ref = "internalServerError")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        Boolean response = abilityService.deleteAbility(id);
        if (response != true) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}

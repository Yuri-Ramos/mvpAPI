package com.projeto.abilities.dto.AbilityDTOs;

import com.projeto.abilities.model.Ability;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AbilityRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Version is required")
    private String version;
    @NotBlank(message = "Image Url is required")
    private String image_url;

    public AbilityRequestDTO(Ability ability) {
        this.name = ability.getName();
        this.version = ability.getVersion();
        this.description = ability.getDescription();
        this.image_url = ability.getImage_url();
    }
}

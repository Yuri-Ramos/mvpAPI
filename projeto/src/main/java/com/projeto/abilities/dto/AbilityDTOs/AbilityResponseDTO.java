package com.projeto.abilities.dto.AbilityDTOs;

import com.projeto.abilities.model.Ability;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AbilityResponseDTO {
    private Long id;

    private String name;

    private String version;

    private String description;

    private String image_url;

    public AbilityResponseDTO(Ability ability) {
        this.id = ability.getId();
        this.name = ability.getName();
        this.version = ability.getVersion();
        this.description = ability.getDescription();
        this.image_url = ability.getImage_url();
    }
}

package com.projeto.abilities.dto.UserAbilityDTOs;

import com.projeto.abilities.dto.AbilityDTOs.AbilityResponseDTO;
import com.projeto.abilities.model.UserAbility;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAbilityFindUserDTO {

    private Long id;
    private Integer knowledgeLevel;
    private AbilityResponseDTO ability;

    public UserAbilityFindUserDTO(UserAbility userAbility) {
        this.id = userAbility.getId();
        this.knowledgeLevel = userAbility.getKnowledgeLevel();
        this.ability = new AbilityResponseDTO(userAbility.getAbility());
    }
}

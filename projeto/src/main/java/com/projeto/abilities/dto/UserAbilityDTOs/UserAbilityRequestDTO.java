package com.projeto.abilities.dto.UserAbilityDTOs;

import com.projeto.abilities.model.UserAbility;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAbilityRequestDTO {
    private UserIdDTO user;
    private AbilityIdDTO ability;
    private int knowledgeLevel;

    public UserAbilityRequestDTO(UserAbility userAbility) {
        this.user = new UserIdDTO(userAbility.getUser());
        this.ability = new AbilityIdDTO(userAbility.getAbility());
        this.knowledgeLevel = userAbility.getKnowledgeLevel();
    }
}

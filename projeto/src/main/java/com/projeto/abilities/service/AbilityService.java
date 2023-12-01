package com.projeto.abilities.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.abilities.dto.AbilityDTOs.AbilityRequestDTO;
import com.projeto.abilities.dto.AbilityDTOs.AbilityResponseDTO;
import com.projeto.abilities.exception.AbilityException;
import com.projeto.abilities.model.Ability;
import com.projeto.abilities.repository.AbilityRepository;

import jakarta.transaction.Transactional;

@Service
public class AbilityService {

    @Autowired
    private AbilityRepository abilityRepository;

    public List<AbilityResponseDTO> findAllAbilities() {
        List<Ability> abilities = abilityRepository.findAll();
        List<AbilityResponseDTO> responses = new ArrayList<>();

        for (Ability ability : abilities) {
            responses.add(new AbilityResponseDTO(ability));
        }

        return responses;
    }

    public AbilityResponseDTO findAbilityById(Long id) {
        return abilityRepository.findById(id).map(AbilityResponseDTO::new)
                .orElseThrow(() -> new AbilityException("Could not find ability id = " + id));
    }

    @Transactional
    public AbilityResponseDTO insertAbility(AbilityRequestDTO ability) {

        if (abilityRepository.existsByNameIgnoreCase(ability.getName())) {
            throw new AbilityException("The ability already exists");
        }

        Ability abl = new Ability();
        abl.setName(ability.getName());
        abl.setDescription(ability.getDescription());
        abl.setImage_url(ability.getImage_url());
        abl.setVersion(ability.getVersion());

        abl = abilityRepository.save(abl);

        return new AbilityResponseDTO(abl);
    }

    @Transactional
    public AbilityResponseDTO updateAbility(Long id, AbilityRequestDTO ability) {

        Ability abl = abilityRepository.findById(id)
                .orElseThrow(() -> new AbilityException("Could not find ability id = " + id));

        String name = ability.getName();

        if (!abl.getName().equalsIgnoreCase(name) && abilityRepository.existsByNameIgnoreCase(name)) {
            throw new AbilityException("Name already exists for ability name = " + name);
        }

        abl.setName(name);
        abl.setDescription(ability.getDescription());
        abl.setImage_url(ability.getImage_url());
        abl.setVersion(ability.getVersion());

        abl = abilityRepository.save(abl);

        return new AbilityResponseDTO(abl);
    }

    @Transactional
    public boolean deleteAbility(Long id) {
        try {
            Ability ability = abilityRepository.findById(id)
                    .orElseThrow(() -> new AbilityException("Could not find ability id = " + id));

            abilityRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

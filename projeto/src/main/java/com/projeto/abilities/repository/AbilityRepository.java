package com.projeto.abilities.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.abilities.model.Ability;

public interface AbilityRepository extends JpaRepository<Ability, Long> {
    Boolean existsByNameIgnoreCase(String name);
}

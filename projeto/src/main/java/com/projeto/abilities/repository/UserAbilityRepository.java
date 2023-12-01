package com.projeto.abilities.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.abilities.model.UserAbility;

public interface UserAbilityRepository extends JpaRepository<UserAbility, Long> {

}

package com.projeto.abilities.dto.UserDTOs;

import java.time.Instant;

import com.projeto.abilities.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    private String password;

    private Instant lastLoginDate;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.lastLoginDate = user.getLastLoginDate();
    }
}
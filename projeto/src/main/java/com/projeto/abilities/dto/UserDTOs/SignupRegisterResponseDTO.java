package com.projeto.abilities.dto.UserDTOs;

import java.util.List;

import com.projeto.abilities.Enums.ERole;
import com.projeto.abilities.model.User;

import lombok.Data;

@Data
public class SignupRegisterResponseDTO {

    private Long id;
    private String username;
    private String email;
    private List<ERole> roles;

    public SignupRegisterResponseDTO(User u, List<ERole> roles2) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.roles = roles2;
    }
}

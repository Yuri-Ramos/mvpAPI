package com.projeto.abilities.service.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.abilities.model.User;
import com.projeto.abilities.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImplements implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImplements.build(user);
    }

}

// Este código é uma implementação do serviço UserDetailsService, que é usado
// pelo Spring Security para carregar detalhes do usuário durante a
// autenticação. Ele usa o repositório UserRepository para buscar um usuário
// pelo nome de usuário fornecido, cria um objeto UserDetails a partir do
// usuário encontrado e retorna esse objeto. A classe UserDetailsImplements é
// usada para construir o objeto UserDetails a partir do usuário retornado. Este
// método é transacional, garantindo que a transação seja gerenciada pelo Spring
// e que o estado do usuário não seja alterado. Se o usuário não for encontrado,
// é lançada uma exceção UsernameNotFoundException.

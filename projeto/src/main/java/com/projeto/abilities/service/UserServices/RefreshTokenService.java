package com.projeto.abilities.service.UserServices;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.projeto.abilities.exception.RefreshTokenException;
import com.projeto.abilities.model.RefreshToken;
import com.projeto.abilities.model.User;
import com.projeto.abilities.repository.RefreshTokenRepository;
import com.projeto.abilities.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {
    @Value("${projetoability.jwt.refresh.expiration}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * It returns an Optional of a RefreshToken object, which is found by the token
     * parameter
     * 
     * @param token The token that was sent to the client
     * @return An Optional object.
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * It creates a new refresh token for the user if the user doesn't have a
     * refresh token or if the
     * user's refresh token has expired
     * 
     * @param userId The user's id.
     * @return RefreshToken
     */
    public RefreshToken createRefreshToken(Long userId) {

        User user = userRepository.findById(userId).get();

        RefreshToken refreshTokenVeri = refreshTokenRepository.findByUserAndExpiryDateAfter(user, Instant.now());
        if (refreshTokenVeri != null) {
            return refreshTokenVeri;
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    /**
     * It deletes all refresh tokens associated with a user
     * 
     * @param userId The user id of the user whose refresh token you want to delete.
     * @return The number of rows affected by the delete operation.
     */
    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    /**
     * If the current time is after the expiry date of the token, delete the token
     * from the database.
     */
    @Transactional
    public void deleteAllRefreshTokensExpired() {
        List<RefreshToken> tokens = refreshTokenRepository.findAll();

        if (!tokens.isEmpty()) {
            Instant now = Instant.now();

            tokens.forEach(token -> {
                if (now.isAfter(token.getExpiryDate())) {
                    refreshTokenRepository.deleteById(token.getId());
                }
            });
        }

    }
}

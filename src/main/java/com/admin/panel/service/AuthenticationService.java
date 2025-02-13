package com.admin.panel.service;

import com.admin.panel.domain.model.AuthInfo;
import com.admin.panel.domain.model.AuthenticationResponse;
import com.admin.panel.domain.model.UserInfo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.admin.panel.domain.entity.UserEntity;
import com.admin.panel.domain.model.*;
import com.admin.panel.domain.repository.UserRepository;
import com.admin.panel.exceptions.UserNotFoundException;

import java.util.Date;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(AuthInfo user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);

        return buildAuthenticationResponse(savedUser);
    }

    public AuthenticationResponse login(AuthInfo userRequest) {
        authenticateUser(userRequest.getEmail(), userRequest.getPassword());

        UserEntity user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return buildAuthenticationResponse(user);
    }

    public String logout(String accessToken, String refreshToken) {
        Date accessTokenExpiry = jwtService.extractExpiration(accessToken);
        Date refreshTokenExpiry = jwtService.extractExpiration(refreshToken);

        jwtService.blacklistToken(accessToken, accessTokenExpiry);
        jwtService.blacklistToken(refreshToken, refreshTokenExpiry);

        return "Logout successful!";
    }

    public AuthenticationResponse refreshToken(Long loggedInUserId) {
        UserEntity user = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return buildAuthenticationResponse(user);
    }

    private void authenticateUser(String userEmail, String password) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), password)
        );
    }

    private AuthenticationResponse buildAuthenticationResponse(UserEntity user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setAdmin(user.isAdmin());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken, userInfo);
    }
}

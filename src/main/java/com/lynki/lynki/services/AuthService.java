package com.lynki.lynki.services;

import com.lynki.lynki.domain.User;
import com.lynki.lynki.domain.dtos.LoginRequestDTO;
import com.lynki.lynki.domain.dtos.LoginResponseDTO;
import com.lynki.lynki.domain.dtos.RegisterUserDTO;
import com.lynki.lynki.domain.enums.UserRole;
import com.lynki.lynki.exceptions.UserException;
import com.lynki.lynki.infra.TokenService;
import com.lynki.lynki.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, @Lazy AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    public void registerUser(RegisterUserDTO registerUser) {

        userValidation(registerUser);

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerUser.password());
        userRepository.save(new User(registerUser.username(),encryptedPassword, registerUser.email(), UserRole.USER));

    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) authentication.getPrincipal());

        return new LoginResponseDTO(token);

    }

    private void userValidation(RegisterUserDTO registerUser) {
        if (userRepository.findByUsername(registerUser.username()) != null) {
            throw new UserException.UsernameAlreadyExists(registerUser.username());
        }

        if (userRepository.findByEmail(registerUser.email()) != null) {
            throw new UserException.EmailAlreadyExists();
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }


}

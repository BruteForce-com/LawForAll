package com.bruteforce.userasaservice.service;

import com.bruteforce.userasaservice.dto.SignUpRequestDto;
import com.bruteforce.userasaservice.dto.SignUpResponseDto;
import com.bruteforce.userasaservice.model.User;
import com.bruteforce.userasaservice.repo.UserRepository;
import com.bruteforce.userasaservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }



    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        boolean isEmailExists = userRepository.existsUserByEmail(requestDto.getEmail());
        boolean isUsernameExists = userRepository.existsUserByuserName(requestDto.getUserName());


        if (isEmailExists || isUsernameExists) {
            throw new RuntimeException("Email or username already exists");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(encodedPassword);
        user.setFullName(requestDto.getFullName());
        user.setUserName(requestDto.getUserName());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setRole(requestDto.getRole());

        User savedUser = userRepository.save(user);
        
        // After registration, generate the token to return to the client
        String token = jwtService.generateToken(savedUser.getUserName());




    }
}

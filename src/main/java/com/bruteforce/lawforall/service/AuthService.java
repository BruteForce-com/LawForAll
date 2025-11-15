package com.bruteforce.lawforall.service;

import com.bruteforce.lawforall.dto.SignUpRequestDto;
import com.bruteforce.lawforall.dto.SignUpResponseDto;
import com.bruteforce.lawforall.model.User;
import com.bruteforce.lawforall.repo.UserRepository;
import com.bruteforce.lawforall.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        String token = jwtService.generateToken(savedUser);

        LocalDateTime createdAt = LocalDateTime.now();
        return new SignUpResponseDto(savedUser.getUserId(), savedUser.getUserName(), savedUser.getEmail(), token, createdAt);



    }
}

package com.bruteforce.lawforall.service;

import com.bruteforce.lawforall.Utils.DtoConverter;
import com.bruteforce.lawforall.dto.SignInRequestDto;
import com.bruteforce.lawforall.dto.SignUpRequestDto;
import com.bruteforce.lawforall.model.Role;
import com.bruteforce.lawforall.model.User;
import com.bruteforce.lawforall.repo.UserRepository;
import com.bruteforce.lawforall.security.JwtService;
import com.bruteforce.lawforall.security.UserPriciple;
import jakarta.validation.Valid;
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



    public String signUp(SignUpRequestDto requestDto) {

        // Check if email already exists
        if (userRepository.existsUserByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Check if the username already exists
        if (userRepository.existsUserByUsername(requestDto.getUserName())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

        // convert SignUpRequestDto to User
        User newUser = DtoConverter.convertSignUpRequestDtoToUser(requestDto, hashedPassword);

        // Save user to the database
        User savedUser = userRepository.save(newUser);

        // Generate and return JWT token
        return jwtService.generateToken(savedUser.getEmail());


    }


    public String signIn(SignInRequestDto requestDto) {

        // check the user exists or not
        if(requestDto.getUsernameOrEmail().contains("@")) {
            User user = userRepository.findByEmail(requestDto.getUsernameOrEmail());
            System.out.println(user);
            if(user == null) {
                throw new IllegalArgumentException("User not found");
            }
            if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }
            return jwtService.generateToken(user.getEmail());
        } else {
            User user = userRepository.findByUsername(requestDto.getUsernameOrEmail());
            if(user == null) {
                throw new IllegalArgumentException("User not found");
            }
            if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }
            return jwtService.generateToken(user.getUsername());
        }
    }
}

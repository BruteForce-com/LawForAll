package com.bruteforce.userasaservice.security;


import com.bruteforce.userasaservice.dto.UserRegistrationRequestDto;
import com.bruteforce.userasaservice.dto.UserRegistrationResponseDto;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    public UserRegistrationResponseDto registerUser(UserRegistrationRequestDto requestDto){
        try {
            return new UserRegistrationResponseDto();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}

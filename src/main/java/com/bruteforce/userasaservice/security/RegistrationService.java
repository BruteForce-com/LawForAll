package com.bruteforce.userasaservice.security;


import com.bruteforce.userasaservice.dto.UserRegistrationRequestDto;
import com.bruteforce.userasaservice.dto.UserRegistrationResponseDto;
import com.bruteforce.userasaservice.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {


    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);


    private final UserRepository userRepository;
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserRegistrationResponseDto registerUser(UserRegistrationRequestDto requestDto){
        try {
           boolean existsUserByuserName = userRepository.existsUserByuserName(requestDto.getUserName());
           if(existsUserByuserName){
               throw new RuntimeException("Username is already taken! or an existing user");
           }
           boolean existsUserByemail = userRepository.existsUserByEmail(requestDto.getEmail());
           if(existsUserByemail){
               throw new RuntimeException("Email is active with another account or an existing user");
           }


        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}

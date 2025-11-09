package com.bruteforce.userasaservice.control;

import com.bruteforce.userasaservice.dto.UserRegistrationRequestDto;
import com.bruteforce.userasaservice.dto.UserRegistrationResponseDto;
import com.bruteforce.userasaservice.security.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/auth")
public class RegistrationController {


    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }




    @PostMapping("/register")
    public ResponseEntity<?> registerUser( @Valid UserRegistrationRequestDto requestDto) {

        try{
            UserRegistrationResponseDto responseDto = registrationService.registerUser(requestDto);
            return ResponseEntity.ok(responseDto);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}

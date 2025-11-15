package com.bruteforce.lawforall.control;

import com.bruteforce.lawforall.dto.SignUpRequestDto;
import com.bruteforce.lawforall.dto.SignUpResponseDto;
import com.bruteforce.lawforall.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid SignUpRequestDto requestDto) {

        SignUpResponseDto responseDto = authService.signUp(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


    public ResponseEntity<?> signIn() {
        return null;
    }
}

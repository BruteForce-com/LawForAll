package com.bruteforce.lawforall.control;

import com.bruteforce.lawforall.dto.SignInRequestDto;
import com.bruteforce.lawforall.dto.SignUpRequestDto;
import com.bruteforce.lawforall.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {

        String generatedToken = authService.signUp(requestDto);
        return new ResponseEntity<>(generatedToken, HttpStatus.CREATED);
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequestDto requestDto) {

        String generatedToken = authService.signIn(requestDto);
        return new ResponseEntity<>(generatedToken, HttpStatus.OK);
    }
}

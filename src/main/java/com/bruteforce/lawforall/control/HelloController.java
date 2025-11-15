package com.bruteforce.lawforall.control;

import com.bruteforce.lawforall.security.UserPriciple;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal UserPriciple userPriciple) {
        String username = userPriciple.getUsername();
        System.out.println(username);

        return "Hello World!";
    }
}

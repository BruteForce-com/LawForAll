package com.bruteforce.userasaservice.security;

import com.bruteforce.userasaservice.model.User;
import com.bruteforce.userasaservice.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean isUserExists = userRepository.existsUserByuserName(username);
        boolean isEmailExists = userRepository.existsUserByEmail(username);
        if (!isUserExists || !isEmailExists) {
            throw new UsernameNotFoundException("User not found");
        }
        User user;
        if(username.contains("@"))
            user = userRepository.findByEmail(username);
        else
            user = userRepository.findByUserName(username);

        return new UserPriciple(user);
    }
}

package com.bruteforce.lawforall.security;

import com.bruteforce.lawforall.model.User;
import com.bruteforce.lawforall.repo.UserRepository;
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
        // First, try to find the user by username or email
        User user = username.contains("@")
                ? userRepository.findByEmail(username)
                : userRepository.findByUsername(username);

        // If user not found, throw an exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username/email: " + username);
        }

        return new UserPriciple(user);
    }
}

package com.bruteforce.userasaservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bruteforce.userasaservice.model.User;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsUserByuserName(String userName);
    boolean existsUserByEmail(String email);
    User findByUserName(String userName);
    User findByEmail(String email);
}

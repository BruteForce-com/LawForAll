package com.bruteforce.lawforall.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bruteforce.lawforall.model.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsUserByUsername(String userName);
    boolean existsUserByEmail(String email);
    User findByUsername(String userName);
    User findByEmail(String email);
}

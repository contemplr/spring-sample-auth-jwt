package com.auth.example.services.repositories;


import com.auth.example.entites.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select (count(u) > 0) from users u where u.email = ?1")
    boolean existsByEmailAddressOrPhoneNum(String username);

    @Query("select u from users u where u.email = ?1 and u.enabled is true")
    Optional<User> findUsernameByEmailAddressOrPhoneNum(String username);

    @Query("select u from users u where u.username = ?1")
    Optional<User> findByUsername(String username);
}

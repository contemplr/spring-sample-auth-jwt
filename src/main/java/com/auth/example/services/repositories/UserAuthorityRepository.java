package com.auth.example.services.repositories;

import com.auth.example.entites.UserAuthority;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserAuthorityRepository extends CrudRepository<UserAuthority, Long> {
    Set<UserAuthority> findAllByUserId(long id);
}

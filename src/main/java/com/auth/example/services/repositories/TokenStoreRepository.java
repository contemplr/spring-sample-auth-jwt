package com.auth.example.services.repositories;

import com.auth.example.entites.TokenStore;
import org.springframework.data.repository.CrudRepository;

public interface TokenStoreRepository extends CrudRepository<TokenStore, Long> {
}

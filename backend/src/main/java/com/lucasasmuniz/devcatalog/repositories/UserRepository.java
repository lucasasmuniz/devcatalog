package com.lucasasmuniz.devcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucasasmuniz.devcatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
}

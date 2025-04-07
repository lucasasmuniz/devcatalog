package com.lucasasmuniz.devcatalog.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lucasasmuniz.devcatalog.entities.PasswordRecover;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long>{
	
	@Query("SELECT obj FROM PasswordRecover obj WHERE obj.token = :token AND obj.expirationDate > :now")
	List<PasswordRecover> searchValidTokens(String token, Instant now);
}

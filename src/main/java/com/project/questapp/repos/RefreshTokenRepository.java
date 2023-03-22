package com.project.questapp.repos;
import com.project.questapp.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
	RefreshToken findByUserId(Long userId);
}

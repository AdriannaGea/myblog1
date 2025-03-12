package org.wcs.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wcs.myblog.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

package dev.delfi.chatapp.chatappbackend.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findUserById(Long id);
    boolean existsByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = 'root'")
    User getRoot();
}

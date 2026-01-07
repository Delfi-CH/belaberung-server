package dev.delfi.chatapp.chatappbackend.model.meta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LongtermMetadataRepository extends JpaRepository<LongtermMetadata, Long> {
    Optional<LongtermMetadata> findByName(String name);
}

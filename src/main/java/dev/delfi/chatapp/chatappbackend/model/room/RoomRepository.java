package dev.delfi.chatapp.chatappbackend.model.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findRoomById(Long id);
    Optional<Room> findRoomByName(String name);
    boolean existsByName(String name);
}

package dev.delfi.chatapp.chatappbackend.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findRoomById(Long id);
    Room findRoomByName(String name);
    boolean existsByName(String name);
}

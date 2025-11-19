package dev.delfi.chatapp.chatappbackend.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUserID(long userID);
    Message findMessageById(Long id);
    List<Message> findByRoomID(Long roomID);
}

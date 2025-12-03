package dev.delfi.chatapp.chatappbackend.model.message;

import dev.delfi.chatapp.chatappbackend.model.room.Room;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findByUser(User user);
    Optional<Message> findMessageById(Long id);
    Optional<List<Message>> findByRoom(Room room);
}

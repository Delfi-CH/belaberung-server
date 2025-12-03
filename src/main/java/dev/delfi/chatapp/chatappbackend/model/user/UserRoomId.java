package dev.delfi.chatapp.chatappbackend.model.user;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRoomId implements Serializable {

    private Long userId;
    private Long roomId;

    public UserRoomId() {}

    public UserRoomId(Long userId, Long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoomId that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roomId);
    }

    // getters & setters
}

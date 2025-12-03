package dev.delfi.chatapp.chatappbackend.model.user;

import dev.delfi.chatapp.chatappbackend.model.room.Room;
import dev.delfi.chatapp.chatappbackend.model.room.RoomRole;
import dev.delfi.chatapp.chatappbackend.model.room.RoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserRoom {

    @EmbeddedId
    private UserRoomId id = new UserRoomId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    private RoomRole role;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    public UserRoom() {}

    public UserRoom(User user, Room room, RoomRole role, RoomStatus status) {
        this.user = user;
        this.room = room;
        this.role = role;
        this.status = status;
        this.id = new UserRoomId(user.getId(), room.getId());
    }
}

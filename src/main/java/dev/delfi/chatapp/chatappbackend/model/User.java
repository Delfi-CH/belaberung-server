package dev.delfi.chatapp.chatappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    @Column(unique = true, nullable = false)
    private String username;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "user-messages")
    @JsonProperty("messages")
    private List<Message> messages = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    @JsonBackReference(value = "room-users")
    @JsonProperty("rooms")
    private List<Room> joinedRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "bannedUsers")
    @JsonBackReference(value = "room-banned")
    @JsonProperty("banned_rooms")
    private List<Room> bannedRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "roomAdmins")
    @JsonBackReference(value = "room-admins")
    @JsonProperty("managed_rooms")
    private List<Room> managedRooms = new ArrayList<>();

    @OneToMany(mappedBy = "roomRoot")
    @JsonBackReference(value = "room-owner")
    @JsonProperty("owned_rooms")
    private List<Room> ownedRooms = new ArrayList<>();

    public User() {}

    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            messages.add(message);
            message.setUser(this);
        }
    }

    public void deleteMessage(Message message) {
        if (messages.contains(message)) {
            messages.remove(message);
            message.setUser(null);
        }
    }

    public void joinRoom(Room room) {
        if (!joinedRooms.contains(room)) {
            joinedRooms.add(room);
            if (!room.getUsers().contains(this)) {
                room.getUsers().add(this);
            }
        }
    }

    public void leaveRoom(Room room) {
        if (joinedRooms.contains(room)) {
            joinedRooms.remove(room);
            if (room.getUsers().contains(this)) {
                room.getUsers().remove(this);
            }
        }
    }

    public void addManagedRoom(Room room) {
        if (!managedRooms.contains(room)) {
            managedRooms.add(room);
            if (!room.getRoomAdmins().contains(this)) {
                room.getRoomAdmins().add(this);
            }
        }
    }

    public void stopManagingRoom(Room room) {
        if (managedRooms.contains(room)) {
            managedRooms.remove(room);
            if (room.getRoomAdmins().contains(this)) {
                room.getRoomAdmins().remove(this);
            }
        }
    }

    public void addOwnedRoom(Room room) {
        if (!ownedRooms.contains(room)) {
            ownedRooms.add(room);
            room.setRoomRoot(this);
        }
    }

    public void stopOwningRoom(Room room, User newRoot) {
        if (ownedRooms.contains(room)) {
            ownedRooms.remove(room);
            if (newRoot != null) {
                room.setRoomRoot(newRoot);
                if (!newRoot.getOwnedRooms().contains(room)) {
                    newRoot.getOwnedRooms().add(room);
                }
            } else {
                room.setRoomRoot(null);
            }
        }
    }

    public void addBannedRoom(Room room) {
        if (!bannedRooms.contains(room)) {
            bannedRooms.add(room);
            if (!room.getBannedUsers().contains(this)) {
                room.getBannedUsers().add(this);
            }
        }
    }

    public void removeBannedRoom(Room room) {
        if (bannedRooms.contains(room)) {
            bannedRooms.remove(room);
            if (room.getBannedUsers().contains(this)) {
                room.getBannedUsers().remove(this);
            }
        }
    }
}

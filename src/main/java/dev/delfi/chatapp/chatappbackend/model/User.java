package dev.delfi.chatapp.chatappbackend.model;

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
    @JsonProperty("messages")
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JsonProperty("rooms")
    private List<Room> joinedRooms= new ArrayList<>();

    @ManyToMany
    @JsonProperty("banned_rooms")
    private List<Room> bannedRooms = new ArrayList<>();

    @ManyToMany
    @JsonProperty("managed_rooms")
    private List<Room> managedRooms = new ArrayList<>();

    @OneToMany(mappedBy = "roomRoot")
    @JsonProperty("owned_rooms")
    private List<Room> ownedRooms = new ArrayList<>();

    public User() {}

    public void addMessage(Message message) {
        this.messages.add(message);
        message.setUser(this);
    }
    public void joinRoom(Room room) {
        this.joinedRooms.add(room);
        room.addUser(this);
    }
    public void addManagedRoom(Room room) {
        this.managedRooms.add(room);
        room.promoteToAdminUser(this);
    }
    public void addOwnedRoom(Room room) {
        this.ownedRooms.add(room);
        room.setRoomRoot(this);
    }
    public void addBannedRoom(Room room) {
        this.bannedRooms.add(room);
        room.banUser(this);
    }
    public void deleteMessage(Message message) {
        this.messages.remove(message);
        message.setUser(null);
    }
    public void leaveRoom(Room room) {
        this.joinedRooms.remove(room);
        room.removeUser(this);
    }
    public void stopManagingRoom(Room room) {
        this.managedRooms.remove(room);
        room.demoteRoomAdmin(this);
    }
    public void stopOwningRoom(Room room, User newRoot) {
        this.ownedRooms.remove(room);
        room.setRoomRoot(newRoot);
    }
    public void removeBannedRoom(Room room) {
        this.bannedRooms.remove(room);
        room.unbanUser(this);
    }
}

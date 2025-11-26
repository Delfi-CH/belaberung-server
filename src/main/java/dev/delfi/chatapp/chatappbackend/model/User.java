package dev.delfi.chatapp.chatappbackend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Getter
@Setter
@Entity
@NoArgsConstructor
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

    @ManyToMany(mappedBy = "users")
    @JsonProperty("rooms")
    private List<Room> joinedRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "bannedUsers")
    @JsonProperty("banned_rooms")
    private List<Room> bannedRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "roomAdmins")
    @JsonProperty("managed_rooms")
    private List<Room> managedRooms = new ArrayList<>();

    @OneToMany(mappedBy = "roomRoot")
    @JsonProperty("owned_rooms")
    private List<Room> ownedRooms = new ArrayList<>();

    public User(String username, String domain, String password, List<Message> messages, List<Room> joinedRooms, List<Room> bannedRooms, List<Room> managedRooms, List<Room> ownedRooms) {
        this.username = username;
        this.domain = domain;
        this.password = password;
        this.messages = messages;
        this.joinedRooms = joinedRooms;
        this.bannedRooms = bannedRooms;
        this.managedRooms = managedRooms;
        this.ownedRooms = ownedRooms;
    }

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

    public void leaveEveryRoom(User root) {
        for (Room room : ownedRooms) {
            stopOwningRoom(room, root);
        }
        for (Room room : joinedRooms) {
            stopManagingRoom(room);
            leaveRoom(room);
        }
    }
}

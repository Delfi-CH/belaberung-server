package dev.delfi.chatapp.chatappbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "room_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    @JsonProperty("users")
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "banned_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    @JsonProperty("banned")
    private List<User> bannedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonProperty("messages")
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "room_admins",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    @JsonProperty("room_admins")
    private List<User> roomAdmins = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "room_root_id")
    @JsonManagedReference
    @JsonProperty("room_root")
    private User roomRoot;

    @JsonProperty("is_public")
    private boolean isPublic;

    @JsonProperty("is_directmessage")
    private boolean isDirectMessage;

    @JsonProperty("max_users")
    private Long maxUsers = 10L;

    public Room() {}

    public void addUser(User user) {
        if (!users.contains(user) && users.size() < maxUsers && !bannedUsers.contains(user)) {
            users.add(user);
            if (!user.getJoinedRooms().contains(this)) {
                user.getJoinedRooms().add(this);
            }
        }
    }

    public void removeUser(User user) {
        if (user != roomRoot) {
            users.remove(user);
            roomAdmins.remove(user);
            if (user.getJoinedRooms().contains(this)) {
                user.getJoinedRooms().remove(this);
            }
            if (user.getManagedRooms().contains(this)) {
                user.getManagedRooms().remove(this);
            }
        }
    }

    public void banUser(User user) {
        if (user != roomRoot) {
            removeUser(user);
            if (!bannedUsers.contains(user)) {
                bannedUsers.add(user);
            }
            if (!user.getBannedRooms().contains(this)) {
                user.getBannedRooms().add(this);
            }
        }
    }

    public void unbanUser(User user) {
        bannedUsers.remove(user);
        if (user.getBannedRooms().contains(this)) {
            user.getBannedRooms().remove(this);
        }
    }

    public void promoteToAdminUser(User user) {
        if (!roomAdmins.contains(user) && users.contains(user)) {
            roomAdmins.add(user);
            if (!user.getManagedRooms().contains(this)) {
                user.getManagedRooms().add(this);
            }
        }
    }

    public void demoteRoomAdmin(User user) {
        if (roomAdmins.contains(user)) {
            roomAdmins.remove(user);
            if (user.getManagedRooms().contains(this)) {
                user.getManagedRooms().remove(this);
            }
        }
    }

    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            messages.add(message);
            message.setRoom(this);
        }
    }

    public void removeMessage(Message message) {
        if (messages.contains(message)) {
            messages.remove(message);
            message.setRoom(null);
        }
    }
}

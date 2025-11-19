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
    @JsonProperty("users")
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "banned_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonProperty("banned")
    private List<User> bannedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("messages")
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "room_admins",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonProperty("room_admins")
    private List<User> roomAdmins = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "room_root_id")
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
        if (!this.users.contains(user) && this.users.size() < maxUsers && !this.bannedUsers.contains(user)) {
            this.users.add(user);
            user.joinRoom(this);
        }
    }

    public void unbanUser(User user) {
        this.bannedUsers.remove(user);
        user.removeBannedRoom(this);
    }

    public void promoteToAdminUser(User user) {
        if (!this.roomAdmins.contains(user) && this.users.contains(user)) {
            this.roomAdmins.add(user);
            user.addManagedRoom(this);
        }
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        message.setRoom(this);
    }

    public void removeUser(User user) {
        if (user != roomRoot) {
            this.roomAdmins.remove(user);
            this.users.remove(user);
            user.stopManagingRoom(this);
            user.leaveRoom(this);
        }
    }
    public void banUser(User user) {
        if (user != roomRoot) {
            this.roomAdmins.remove(user);
            this.users.remove(user);
            bannedUsers.add(user);
            user.stopManagingRoom(this);
            user.leaveRoom(this);
            user.addBannedRoom(this);
        }
    }

    public void demoteRoomAdmin(User user) {
        if (this.roomAdmins.contains(user)) {
            this.roomAdmins.remove(user);
            user.stopManagingRoom(this);
        }
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
        message.setRoom(null);
    }
}

package dev.delfi.chatapp.chatappbackend.model.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.delfi.chatapp.chatappbackend.model.message.Message;
import dev.delfi.chatapp.chatappbackend.model.room.Room;
import dev.delfi.chatapp.chatappbackend.model.room.RoomRole;
import dev.delfi.chatapp.chatappbackend.model.room.RoomStatus;
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
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("messages")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> memberships = new ArrayList<>();

    @JsonProperty("roles")
    private String roles;

    public User(String username, String domain, String password, List<Message> messages) {
        this.username = username;
        this.domain = domain;
        this.password = password;
        this.messages = messages;
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

    public void addRoom(Room room, RoomRole role) {
        UserRoom membership = new UserRoom(this, room, role, RoomStatus.JOINED);
        memberships.add(membership);
        room.getMembers().add(membership);
    }

    public void removeRoom(Room room) {
        memberships.removeIf(ur -> ur.getRoom().equals(room));
        room.getMembers().removeIf(ur -> ur.getUser().equals(this));
    }
}
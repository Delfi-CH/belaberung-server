package dev.delfi.chatapp.chatappbackend.model.room;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.delfi.chatapp.chatappbackend.model.message.Message;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)

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

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("messages")
    private List<Message> messages = new ArrayList<>();

    @JsonProperty("is_public")
    private boolean isPublic;

    @JsonProperty("is_directmessage")
    private boolean isDirectMessage;

    @JsonProperty("max_users")
    private Long maxUsers = 10L;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> members = new ArrayList<>();

    public Room() {}

    public List<User> getUsersByRole(RoomRole role) {
        return members.stream()
                .filter(ur -> ur.getRole() == role && ur.getStatus() == RoomStatus.JOINED)
                .map(UserRoom::getUser)
                .toList();
    }

    public List<User> getBannedUsers() {
        return members.stream()
                .filter(ur -> ur.getStatus() == RoomStatus.BANNED)
                .map(UserRoom::getUser)
                .toList();
    }

    public User getOwner() {
        return members.stream()
                .filter(ur -> ur.getRole() == RoomRole.OWNER)
                .map(UserRoom::getUser)
                .findFirst()
                .orElse(null);
    }

    public List<User> getModerators() {
        return getUsersByRole(RoomRole.MODERATOR);
    }

    public List<User> getRegularUsers() {
        return getUsersByRole(RoomRole.USER);
    }

}

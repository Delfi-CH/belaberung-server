package dev.delfi.chatapp.chatappbackend.model.message;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.delfi.chatapp.chatappbackend.model.room.Room;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Getter
@Setter
@Entity
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonProperty("room")
    private Room room;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty("user")
    private User user;

    @JsonProperty("timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Long timestamp;

    @JsonProperty("content")
    @Column(nullable = false)
    private String content;

    public Message() {}
}

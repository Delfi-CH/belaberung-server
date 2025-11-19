package dev.delfi.chatapp.chatappbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
    private String name;

    @ManyToMany
    @JsonProperty("users")
    private List<User> users;

    @OneToMany
    @JsonProperty("messages")
    private List<Message> messages;

    public Room() {

    }
}

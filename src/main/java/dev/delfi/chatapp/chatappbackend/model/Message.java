package dev.delfi.chatapp.chatappbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("room")
    @ManyToOne
    private Room room;

    @JsonProperty("user")
    @ManyToOne
    private User user;

    @JsonProperty("timestamp")
    private Date timestamp;

    @JsonProperty("content")
    private String content;

    public Message() {

    }
}

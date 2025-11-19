package dev.delfi.chatapp.chatappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference(value = "room-messages")
    @JsonProperty("room")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-messages")
    @JsonProperty("user")
    private User user;

    @JsonProperty("timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @JsonProperty("content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    @JsonBackReference(value = "message-reply")
    @JsonProperty("reply")
    private Message replyMessage;

    public Message() {}
}

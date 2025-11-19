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
    @JoinColumn(name = "room_id")
    private Room room;

    @JsonProperty("user")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonProperty("timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @JsonProperty("content")
    private String content;

    @JsonProperty("reply")
    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Message replyMessage;

    public Message() {

    }
}

package dev.delfi.chatapp.chatappbackend.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponseItem {
    private Long id;
    private Long timestamp;
    private String content;
    private String username;
    private String userdomain;
}

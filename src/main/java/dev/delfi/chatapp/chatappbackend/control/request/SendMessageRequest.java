package dev.delfi.chatapp.chatappbackend.control.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {
    private String content;
    private String username;
}

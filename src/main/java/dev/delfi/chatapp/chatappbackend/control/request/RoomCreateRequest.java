package dev.delfi.chatapp.chatappbackend.control.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomCreateRequest {
    private String name;
    private boolean isPublic;
    private boolean isDirectMessage;
    private Long maxUsers = 10L;
    private Long ownerUserId;
}

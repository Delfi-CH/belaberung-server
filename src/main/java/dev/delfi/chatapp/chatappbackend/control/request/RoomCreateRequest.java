package dev.delfi.chatapp.chatappbackend.control.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomCreateRequest {
    private String name;

    @JsonProperty("max_users")
    private Long maxUsers = 10L;

    @JsonProperty("owner_user_id")
    private Long ownerUserId;
}


package dev.delfi.chatapp.chatappbackend.control.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateRequest {

    @JsonProperty("old_password")
    public String oldPassword;
    @JsonProperty("new_password")
    public String newPassword;
}

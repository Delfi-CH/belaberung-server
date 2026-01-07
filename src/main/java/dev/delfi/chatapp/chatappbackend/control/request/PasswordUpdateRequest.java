package dev.delfi.chatapp.chatappbackend.control.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordUpdateRequest {

    @JsonProperty("old_password")
    public String oldPassword;
    @JsonProperty("new_password")
    public String newPassword;
}

package dev.delfi.chatapp.chatappbackend.control;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    public String username;
    public String password;
}

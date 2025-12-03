package dev.delfi.chatapp.chatappbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyExistsExecption extends RuntimeException {
    public UsernameAlreadyExistsExecption(String message) {
        super(message);
    }
}
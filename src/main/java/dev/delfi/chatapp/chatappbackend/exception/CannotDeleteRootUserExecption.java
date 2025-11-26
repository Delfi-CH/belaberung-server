package dev.delfi.chatapp.chatappbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CannotDeleteRootUserExecption extends RuntimeException {
    public CannotDeleteRootUserExecption(String message) {
        super(message);
    }
}

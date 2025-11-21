package dev.delfi.chatapp.chatappbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoomHasToManyUsersExecption extends RuntimeException {
    public RoomHasToManyUsersExecption(String message) {
        super(message);
    }
}
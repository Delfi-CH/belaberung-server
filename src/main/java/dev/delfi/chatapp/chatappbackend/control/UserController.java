package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.control.request.PasswordUpdateRequest;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/chat/user", produces = "application/json")
public class UserController {

    private final UserService service;


    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
    @GetMapping("/id/{id}")
    public User getUser(@PathVariable Long id) {
        return service.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    @GetMapping("/name/{username}")
    public User getUser(@PathVariable String username) {
        return service.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.delete(id);
    }
    @PutMapping("/{id}/name")
    public void updatePassword(@PathVariable Long id, @RequestBody String username) {
        service.updateUsername(id, username);
    }
    @PutMapping("/{id}/password")
    public void updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest updateRequest) {
        service.updatePassword(id, updateRequest.oldPassword, updateRequest.newPassword);
    }
}

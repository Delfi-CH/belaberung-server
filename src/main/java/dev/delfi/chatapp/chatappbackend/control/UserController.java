package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.control.request.PasswordUpdateRequest;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        List<User> tmp = service.getAllUsers();
        for (User tmpusr: tmp) {
            tmpusr.setPassword("no");
        }
        return tmp;
    }
    @GetMapping("/id/{id}")
    public User getUser(@PathVariable Long id) {
        User tmp = service.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        tmp.setPassword("no");
        return tmp;
    }
    @GetMapping("/name/{username}")
    public User getUser(@PathVariable String username) {
        User tmp = service.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        tmp.setPassword("no");
        return tmp;
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

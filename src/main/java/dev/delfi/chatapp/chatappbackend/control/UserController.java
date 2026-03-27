package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.control.request.PasswordUpdateRequest;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the users
 * @author Delfi-CH
 * @version 1
 * */
@RestController
@RequestMapping(value = "/api/chat/user", produces = "application/json")
public class UserController {

    private final UserService service;

    /**
     * Creates the Service
     * @param service the Service
     * */
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Gets all Users
     * @return List of all Users
     * */
    @GetMapping
    public List<User> getAllUsers() {
        List<User> tmp = service.getAllUsers();
        for (User tmpusr: tmp) {
            tmpusr.setPassword("no");
        }
        return tmp;
    }

    /**
     * Get a specific User by ID
     * @param id Id of the requested User
     * @return a singular User
     * */
    @GetMapping("/id/{id}")
    public User getUser(@PathVariable Long id) {
        User tmp = service.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        tmp.setPassword("no");
        return tmp;
    }

    /**
     * Get a specific User by Username
     * @param username Name of the requested User
     * @return a singular User
     * */
    @GetMapping("/name/{username}")
    public User getUser(@PathVariable String username) {
        User tmp = service.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        tmp.setPassword("no");
        return tmp;
    }

    /**
     * Delete a User by ID
     * @param id Id of the User
     * */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.delete(id);
    }

    /**
     * Update a users Username
     * @param id Id of the User
     * @param username New Username
     * */
    @PutMapping("/{id}/name")
    public void updatePassword(@PathVariable Long id, @RequestBody String username) {
        service.updateUsername(id, username);
    }

    /**
     * Update a users Password
     * @param id Id of the User
     * @param updateRequest JSON-Data containing the users current and new password
     * */
    @PutMapping("/{id}/password")
    public void updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest updateRequest) {
        service.updatePassword(id, updateRequest.oldPassword, updateRequest.newPassword);
    }
}

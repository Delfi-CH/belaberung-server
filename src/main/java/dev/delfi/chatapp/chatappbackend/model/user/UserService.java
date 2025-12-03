package dev.delfi.chatapp.chatappbackend.model.user;

import dev.delfi.chatapp.chatappbackend.config.ChatappConfig;
import dev.delfi.chatapp.chatappbackend.control.request.RegistrationRequest;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.UsernameAlreadyExistsExecption;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private String domain;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ChatappConfig config) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.domain = config.getDomain();
    }



    public void register(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.username)) {
            throw new UsernameAlreadyExistsExecption("Username taken!");
        }

        User user = new User(request.username, domain, passwordEncoder.encode(request.password), new ArrayList<>());
        userRepository.save(user);
    }

    public void delete(Long id) {
        User user = userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if  (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password doesn't match");
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    public void updateUsername(Long id, String username) {
        User user = userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(username);
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getRoot() {
        return userRepository.getRoot();
    }
}

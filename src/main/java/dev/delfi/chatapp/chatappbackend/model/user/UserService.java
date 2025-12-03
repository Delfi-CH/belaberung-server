package dev.delfi.chatapp.chatappbackend.model.user;

import dev.delfi.chatapp.chatappbackend.config.ChatappConfig;
import dev.delfi.chatapp.chatappbackend.control.RegistrationRequest;
import dev.delfi.chatapp.chatappbackend.model.room.RoomRepository;
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
            throw new RuntimeException("Username already taken");
        }

        User user = new User(request.username, domain, passwordEncoder.encode(request.password), new ArrayList<>());
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

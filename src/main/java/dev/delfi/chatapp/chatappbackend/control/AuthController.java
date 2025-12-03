package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.auth.JsonWebTokenUtils;
import dev.delfi.chatapp.chatappbackend.control.request.LoginRequest;
import dev.delfi.chatapp.chatappbackend.control.request.RegistrationRequest;
import dev.delfi.chatapp.chatappbackend.model.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JsonWebTokenUtils jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authManager, JsonWebTokenUtils jwtUtil, UserService userService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return jwtUtil.generateToken(request.getUsername());
    }

    @PostMapping("/register")
    public String register(@RequestBody RegistrationRequest request) {
        userService.register(request);
        return "User registered successfully!";
    }
}

package dev.delfi.chatapp.chatappbackend;

import dev.delfi.chatapp.chatappbackend.config.ChatappConfig;
import dev.delfi.chatapp.chatappbackend.model.User;
import dev.delfi.chatapp.chatappbackend.model.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class ChatappBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatappBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ChatappBackendApplication application, UserRepository userRepository) {
        return args -> {
            if(userRepository.findByUsername("root").isPresent()) return;
            userRepository.save(new User("root", "root", "1234", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        };
    }
}

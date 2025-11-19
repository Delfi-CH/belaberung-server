package dev.delfi.chatapp.chatappbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chatapp")
@Getter
@Setter
public class ChatappConfig {

    private String domain;
    private String applicationName;
    private String applicationVersion;
    private String protocolVersion;
    private String adminName;
    private String adminMail;
}

package dev.delfi.chatapp.chatappbackend.model.meta;

import dev.delfi.chatapp.chatappbackend.config.ChatappConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetadataService {

    private final String applicationName;
    private final String applicationVersion;
    private final String protocolVersion;
    private final String adminMail;
    private final String domain;

    public MetadataService(ChatappConfig config) {
        this.applicationName = config.getApplicationName();
        this.applicationVersion = config.getApplicationVersion();
        this.protocolVersion = config.getProtocolVersion();
        this.adminMail = config.getAdminMail();
        this.domain = config.getDomain();
    }

    public List<String> getMetadata() {
        List<String> metadata = new ArrayList<>();
        metadata.add(applicationName);
        metadata.add(applicationVersion);
        metadata.add(protocolVersion);
        metadata.add(adminMail);
        metadata.add(domain);
        return metadata;
    }

}

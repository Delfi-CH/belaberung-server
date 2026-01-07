package dev.delfi.chatapp.chatappbackend.model.meta;

import dev.delfi.chatapp.chatappbackend.config.ChatappConfig;
import dev.delfi.chatapp.chatappbackend.exception.MetadataNotInitialisedExecption;
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
    private final LongtermMetadataRepository metadataRepository;

    public MetadataService(ChatappConfig config, LongtermMetadataRepository metadataRepository) {
        this.applicationName = config.getApplicationName();
        this.applicationVersion = config.getApplicationVersion();
        this.protocolVersion = config.getProtocolVersion();
        this.adminMail = config.getAdminMail();
        this.domain = config.getDomain();
        this.metadataRepository = metadataRepository;
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

    public Long getDeletedUserCount() {
        LongtermMetadata data = metadataRepository.findByName("main").orElseThrow(()-> new MetadataNotInitialisedExecption("Your Metadata was not initalised! Please restart the app. If this problem persits open a GitHub Issue"));
        return data.getDeletedUserCount();
    }
    public void upDeletedUserCount() {
        LongtermMetadata data = metadataRepository.findByName("main").orElseThrow(()-> new MetadataNotInitialisedExecption("Your Metadata was not initalised! Please restart the app. If this problem persits open a GitHub Issue"));
        data.setDeletedUserCount(data.getDeletedUserCount()+1);
    }

}

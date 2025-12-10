package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.auth.JsonWebTokenUtils;
import dev.delfi.chatapp.chatappbackend.model.meta.MetadataService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat/meta")
public class MetadataController {

    private final JsonWebTokenUtils jwtUtil;
    private final MetadataService metadataService;

    public MetadataController(JsonWebTokenUtils jwtUtil, MetadataService metadataService) {
        this.jwtUtil = jwtUtil;
        this.metadataService = metadataService;
    }

    @GetMapping()
    public List<String> getMetadata() {
        return metadataService.getMetadata();
    }

    @GetMapping("/status")
    public ResponseEntity<?> validateToken(@RequestHeader(value="Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Map.of("valid", false, "reason", "missing_token"));
        }

        String token = authHeader.substring(7);

        boolean valid = jwtUtil.validateToken(token);

        if (valid) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.status(401).body(Map.of("valid", false, "reason", "invalid_or_expired"));
        }
    }

}

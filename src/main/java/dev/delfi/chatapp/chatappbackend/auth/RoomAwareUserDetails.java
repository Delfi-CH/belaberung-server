package dev.delfi.chatapp.chatappbackend.auth;

import dev.delfi.chatapp.chatappbackend.model.room.Room;
import dev.delfi.chatapp.chatappbackend.model.room.RoomStatus;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserRoom;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Getter
public class RoomAwareUserDetails implements UserDetails {

    private final User user;

    public RoomAwareUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * Return authorities for a specific room
     */
    public Collection<? extends GrantedAuthority> getAuthoritiesForRoom(Room room) {
        UserRoom membership = user.getMemberships().stream()
                .filter(ur -> ur.getRoom().equals(room) && ur.getStatus() == RoomStatus.JOINED)
                .findFirst()
                .orElse(null);

        if (membership == null) return List.of(); // not in room or banned

        switch (membership.getRole()) {
            case OWNER -> {
                return List.of(
                        () -> "ROOM_MANAGE",
                        () -> "ROOM_DELETE",
                        () -> "ROOM_PROMOTE_USER",
                        () -> "ROOM_SEND_MESSAGE"
                );
            }
            case MODERATOR -> {
                return List.of(
                        () -> "ROOM_KICK_USER",
                        () -> "ROOM_MUTE_USER",
                        () -> "ROOM_SEND_MESSAGE"
                );
            }
            case USER -> {
                return List.of(
                        () -> "ROOM_SEND_MESSAGE"
                );
            }
            default -> {
                return List.of();
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return global authorities if you have any (optional)
        return List.of();
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

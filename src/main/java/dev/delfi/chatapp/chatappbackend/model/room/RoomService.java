package dev.delfi.chatapp.chatappbackend.model.room;

import dev.delfi.chatapp.chatappbackend.control.request.RoomCreateRequest;
import dev.delfi.chatapp.chatappbackend.exception.RoomNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserRepository;
import dev.delfi.chatapp.chatappbackend.model.user.UserRoom;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private UserRepository userRepository;
    private RoomRepository roomRepository;
    public RoomService(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public void create(RoomCreateRequest request) {
        Room room = new Room();
        room.setName(request.getName());
        room.setMaxUsers(request.getMaxUsers());
        roomRepository.save(room);

        User owner = userRepository.findUserById(request.getOwnerUserId()).orElseThrow(()->new UserNotFoundException("User not found"));
        owner.addRoom(room, RoomRole.OWNER);
    }

    public void delete(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(()->new RoomNotFoundException("Room not found"));
        roomRepository.delete(room);
    }
    public void updateName(Long id, String name) {
        Room room = roomRepository.findById(id).orElseThrow(()->new RoomNotFoundException("Room not found"));
        room.setName(name);
        roomRepository.save(room);
    }
    public void updateMaxUsers(Long id, Long maxUsers) {
        Room room = roomRepository.findById(id).orElseThrow(()->new RoomNotFoundException("Room not found"));
        if (maxUsers >= 1000L) {
            room.setMaxUsers(999L);
        } else if (maxUsers <= 0L) {
            room.setMaxUsers(10L);
        }
        roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    public Optional<Room> findRoomById(Long id) {
        return roomRepository.findRoomById(id);
    }
    public Optional<Room> findRoomByName(String name) {
        return roomRepository.findRoomByName(name);
    }
    public List<User> findAllUsers(Long id) {
        List<User> result = new ArrayList<>();
        Room room = roomRepository.findRoomById(id).orElseThrow(()->new RoomNotFoundException("Room not found"));
        room.getMembers().forEach(member -> {result.add(member.getUser());});
        return result;
    }

    @Transactional
    public void addUserToRoom(String username, Long roomID) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Room room = roomRepository.findRoomById(roomID)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        user.addRoom(room, RoomRole.USER);
    }

    @Transactional
    public void removeUserFromRoom(String username, Long roomID) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("User not found"));
        Room room = roomRepository.findRoomById(roomID).orElseThrow(()->new RoomNotFoundException("Room not found"));
        user.removeRoom(room);
        userRepository.save(user);
    }
}

package dev.delfi.chatapp.chatappbackend.model.room;

import dev.delfi.chatapp.chatappbackend.exception.RoomNotFoundException;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private RoomRepository roomRepository;
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void create(Room room) {
        roomRepository.save(room);
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
}

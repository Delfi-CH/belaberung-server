package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.control.request.RoomCreateRequest;
import dev.delfi.chatapp.chatappbackend.exception.RoomNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import dev.delfi.chatapp.chatappbackend.model.room.Room;
import dev.delfi.chatapp.chatappbackend.model.room.RoomRole;
import dev.delfi.chatapp.chatappbackend.model.room.RoomService;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;;

@RestController
@RequestMapping(value = "/api/chat/rooms", produces = "application/json")
public class RoomController {

    private final RoomService service;
    private final UserRepository userRepository;

    public RoomController(RoomService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return service.getAllRooms();
    }
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return service.findRoomById(id).orElseThrow(()->new RoomNotFoundException("Room not found"));
    }
    @GetMapping("/{name}")
    public Room getRoomByName(@PathVariable String name) {
        return service.findRoomByName(name).orElseThrow(()->new RoomNotFoundException("Room not found"));
    }
    @GetMapping("/{id}/users")
    public List<User> getUsersByRoomId(@PathVariable Long id) {
        return service.findAllUsers(id);
    }

    @PostMapping
    public void createRoom(@RequestBody RoomCreateRequest request) {
        service.create(request);
    }
    @DeleteMapping("/{id}")
    public void deleteRoomById(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/name")
    public void updateRoomName(@PathVariable Long id, @RequestBody String name) {
        service.updateName(id, name);
    }
    @PutMapping("/{id}/maxusers")
    public void updateRoomMaxUsers(@PathVariable Long id, @RequestBody Long maxUsers) {
        service.updateMaxUsers(id, maxUsers);
    }
}

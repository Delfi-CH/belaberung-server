package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.control.request.RoomCreateRequest;
import dev.delfi.chatapp.chatappbackend.control.request.RoomUserModifyerRequest;
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

    public RoomController(RoomService service, UserRepository userRepository) {
        this.service = service;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return service.getAllRooms();
    }
    @GetMapping("/id/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return service.findRoomById(id).orElseThrow(()->new RoomNotFoundException("Room not found"));
    }
    @GetMapping("/name/{name}")
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
    @PostMapping("/{id}/invite")
    public void addUserToRoom(@PathVariable Long id, @RequestBody RoomUserModifyerRequest request) {
        service.addUserToRoom(request.getUsername(), id);
    }
    @DeleteMapping("/{id}/")
    public void deleteRoomById(@PathVariable Long id) {
        service.delete(id);
    }

    @DeleteMapping("/{id}/remove")
    public void removeUserFromRoom(@PathVariable Long id, @RequestBody RoomUserModifyerRequest request) {
        service.removeUserFromRoom(request.getUsername(), id);
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

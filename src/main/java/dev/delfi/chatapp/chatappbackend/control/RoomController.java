package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.auth.JsonWebTokenUtils;
import dev.delfi.chatapp.chatappbackend.control.request.RoomCreateRequest;
import dev.delfi.chatapp.chatappbackend.control.request.RoomUserModifyerRequest;
import dev.delfi.chatapp.chatappbackend.control.request.SendMessageRequest;
import dev.delfi.chatapp.chatappbackend.exception.MessageNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.RoomNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import dev.delfi.chatapp.chatappbackend.model.message.Message;
import dev.delfi.chatapp.chatappbackend.model.message.MessageResponseItem;
import dev.delfi.chatapp.chatappbackend.model.message.MessageService;
import dev.delfi.chatapp.chatappbackend.model.room.Room;
import dev.delfi.chatapp.chatappbackend.model.room.RoomRole;
import dev.delfi.chatapp.chatappbackend.model.room.RoomService;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;;
/**
 * Controls the rooms
 * @author Delfi-CH
 * @version 1
 * */
@RestController
@RequestMapping(value = "/api/chat/rooms", produces = "application/json")
public class RoomController {

    private final RoomService service;
    private final MessageService msgService;
    private final JsonWebTokenUtils jwtUtils;

    /**
     * Creates the controller
     * */
    public RoomController(RoomService service, UserRepository userRepository, MessageService msgService, JsonWebTokenUtils jwtUtils) {
        this.service = service;
        this.msgService = msgService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Gets all Rooms
     * @return List of all Rooms
     * */
    @GetMapping
    public List<Room> getAllRooms() {
        return service.getAllRooms();
    }

    /**
     * Get a specific Room by ID
     * @param id Id of the requested Room
     * @return a singular Room
     * */
    @GetMapping("/id/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return service.findRoomById(id).orElseThrow(()->new RoomNotFoundException("Room not found"));
    }

    /**
     * Get a specific Room by Name
     * @param name name of the requested Room
     * @return a singular Room
     * */
    @GetMapping("/name/{name}")
    public Room getRoomByName(@PathVariable String name) {
        return service.findRoomByName(name).orElseThrow(()->new RoomNotFoundException("Room not found"));
    }

    /**
     * Get a List of Users of a Room
     * @param id Id of the requested Room
     * @return List of Users
     * */
    @GetMapping("/{id}/users")
    public List<User> getUsersByRoomId(@PathVariable Long id) {
        return service.findAllUsers(id);
    }

    /**
     * Get a List of Messages of a Room
     * @param id Id of the requested Room
     * @return List of Messages
     * */
    @GetMapping("/{id}/messages")
    public List<MessageResponseItem> getMessagesByRoomId(@PathVariable Long id) {
        return msgService.getMessagesInRoom(id);
    }

    @GetMapping(value = "/stream/{id}/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMessages(@PathVariable Long id) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        msgService.addEmitter(id, emitter);
        emitter.onCompletion(() -> msgService.removeEmitter(id, emitter));
        emitter.onTimeout(() -> msgService.removeEmitter(id, emitter));
        return emitter;
    }

    /**
     * Creates a room
     * @param request JSON-Data of the Room
     */
    @PostMapping("/create")
    public void createRoom(@RequestBody RoomCreateRequest request) {
        service.create(request);
    }

    /**
     * Adds user to room
     * @param id Id of the requested Room
     * */
    @PostMapping("/{id}/invite")
    public void addUserToRoom(@PathVariable Long id, @RequestBody RoomUserModifyerRequest request) {
        service.addUserToRoom(request.getUsername(), id);
    }

    /**
     * Deletes a room
     * @param id Id of the Room
     * */
    @DeleteMapping("/{id}/")
    public void deleteRoomById(@PathVariable Long id) {
        service.delete(id);
    }

    /**
     * Removes user from room
     * @param id Id of the requested Room
     * */
    @DeleteMapping("/{id}/remove")
    public void removeUserFromRoom(@PathVariable Long id, @RequestBody RoomUserModifyerRequest request) {
        service.removeUserFromRoom(request.getUsername(), id);
    }

    /**
     * Changes room name
     * @param id Id of the requested Room
     * @param name new Name of the Room
     * */
    @PutMapping("/{id}/name")
    public void updateRoomName(@PathVariable Long id, @RequestBody String name) {
        service.updateName(id, name);
    }

    /**
     * Sends message in room
     * @param id Id of the requested Room
     * @param request JSON-Data of the message
     * */
    @PostMapping("/{id}/send")
    public void sendMessage(@PathVariable Long id, @RequestBody SendMessageRequest request){
        msgService.sendMessageStream(request, id);
    }

}

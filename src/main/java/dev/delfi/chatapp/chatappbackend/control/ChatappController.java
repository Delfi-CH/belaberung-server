package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/chat", produces = "application/json")
@CrossOrigin("*")
public class ChatappController {

    private final ChatappService chatappService;
    private final UserRepository userRepository;

    public ChatappController(ChatappService chatappService, UserRepository userRepository) {
        this.chatappService = chatappService;
        this.userRepository = userRepository;
    }

    //
    // GET Mappings
    //

    // Users
    @GetMapping("/user")
    public List<User> getUsers() {
        return chatappService.getAllUsers();
    }
    @GetMapping("/user/id/{id}")
    public User getUserByID(@PathVariable Long id) {
        return chatappService.getUserById(id);
    }
    @GetMapping("/user/name/{username}")
    public User getUserByName(@PathVariable String username) {
        return chatappService.getUserByUsername(username);
    }
    @GetMapping("/user/rooms/{id}")
    public List<Room> getRoomsOfUserByUserID(@PathVariable Long id) {
        User user = chatappService.getUserById(id);
        return chatappService.getAllRoomsWithUserByUser(user);
    }

    // Messages
    @GetMapping("/message")
    public List<Message> getMessages() {
        return chatappService.getAllMessages();
    }
    @GetMapping("/message/room/{roomid}")
    public List<Message> getMessagesByRoomID(@PathVariable Long roomid) {
        Room room = chatappService.getRoomById(roomid);
        return chatappService.getMessagesByRoom(room);
    }
    @GetMapping("/message/user/{userid}")
    public List<Message> getMessagesByUserID(@PathVariable Long userid) {
        User user = chatappService.getUserById(userid);
        return chatappService.getMessagesByUser(user);
    }
    @GetMapping("/message/{id}")
    public Message getMessageByID(@PathVariable Long id) {
        return chatappService.getMessageById(id);
    }
    @GetMapping("/message/room/{roomid}/timeframe")
    public List<Message> getMessagesByRoomIDInTimeframe(@PathVariable Long roomid, @RequestParam Date from, @RequestParam Date to) {
        Room room = chatappService.getRoomById(roomid);
        return chatappService.getMessagesBetweenTimestampsInRoom(from, to, room);
    }
    @GetMapping("/message/user/{userid}/timeframe")
    public List<Message> getMessagesByUserIDInTimeframe(@PathVariable Long userid, @RequestParam Date from, @RequestParam Date to) {
        User user = chatappService.getUserById(userid);
        return chatappService.getMessagesBetweenTimestampsByUser(from, to, user);
    }

    // Rooms
    @GetMapping("/room")
    public List<Room> getRooms() {
        return chatappService.getAllRooms();
    }
    @GetMapping("/room/id/{id}")
    public Room getRoomByID(@PathVariable Long id) {
        return chatappService.getRoomById(id);
    }
    @GetMapping("/room/name/{roomname}")
    public Room getRoomByName(@PathVariable String roomname) {
        return chatappService.getRoomByName(roomname);
    }

    //
    // POST Mappings
    //

    @PostMapping("/user/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        if (chatappService.checkIfUserAlreadyExists(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        } else {
            User createdUser = chatappService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }
    }
    @PostMapping("/message/new")
    public Message sendMessage(@RequestBody Message message) {
        return chatappService.newMessage(message);
    }
    @PostMapping("/room/create")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        if (chatappService.checkIfRoomAlreadyExists(room.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(room);
        } else {
            if (room.getRoomRoot() == null) {
                User root = userRepository.getRoot();
                room.setRoomRoot(root);
            }
            Room createdRoom = chatappService.createRoom(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
        }
    }

    //
    // PUT Mappings
    //

    @PutMapping("/user/update/{id}/username")
    public User updateUsername(@PathVariable Long id, @RequestBody String username) {
        return chatappService.updateUsername(id, username);
    }

    @PutMapping("/user/update/{id}/password")
    public User updateUserPassword(@PathVariable Long id, @RequestBody String password) {
        return chatappService.updateUserPassword(id, password);
    }

    @PutMapping("/room/update/{id}/name")
    public Room updateRoomName(@PathVariable Long id, @RequestBody String name) {
        return chatappService.updateRoomName(id, name);
    }

    @PutMapping("/room/update/{id}/adduser")
    public void updateRoomUser(@PathVariable Long id, @RequestBody Long userid) {
        chatappService.addUserToRoom(id,userid);
    }
    @PutMapping("/room/update/{id}/promoteuser")
    public void promoteUserInRoom(@PathVariable Long id, @RequestBody Long userid) {
        chatappService.grantAdminPrivilegeToUserForRoom(id,userid);
    }
    @PutMapping("/room/update/{id}/changeowner")
    public void changeOwnerOfRoom(@PathVariable Long id, @RequestBody Long userid) {
        chatappService.changeOwnershipOfRoom(id,userid);
    }
    @PutMapping("/room/update/{id}/unbanuser")
    public void unbanUserFromRoom(@PathVariable Long id, @RequestBody Long userid) {
        chatappService.unbanUserFromRoom(id,userid);
    }

    @PutMapping("/room/update/{id}/removeuser")
    public void removeUserFromRoom(@PathVariable Long id, @RequestBody Long userid) {
        chatappService.removeUserFromRoom(id,userid);
    }
    @PutMapping("/room/update/{id}/demoteuser")
    public void demoteUserInRoom(@PathVariable Long id, @RequestBody Long userid) {
        chatappService.revokeAdminPrivilegeToUserForRoom(id,userid);
    }
    @PutMapping("/room/update/{id}/banuser")
    public void banUserFromRoom(@PathVariable Long id, @RequestBody Long userid) {
        chatappService.banUserFromRoom(id,userid);
    }

    @PutMapping("/room/update/{id}/maxusers")
    public void setMaxUsersFromRoom(@PathVariable Long id, @RequestBody Integer maxusers) {
        chatappService.updateMaxUsersOfRoom(id, maxusers);
    }
    @PutMapping("/room/update/{id}/public")
    public void setPublicStatusOfRoom(@PathVariable Long id, @RequestBody boolean isPublic) {
        chatappService.setPublicStatusOfRoom(id, isPublic);
    }


    //
    // DELETE Mappings
    //

    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        chatappService.deleteUserByID(id);
    }
    @DeleteMapping("/message/delete/{id}")
    public void deleteMessage(@PathVariable Long id) {
        chatappService.deleteMessageByID(id);
    }
    @DeleteMapping("/room/delete/{id}")
    public void deleteRoom(@PathVariable Long id) {
        chatappService.deleteRoomByID(id);
    }
}

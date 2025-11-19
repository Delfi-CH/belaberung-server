package dev.delfi.chatapp.chatappbackend.control;

import dev.delfi.chatapp.chatappbackend.model.ChatappService;
import dev.delfi.chatapp.chatappbackend.model.Message;
import dev.delfi.chatapp.chatappbackend.model.Room;
import dev.delfi.chatapp.chatappbackend.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/chat", produces = "application/json")
@CrossOrigin("*")
public class ChatappController {

    private final ChatappService chatappService;

    public ChatappController(ChatappService chatappService) {
        this.chatappService = chatappService;
    }

    //
    // GET Requests
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
        return chatappService.getMessagesByRoomID(roomid);
    }
    @GetMapping("/message/user/{userid}")
    public List<Message> getMessagesByUserID(@PathVariable Long userid) {
        return chatappService.getMessagesByUserID(userid);
    }
    @GetMapping("/message/{id}")
    public Message getMessageByID(@PathVariable Long id) {
        return chatappService.getMessageById(id);
    }
    @GetMapping("/message/room/{roomid}/timeframe")
    public List<Message> getMessagesByRoomIDInTimeframe(@PathVariable Long roomid, @RequestParam Date from, @RequestParam Date to) {
        return chatappService.getMessagesBetweenTimestampsInRoom(from, to, roomid);
    }
    @GetMapping("/message/user/{userid}/timeframe")
    public List<Message> getMessagesByUserIDInTimeframe(@PathVariable Long userid, @RequestParam Date from, @RequestParam Date to) {
        return chatappService.getMessagesBetweenTimestampsByUser(from, to, userid);
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
    public User createUser(@RequestBody User user) {
        return chatappService.createUser(user);
    }
    @PostMapping("/message/new")
    public Message sendMessage(@RequestBody Message message) {
        return chatappService.newMessage(message);
    }
    @PostMapping("/room/create")
    public Room createRoom(@RequestBody Room room) {
        return chatappService.createRoom(room);
    }

    //
    // DELETE Mapping
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

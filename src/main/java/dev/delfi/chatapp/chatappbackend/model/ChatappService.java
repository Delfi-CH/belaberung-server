package dev.delfi.chatapp.chatappbackend.model;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatappService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    public ChatappService(UserRepository userRepository, MessageRepository messageRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
    }

    // Query Methods
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    public List<Message> getMessagesByUser(User user) {
        return messageRepository.findByUser(user);
    }
    public List<Message> getMessagesByRoom(Room room) {
        return messageRepository.findByRoom(room);
    }
    public Message getMessageById(Long id) {
        return messageRepository.findMessageById(id);
    }
    public List<Message> getMessagesBetweenTimestampsInRoom(Date from, Date to, Room room) {
        List<Message> messages = messageRepository.findByRoom(room);
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getTimestamp().after(from) && message.getTimestamp().before(to)) {
                result.add(message);
            }
        }
        return result;
    }
    public List<Message> getMessagesBetweenTimestampsByUser(Date from, Date to, User user) {
        List<Message> messages = messageRepository.findByUser(user);
        List<Message> result = new ArrayList<>();

        for (Message message : messages) {
            if (message.getTimestamp().after(from) && message.getTimestamp().before(to)) {
                result.add(message);
            }
        }
        return result;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    public Room getRoomById(Long id) {
        return roomRepository.findRoomById(id);
    }
    public Room getRoomByName(String name) {
        return roomRepository.findRoomByName(name);
    }
    public List<Room> getAllRoomsWithUserByUser(User user) {
        List<Room> rooms = roomRepository.findAll();
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getUsers().contains(user)) {
                result.add(room);
            }
        }
        return result;
    }

    // Modify Methods

    public User createUser(User user) { return userRepository.save(user); }
    public Message newMessage(Message message) { return messageRepository.save(message); }
    public Room createRoom(Room room) { return roomRepository.save(room); }

    public void deleteUserByID(Long id) { userRepository.deleteById(id); }
    public void deleteMessageByID(Long id) { messageRepository.deleteById(id); }
    public void deleteRoomByID(Long id) { roomRepository.deleteById(id); }
}

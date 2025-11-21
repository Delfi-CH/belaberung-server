package dev.delfi.chatapp.chatappbackend.model;

import dev.delfi.chatapp.chatappbackend.config.ChatappConfig;
import dev.delfi.chatapp.chatappbackend.exception.MessageNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.RoomHasToManyUsersExecption;
import dev.delfi.chatapp.chatappbackend.exception.RoomNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatappService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final String configDomain;

    public ChatappService(UserRepository userRepository, MessageRepository messageRepository, RoomRepository roomRepository, ChatappConfig config) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.configDomain = config.getDomain();
    }

    // Query Methods
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    public List<Message> getMessagesByUser(User user) {
        return messageRepository.findByUser(user).orElseThrow(() -> new MessageNotFoundException("Cant't find messages by user"));
    }
    public List<Message> getMessagesByRoom(Room room) {
        return messageRepository.findByRoom(room).orElseThrow(() -> new MessageNotFoundException("Cant't find messages by room"));
    }
    public Message getMessageById(Long id) {
        return messageRepository.findMessageById(id).orElseThrow(() -> new MessageNotFoundException("Message with id " + id + " not found"));
    }
    public List<Message> getMessagesBetweenTimestampsInRoom(Date from, Date to, Room room) {
        List<Message> messages = messageRepository.findByRoom(room).orElseThrow(() -> new MessageNotFoundException("Cant't find messages by room"));
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getTimestamp().after(from) && message.getTimestamp().before(to)) {
                result.add(message);
            }
        }
        return result;
    }
    public List<Message> getMessagesBetweenTimestampsByUser(Date from, Date to, User user) {
        List<Message> messages = messageRepository.findByUser(user).orElseThrow(() -> new MessageNotFoundException("Cant't find messages by user"));
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
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with id " + username + " not found"));
    }
    public User getUserById(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    public Room getRoomById(Long id) {
        return roomRepository.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("Room with id " + id + " not found"));
    }
    public Room getRoomByName(String name) {
        return roomRepository.findRoomByName(name).orElseThrow(() -> new RoomNotFoundException("Room with name " + name + " not found"));
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

    public boolean checkIfUserAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }
    public boolean checkIfRoomAlreadyExists(String name) {
        return roomRepository.existsByName(name);
    }

    public Integer getNumberOfUsersInRoom(Long id) {
        Room room = roomRepository.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("Room with id " + id + " not found"));
        return room.getUsers().size();
    }

    public List<User> getUsersInRoom(Long id) {
        Room room = roomRepository.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("Room with id " + id + " not found"));
        return room.getUsers();
    }
    public List<Message> getMessagesInRoom(Long id) {
        Room room = roomRepository.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("Room with id " + id + " not found"));
        return room.getMessages();
    }

    // Create Methods

    public User createUser(User user) {
        user.setDomain(configDomain);
        return userRepository.save(user);
    }
    public Message newMessage(Message message) { return messageRepository.save(message); }
    public Room createRoom(Room room) {

        if (room.isDirectMessage()) {
            room.setMaxUsers(2L);
        } else if (room.isPublic()) {
            room.setMaxUsers(999L);
        } else if (room.getMaxUsers() > 999L) {
            room.setMaxUsers(999L);
        } else if (room.getMaxUsers() == 2L) {
            room.setDirectMessage(true);
        }

        return roomRepository.save(room);
    }

    // Modify Methods

    public User updateUsername(Long id, String name) {
        User user = userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setUsername(name);
        return userRepository.save(user);
    }

    public User updateUserPassword(Long id, String password) {
        User user = userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setPassword(password);
        return userRepository.save(user);
    }

    public Room updateRoomName(Long id, String name) {
        Room room = roomRepository.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("Room with id " + id + " not found"));
        room.setName(name);
        return roomRepository.save(room);
    }

    public void addUserToRoom(Long roomID, Long userID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(() -> new RoomNotFoundException("Room with id " + roomID + " not found"));
        User user = userRepository.findUserById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        if (!(room.getMaxUsers() >= room.getUsers().size())) {
            user.joinRoom(room);
        } else {
            throw new RoomHasToManyUsersExecption("Room with id " + roomID + " has too many users");
        }
        userRepository.save(user);
        roomRepository.save(room);
    }

    public void removeUserFromRoom(Long roomID, Long userID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(() -> new RoomNotFoundException("Room with id " + roomID + " not found"));
        User user = userRepository.findUserById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        user.leaveRoom(room);
        userRepository.save(user);
        roomRepository.save(room);
    }

    public void grantAdminPrivilegeToUserForRoom(Long roomID, Long userID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(() -> new RoomNotFoundException("Room with id " + roomID + " not found"));
        User user = userRepository.findUserById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        user.addManagedRoom(room);
        userRepository.save(user);
        roomRepository.save(room);
    }

    public void revokeAdminPrivilegeToUserForRoom(Long roomID, Long userID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(() -> new RoomNotFoundException("Room with id " + roomID + " not found"));
        User user = userRepository.findUserById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        user.stopManagingRoom(room);
        userRepository.save(user);
        roomRepository.save(room);
    }

    public void banUserFromRoom(Long roomID, Long userID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(() -> new RoomNotFoundException("Room with id " + roomID + " not found"));
        User user = userRepository.findUserById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        user.addBannedRoom(room);
        userRepository.save(user);
    }
    public void unbanUserFromRoom(Long roomID, Long userID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(() -> new RoomNotFoundException("Room with id " + roomID + " not found"));
        User user = userRepository.findUserById(userID).orElseThrow(() -> new UserNotFoundException("User with id " + userID + " not found"));
        user.removeBannedRoom(room);
        userRepository.save(user);
        roomRepository.save(room);
    }

    public void changeOwnershipOfRoom(Long roomID, Long newOwnerID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(() -> new RoomNotFoundException("Room with id " + roomID + " not found"));
        User user = userRepository.findUserById(newOwnerID).orElseThrow(() -> new UserNotFoundException("User with id " + newOwnerID + " not found"));
        room.setRoomRoot(user);
        room.addUser(user);
        userRepository.save(user);
        roomRepository.save(room);
    }

    public void setPublicStatusOfRoom(Long id, boolean newStatus) {
        Room room = roomRepository.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("Room with id " + id + " not found"));
        room.setPublic(newStatus);
        roomRepository.save(room);
    }

    public void updateMaxUsersOfRoom(Long id, long newMaxUsers) {
        Room room = roomRepository.findRoomById(id).orElseThrow(() -> new RoomNotFoundException("Room with id " + id + " not found"));
        room.setMaxUsers(newMaxUsers);
        if (room.getMaxUsers() > 999L) {
            room.setMaxUsers(999L);
        } else if (room.getMaxUsers() == 2L) {
            room.setDirectMessage(true);
        }
        roomRepository.save(room);
    }

    // Delete Methods

    public void deleteUserByID(Long id) { userRepository.deleteById(id); }
    public void deleteMessageByID(Long id) { messageRepository.deleteById(id); }
    public void deleteRoomByID(Long id) { roomRepository.deleteById(id); }
}

package dev.delfi.chatapp.chatappbackend.model.message;

import dev.delfi.chatapp.chatappbackend.control.request.SendMessageRequest;
import dev.delfi.chatapp.chatappbackend.exception.MessageNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.RoomNotFoundException;
import dev.delfi.chatapp.chatappbackend.exception.UserNotFoundException;
import dev.delfi.chatapp.chatappbackend.model.room.Room;
import dev.delfi.chatapp.chatappbackend.model.room.RoomRepository;
import dev.delfi.chatapp.chatappbackend.model.user.User;
import dev.delfi.chatapp.chatappbackend.model.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository, RoomRepository roomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public List<MessageResponseItem> getMessagesInRoom(Long roomID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(()->new RoomNotFoundException("Room not found"));
        List<Message> messages = messageRepository.findByRoom(room).orElseThrow(()->new MessageNotFoundException("Message not found"));
        List<MessageResponseItem> response = new ArrayList<>();
        for (Message message : messages) {
            MessageResponseItem tmp = new MessageResponseItem(message.getId(), message.getTimestamp(), message.getContent(), message.getUser().getUsername(), message.getUser().getDomain());
            response.add(tmp);
        }
        return response;
    }
    public void sendMessage(SendMessageRequest request, Long roomID){
        Room room = roomRepository.findRoomById(roomID).orElseThrow(()->new RoomNotFoundException("Room not found"));
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()-> new UserNotFoundException("User not found"));
        Message message = new Message();
        message.setRoom(room);
        message.setUser(user);
        message.setContent(request.getContent());
        message.setTimestamp(new Date().getTime());
        messageRepository.save(message);
        user.sendMessage(message);
        room.getMessages().add(message);
    }
}

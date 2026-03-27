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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

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
    /*public void sendMessage(SendMessageRequest request, Long roomID){
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
    }*/

    public void addEmitter(Long roomId, SseEmitter emitter) {
        emitters.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>())
                .add(emitter);
    }

    public void removeEmitter(Long roomId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(roomId);
        if (list != null) {
            list.remove(emitter);
        }
    }
    public void sendMessageStream(SendMessageRequest request, Long roomID) {
        Room room = roomRepository.findRoomById(roomID).orElseThrow(() -> new RoomNotFoundException("Room not found"));
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Message message = new Message();
        message.setRoom(room);
        message.setUser(user);
        message.setContent(request.getContent());
        message.setTimestamp(new Date().getTime());
        messageRepository.save(message);
        user.sendMessage(message);
        room.getMessages().add(message);

        MessageResponseItem response = new MessageResponseItem(message.getId(), message.getTimestamp(), message.getContent(), message.getUser().getUsername(), message.getUser().getDomain());
        List<SseEmitter> roomEmitters = emitters.get(roomID);
        if (roomEmitters != null) {
            for (SseEmitter emitter : roomEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("message")
                            .data(response));
                } catch (Exception e) {
                    emitter.complete();
                    removeEmitter(roomID, emitter);
                }
            }
        }
    }
}

package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        String messageText = message.getMessageText();
        int postedById = message.getPostedBy();
        long timePostedEpoch = message.getTimePostedEpoch();

        if (messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            throw new IllegalArgumentException("Message text must not be blank and should be less than 255 characters");
        }

        if (!messageRepository.existsById(postedById)) {
            throw new IllegalArgumentException("User does not exist");
        }
        
        Message newMessage = new Message(postedById, messageText, timePostedEpoch);
        return messageRepository.save(newMessage);
        }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

    public int deleteMessageById(int messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    public int updateMessageText(int messageId, String newMessageText) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255 || message.isEmpty()) {
            return 0;
        }

        Message existingMessage = message.get();
        existingMessage.setMessageText(newMessageText);
        messageRepository.save(existingMessage);
        return 1;
    }

    public List<Message> getMessagesByPostedBy(int postedBy) {
        return messageRepository.findMessagesByPostedBy(postedBy);
    }
}

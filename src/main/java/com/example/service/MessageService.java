package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        String messageText = message.getMessageText();
        int postedById = message.getPostedBy();
        long timePostedEpoch = message.getTimePostedEpoch();

        if (messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            throw new IllegalArgumentException("Message text must not be blank and should be less than 255 characters");
        }

        if(!messageRepository.existsById(postedById)) {
            throw new IllegalArgumentException("User does not exist");
        }
        
        Message newMessage = new Message(postedById, messageText, timePostedEpoch);
        return messageRepository.save(newMessage);
        }


    
}

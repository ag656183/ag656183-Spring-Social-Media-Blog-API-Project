package com.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.websocket.server.PathParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.AccountService;
import com.example.service.MessageService;

import com.example.entity.Account;
import com.example.entity.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        try {
            Account newAccount = accountService.registerUser(account.getUsername(), account.getPassword());
            if (newAccount == null) {
                return ResponseEntity.badRequest().body("Invalid username or password too short");
            }
            return ResponseEntity.ok(newAccount);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        try {
            Account existingAccount = accountService.loginUser(account.getUsername(), account.getPassword());
            if (existingAccount == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
            return ResponseEntity.ok(existingAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message newMessage = messageService.createMessage(message);
            return ResponseEntity.ok(newMessage);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);

        return message.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable int messageId) {
        int rowsDeleted = messageService.deleteMessageById(messageId);

        if(rowsDeleted == 1) {
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessageText(@PathVariable int messageId, @RequestBody String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        int rowsUpdated = messageService.updateMessageText(messageId, newMessageText);
    
        if (rowsUpdated == 1) {
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByUser(@PathVariable int accountId) {
        List<Message> messages = messageService.getMessagesByPostedBy(accountId);
        return ResponseEntity.ok(messages);
    }
}

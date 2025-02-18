package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password.length() < 4) {
            return null; // Invalid input
        }

        if (accountRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists");
        }

        Account newAccount = new Account(username, password);
        return accountRepository.save(newAccount);
    }
}

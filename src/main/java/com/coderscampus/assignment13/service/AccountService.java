package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account findById(Long accountId){
        return accountRepository.findById(accountId).orElse(null);
    }

    public void save(Account account){
        accountRepository.save(account);
    }
}

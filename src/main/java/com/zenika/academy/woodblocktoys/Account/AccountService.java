package com.zenika.academy.woodblocktoys.Account;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    /************************VARIABLES & CONSTRUCTOR************************/
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    /************************METHODS************************/

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        accountRepository.findAll().forEach(accounts::add);
        return accounts;
    }

    public Optional<Account> getAccountByMail(String mail) {
        return accountRepository.findByMail(mail);
    }


    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }


    public Account saveOrUpdateAccount(Account account) {
        accountRepository.save(account);
        return account;
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}

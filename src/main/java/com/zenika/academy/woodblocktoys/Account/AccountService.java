package com.zenika.academy.woodblocktoys.Account;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {

    Optional<Account> getAccountById(Long id);

    Optional<Account> getAccountByMail(String mail);

    List<Account> getAllAccounts();

    Account saveOrUpdateAccount(Account account);

    void deleteAccount(Long id);

}

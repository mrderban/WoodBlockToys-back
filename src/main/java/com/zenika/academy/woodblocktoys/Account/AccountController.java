package com.zenika.academy.woodblocktoys.Account;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Slf4j
@RequestMapping(path = "/accounts")
public class AccountController {


    /************************VARIABLES & CONSTRUCTOR************************/
    private final AccountService accountService;

    //constructor w/ dependency injection
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    /************************POST & DEL & PUT************************/
    @PostMapping(path = "/", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        //check mail unicity
        Optional<Account> optionalAccount = accountService.getAccountByMail(account.getMail());
        if (optionalAccount.isPresent()) {
            log.info("Email already present in database, cannot procees");
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "This email is already taken");
        }

        log.info("Account registration submitted " + account.toString());
        try {
            Account resultAccount = accountService.saveOrUpdateAccount(account);
            return ResponseEntity.ok(resultAccount);
        } catch (EntityExistsException ex) {
            return ResponseEntity.status((HttpStatus.CONFLICT)).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") long id) {
        log.info("Trying to delete account with id: {}", id);
        Optional<Account> optionalAccount = accountService.getAccountById(id);

        if (optionalAccount.isEmpty()) {
            log.info("Unable to delete account data with id: {}, user not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
        accountService.deleteAccount(id);
        return new ResponseEntity<>("Account has been deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseEntity<Account> updateAccount(@PathVariable("id") long id, @RequestBody Account account) {
        log.info("Trying to update account with id: {}", id);
        Optional<Account> optionalAccount = accountService.getAccountById(id);
        //try catch implementation
        if (optionalAccount.isPresent()) {
            log.info("Updating account with id: {}", id);
            Account _account = optionalAccount.get();
            _account.setFirstname(account.getFirstname());
            _account.setLastname(account.getLastname());
            _account.setMail(account.getMail());
            _account.setAddress(account.getAddress());
            _account.setPassword(account.getPassword());
            return new ResponseEntity<Account>(accountService.saveOrUpdateAccount(_account), HttpStatus.OK);
        } else {
            log.info("Account with id: {} wasn't found !", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    /************************GET************************/
    @GetMapping(path = "/mail/{mail}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Account> findAccountByMail(@PathVariable String mail) {
        log.info("Trying to fetch accounts data with mail: {}", mail);
        Optional<Account> optionalAccount = accountService.getAccountByMail(mail);

        if (optionalAccount.isEmpty()) {
            log.info("Unable to fetch accounts data with mail: {}", mail);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok().body(optionalAccount.get());
    }


    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Account>> getAllAccounts() {
        log.info("Trying to fetch accounts data");
        List<Account> accountList = accountService.getAllAccounts();
        if (accountList.isEmpty()) {
            log.info("Unable to fetch any account data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No users in database");
        }
        return ResponseEntity.ok().body(accountList);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Account> findAccountById(@PathVariable long id) {
        log.info("Trying to fetch account data with id: {}", id);
        Optional<Account> optionalAccount = accountService.getAccountById(id);
        if (optionalAccount.isEmpty()) {
            log.info("Unable to fetch account data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok().body(optionalAccount.get());
    }
}

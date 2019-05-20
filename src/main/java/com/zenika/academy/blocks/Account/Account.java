package com.zenika.academy.blocks.Account;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter


@Entity
@Table(name = "main_blocks_account")

public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotBlank(message = "Lastname field should be filled in")
    @Pattern(regexp = "[^0-9]+", message = "Incorrect entry")
    @Column(name = "account_lastname")
    @Size(min = 2, message = "Lastname must be at least 2 characters long")
    private String lastname;

    @NotBlank(message = "Firstname field should be filled in")
    @Pattern(regexp = "[^0-9]+", message = "Incorrect entry")
    @Column(name = "account_firstname")
    @Size(min = 2, max = 20)
    private String firstname;

    @NotBlank(message = "Address field should be filled in")
    @Column(name = "account_address")
    private String address;

    @Column(name = "account_mail", unique = true)
    @Email(message = "Please enter a valid email adress")
    private String mail;

    @NotBlank(message = "Firstname field should be filled in")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    @Column(name = "account_password")
    private String password;
}
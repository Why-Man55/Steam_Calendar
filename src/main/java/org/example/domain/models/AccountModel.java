package org.example.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountModel {
    String id;
    String username;
    String passwordHash;
}

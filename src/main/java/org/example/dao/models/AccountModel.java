package org.example.dao.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountModel {
    String id;
    String username;
    String passwordHash;
}

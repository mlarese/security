package it.epicode.security.auth.authorization;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}

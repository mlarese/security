package it.epicode.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionMessage {
    private String message;
    private String status;
    private Object error;
    private LocalDateTime timestamp = LocalDateTime.now();
}
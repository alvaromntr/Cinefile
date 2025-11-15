package com.example.cinefile.Infra;

import com.example.cinefile.DTO.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestsExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> threat404(EntityNotFoundException ex){
        ExceptionDTO response = new ExceptionDTO(ex.getMessage() == null ? "Resource not found" : ex.getMessage(), 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDTO> handleBadRequest(IllegalArgumentException ex) {
        ExceptionDTO dto = new ExceptionDTO(ex.getMessage(), 400);
        return ResponseEntity.badRequest().body(dto);
    }

    // Handler genérico (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGeneric(Exception ex) {
        ExceptionDTO dto = new ExceptionDTO(ex.getMessage() == null ? "Internal error" : ex.getMessage(), 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }
}

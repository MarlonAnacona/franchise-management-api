package com.franchise.management.api.domain.exception;

public class ConflictException extends  RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}

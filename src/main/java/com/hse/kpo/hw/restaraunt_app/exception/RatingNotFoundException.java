package com.hse.kpo.hw.restaraunt_app.exception;

public class RatingNotFoundException extends RuntimeException{
    public RatingNotFoundException(String message) {
        super(message);
    }
}

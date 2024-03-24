package com.hse.kpo.hw.restaraunt_app.exception;

public class NotEnoughRightsToMakeReview extends RuntimeException{
    public NotEnoughRightsToMakeReview(String message) {
        super(message);
    }
}

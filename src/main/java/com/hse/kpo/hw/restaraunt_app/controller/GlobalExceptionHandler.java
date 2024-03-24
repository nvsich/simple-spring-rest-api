package com.hse.kpo.hw.restaraunt_app.controller;

import com.hse.kpo.hw.restaraunt_app.entity.ApiErrorResponse;
import com.hse.kpo.hw.restaraunt_app.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            MenuNotFoundException.class,
            OrderNotFoundException.class,
            UserNotFoundException.class,
            UsernameNotFoundException.class,
            RatingNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(Exception e) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .description(e.getMessage())
                .code(String.valueOf(HttpStatus.NOT_FOUND))
                .exceptionName(e.getClass().getName())
                .build();

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            NotEnoughResourcesException.class,
            NotEnoughRightsToMakeReview.class,
            OrderProcessingException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(Exception e) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .description(e.getMessage())
                .code(String.valueOf(HttpStatus.BAD_REQUEST))
                .exceptionName(e.getClass().getName())
                .build();

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }
}

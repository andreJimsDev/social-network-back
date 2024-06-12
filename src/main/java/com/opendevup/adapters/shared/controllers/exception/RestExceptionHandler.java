package com.opendevup.adapters.shared.controllers.exception;

import com.opendevup.adapters.user.controllers.dto.ErrorMessageDto;
import com.opendevup.core.shared.model.exceptions.EntityNotFoundException;
import com.opendevup.core.shared.model.exceptions.ValidationInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessageDto> handleDomainEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(value = {ValidationInputException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessageDto> handleInvalidArgumentException(ValidationInputException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageDto(ex.getMessage()));
    }
}
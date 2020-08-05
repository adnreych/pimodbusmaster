package net.lockoil.pimodbusmaster.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jssc.SerialPortException;

@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ExceptionHandler(SerialPortException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  
    public void handleConflict() {
        // Nothing to do
    }
}
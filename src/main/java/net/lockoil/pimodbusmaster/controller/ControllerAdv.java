package net.lockoil.pimodbusmaster.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;

@ControllerAdvice
public class ControllerAdv extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ModbusIOException.class)
    protected ResponseEntity<String> handleModbusIOException() {
        return new ResponseEntity<>("ModbusIOException", HttpStatus.NOT_FOUND);
    }

    
}
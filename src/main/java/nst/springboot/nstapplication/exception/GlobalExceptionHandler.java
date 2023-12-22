/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author student2
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyErrorDetails> handleException(Exception e) {

        MyErrorDetails myErrorDetails = new MyErrorDetails(e.getMessage());
        System.out.println(e.getClass());
        return new ResponseEntity<>(myErrorDetails, HttpStatus.NOT_FOUND);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        
        //pokupi sve greske koje su nastale pri validaciji vrednosti nad objektoma
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        for (ObjectError error : objectErrors) {
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    
    
}

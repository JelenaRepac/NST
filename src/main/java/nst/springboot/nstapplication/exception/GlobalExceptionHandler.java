/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nst.springboot.nstapplication.constants.ConstantsCustom;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author student2
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(EntityNotFoundException.class)
    public MyErrorDetails handleEntityNotFoundException(EntityNotFoundException ex){
        return MyErrorDetails.builder().
                errorCode(ConstantsCustom.NOT_FOUND_CODE).
                errorMessage(ex.getMessage()).
                build();
    }
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public MyErrorDetails handleEntityNotFoundException(EntityAlreadyExistsException ex){
        return MyErrorDetails.builder().
                errorCode(ConstantsCustom.NOT_FOUND_CODE).
                errorMessage(ex.getMessage()).
                build();
    }
    @ExceptionHandler(Exception.class)
    public MyErrorDetails handleException(Exception e) {
        return MyErrorDetails.builder().
                errorCode(ConstantsCustom.NOT_FOUND_CODE).
                errorMessage(e.getMessage()).
                build();

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

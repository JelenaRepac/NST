package nst.springboot.nstapplication.exception;

public class EmptyResponseException extends RuntimeException{

    public EmptyResponseException(String message) {
        super(message);
    }
}

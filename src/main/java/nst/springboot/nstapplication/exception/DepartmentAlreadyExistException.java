/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.exception;

/**
 *
 * @author student2
 */
public class DepartmentAlreadyExistException extends RuntimeException{

    public DepartmentAlreadyExistException(String message) {
        super(message);
    }
    
}

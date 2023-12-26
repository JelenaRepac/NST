/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.exception;

import lombok.*;

/**
 *
 * @author student2
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyErrorDetails {

    private String errorCode;
    private String errorMessage;
}

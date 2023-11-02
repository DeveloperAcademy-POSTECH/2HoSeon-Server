package com.twohoseon.app.exception;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : InvalidFileTypeException
 * @date : 2023/11/01
 * @modifyed : $
 **/
public class InvalidFileTypeException extends RuntimeException {

    public InvalidFileTypeException() {
        super("Not image file ");
    }

    public InvalidFileTypeException(String message) {
        super(message);
    }
}

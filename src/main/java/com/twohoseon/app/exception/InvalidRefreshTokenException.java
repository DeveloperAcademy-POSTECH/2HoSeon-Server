package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : InvalidRefreshTokenException
 * @date : 11/12/23 7:29 AM
 * @modifyed : $
 **/
public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException() {
        super("Invalid Refresh Token.");
    }

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}

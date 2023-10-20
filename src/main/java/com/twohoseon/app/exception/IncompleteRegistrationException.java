package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : IncompleteRegistrationException
 * @date : 10/21/23 5:10 AM
 * @modifyed : $
 **/
public class IncompleteRegistrationException extends RuntimeException {

    public IncompleteRegistrationException() {
        super("Registration is incomplete.");
    }

    public IncompleteRegistrationException(String message) {
        super(message);
    }
}


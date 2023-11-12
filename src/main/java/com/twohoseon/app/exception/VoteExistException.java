package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : VoteException
 * @date : 11/11/23 1:30 AM
 * @modifyed : $
 **/
public class VoteExistException extends RuntimeException {
    public VoteExistException() {
        super("Already voted.");
    }

    public VoteExistException(String message) {
        super(message);
    }

}

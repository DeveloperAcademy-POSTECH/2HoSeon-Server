package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : BanedMemberException
 * @date : 11/28/23 12:05 AM
 * @modifyed : $
 **/
public class BanedMemberException extends RuntimeException {
    public BanedMemberException() {
        super("You are banned from this service.");
    }

    public BanedMemberException(String message) {
        super(message);
    }
}

package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : NicknameDuplicateException
 * @date : 10/21/23 6:15 AM
 * @modifyed : $
 **/
public class NicknameDuplicateException extends RuntimeException {

    public NicknameDuplicateException() {
        super("Nickname is already in use.");
    }

    public NicknameDuplicateException(String message) {
        super(message);
    }

}

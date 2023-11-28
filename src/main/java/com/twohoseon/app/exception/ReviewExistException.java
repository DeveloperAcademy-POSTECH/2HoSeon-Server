package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ReviewExistException
 * @date : 11/5/23 4:40 AM
 * @modifyed : $
 **/
public class ReviewExistException extends RuntimeException {

    public ReviewExistException() {
        super("Review already exist.");
    }

    public ReviewExistException(String message) {
        super(message);
    }

}

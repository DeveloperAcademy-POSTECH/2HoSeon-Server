package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ConsumerTypeNotFoundException
 * @date : 11/29/23 5:09 AM
 * @modifyed : $
 **/
public class ConsumerTypeNotFoundException extends RuntimeException {
    public ConsumerTypeNotFoundException() {
        super("Please take the consumer type test.");
    }

    public ConsumerTypeNotFoundException(String message) {
        super(message);
    }
}

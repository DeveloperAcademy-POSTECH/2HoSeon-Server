package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostNotFoundException
 * @date : 10/21/23 5:22 AM
 * @modifyed : $
 **/
public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super("Post not found.");
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}

package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CommentNotFoundException
 * @date : 10/21/23 5:22 AM
 * @modifyed : $
 **/
public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {
        super("Comment not found.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}

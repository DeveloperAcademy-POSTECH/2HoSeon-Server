package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PermissionDeniedException
 * @date : 10/31/23 4:44 PM
 * @modifyed : $
 **/
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super("Permission denied.");
    }

    public PermissionDeniedException(String message) {
        super(message);
    }
}

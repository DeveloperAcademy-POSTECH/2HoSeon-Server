package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SchoolUpdateRestrictionException
 * @date : 11/14/23 3:33 PM
 * @modifyed : $
 **/
public class SchoolUpdateRestrictionException extends RuntimeException {
    public SchoolUpdateRestrictionException() {
        super("It has not been 6 months since the school was updated.");
    }

    public SchoolUpdateRestrictionException(String message) {
        super(message);
    }
}

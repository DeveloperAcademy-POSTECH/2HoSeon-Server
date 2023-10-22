package com.twohoseon.app.exception;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : MemberNotFoundException
 * @date : 10/21/23 5:22 AM
 * @modifyed : $
 **/
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("Member not found.");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}

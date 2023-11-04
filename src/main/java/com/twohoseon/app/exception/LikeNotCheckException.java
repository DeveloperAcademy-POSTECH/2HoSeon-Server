package com.twohoseon.app.exception;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : NotCheckLikeException
 * @date : 2023/11/05
 * @modifyed : $
 **/
public class LikeNotCheckException extends RuntimeException {

    public LikeNotCheckException() {
        super("Like Not Check");
    }

    public LikeNotCheckException(String message) {
        super(message);
    }
}

package com.twohoseon.app.exception;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : LikeDuplicateException
 * @date : 2023/11/05
 * @modifyed : $
 **/
public class LikeExistsException extends RuntimeException {

    public LikeExistsException() {
        super("Like is already existed.");
    }

    public LikeExistsException(String message) {
        super(message);
    }

}

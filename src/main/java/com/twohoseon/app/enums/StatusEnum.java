package com.twohoseon.app.enums;

import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : StatusEnum
 * @date : 2023/10/07 4:47 PM
 * @modifyed : $
 **/
@Getter
public enum StatusEnum {
    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    CONFLICT(409, "CONFLICT"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");


    int statusCode;
    String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }


}

package com.twohoseon.app.enums;

import lombok.Getter;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ErrorCode
 * @date : 2023/10/21 4:32 PM
 * @modifyed : $
 * <p>
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Global Error CodeList : 전역으로 발생하는 에러코드를 관리한다.
 * Custom Error CodeList : 추가된 에러코드
 * Error Code Constructor : 에러코드를 직접적으로 사용하기 위한 생성자를 구성한다.
 */
@Getter
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(400, "G005", "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(400, "G006", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400, "G007", "com.fasterxml.jackson.core Exception"),


    // 권한이 없음
    FORBIDDEN_ERROR(403, "G010", "Forbidden Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "G011", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(404, "G012", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(404, "G013", "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(404, "G014", "Header에 데이터가 존재하지 않는 경우 "),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),


    /**
     * ******************************* Custom Error CodeList ***************************************
     */

    // 회원이 존재하지 않음
    MEMBER_NOT_FOUND_ERROR(404, "E001", "Member Not Found Exception"),

    // 게시글이 존재하지 않음
    POST_NOT_FOUND_ERROR(404, "E002", "Post Not Found Exception"),

    // 댓글이 존재하지 않음
    COMMENT_NOT_FOUND_ERROR(404, "E003", "Comment Not Found Exception"),

    // JWT 서명이 유효하지 않음
    INVALID_SIGNATURE_ERROR(401, "E004", "Invalid JWT Signature"),

    // JWT 형식이 올바르지 않음
    MALFORMED_TOKEN_ERROR(401, "E005", "Invalid JWT Token"),

    // JWT 토큰이 만료됨
    EXPIRED_TOKEN_ERROR(401, "E006", "Expired JWT Token"),

    // 지원하지 않는 JWT 토큰
    UNSUPPORTED_TOKEN_ERROR(401, "E007", "Unsupported JWT Token"),

    // JWT 클레임 문자열이 비어 있음
    EMPTY_CLAIMS_ERROR(400, "E008", "JWT Claims String is Empty"),

    // 회원 가입이 완료되지 않음
    NOT_COMPLETED_SIGNUP_ERROR(400, "E009", "Not Completed SignUp Exception"),

    // Transaction Insert Error
    INSERT_ERROR(200, "E010", "Insert Transaction Error Exception"),

    // Transaction Update Error
    UPDATE_ERROR(200, "E011", "Update Transaction Error Exception"),

    // Transaction Delete Error
    DELETE_ERROR(200, "E012", "Delete Transaction Error Exception"),

    // 닉네임이 중복됨
    NICKNAME_DUPLICATE_ERROR(409, "E013", "Nickname is already in use"),

    //후기를 중복해서 작성할 수 없음
    REVIEW_DUPLICATE_ERROR(409, "E014", "Review is already exist"),

    //투표를 중복해서 작성할 수 없음
    VOTE_DUPLICATE_ERROR(409, "E015", "Vote is already exist"),

    //유효하지 않은 리프레쉬 토큰
    INVALID_REFRESH_TOKEN_ERROR(401, "E016", "Invalid Refresh Token");

    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private final int status;

    // 에러 코드의 '코드간 구분 값'을 반환한다.
    private final String divisionCode;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private final String message;

    // 생성자 구성
    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}

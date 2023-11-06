package com.twohoseon.app.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twohoseon.app.dto.ErrorResponse;
import com.twohoseon.app.enums.ErrorCode;
import com.twohoseon.app.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;


/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : GlobalExceptionHandler
 * @date : 2023/10/10 5:16 AM
 * @modifyed : $
 * <p>
 * Controller 내에서 발생하는 Exception 대해서 Catch 하여 응답값(Response)을 보내주는 기능을 수행함.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, String.valueOf(stringBuilder));
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * [Exception] 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 request로 '파라미터로' 데이터가 넘어오지 않았을 경우
     *
     * @param ex MissingServletRequestParameterException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
            MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     *
     * @param e HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException e) {
        log.error("HttpClientErrorException.BadRequest", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     *
     * @param e NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.error("handleNoHandlerFoundExceptionException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    /**
     * [Exception] NULL 값이 발생한 경우
     *
     * @param e NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("handleNullPointerException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * Input / Output 내에서 발생한 경우
     *
     * @param ex IOException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        log.error("handleIOException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.IO_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    /**
     * com.fasterxml.jackson.core 내에 Exception 발생하는 경우
     *
     * @param ex JsonProcessingException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("handleJsonProcessingException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * [Exception] PermissionDeniedException 발생하는 경우(유저에게 해당 권한이 없음)
     *
     * @param ex PermissionDeniedException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(PermissionDeniedException.class)
    protected ResponseEntity<ErrorResponse> handlePermissionDeniedException(PermissionDeniedException ex) {
        log.error("handlePermissionDeniedException", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.FORBIDDEN_ERROR);
    }


    /**
     * [Exception] MemberNotFoundException 발생하는 경우
     *
     * @param ex MemberNotFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException ex) {
        log.error("handleMemberNotFoundException", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.MEMBER_NOT_FOUND_ERROR);
    }

    /**
     * [Exception] IncompleteRegistrationException 발생하는 경우
     *
     * @param ex IncompleteRegistrationException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IncompleteRegistrationException.class)
    protected ResponseEntity<ErrorResponse> handleIncompleteRegistrationException(IncompleteRegistrationException ex) {
        log.error("handleIncompleteRegistrationException", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.NOT_COMPLETED_SIGNUP_ERROR);
    }

    /**
     * [Exception] PostNotFoundException 발생하는 경우
     *
     * @param ex PostNotFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(PostNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException ex) {
        log.error("handlePostNotFoundException", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.POST_NOT_FOUND_ERROR);
    }

    /**
     * [Exception] CommentNotFoundException 발생하는 경우
     *
     * @param ex CommentNotFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(CommentNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException ex) {
        log.error("handleCommentNotFoundException", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.COMMENT_NOT_FOUND_ERROR);
    }

    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex) {
        log.error("handleSignatureException: Invalid JWT signature", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_SIGNATURE_ERROR);
    }

    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
        log.error("handleMalformedJwtException: Invalid JWT token", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.MALFORMED_TOKEN_ERROR);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        log.error("handleExpiredJwtException: Expired JWT token", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.EXPIRED_TOKEN_ERROR);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    protected ResponseEntity<ErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        log.error("handleUnsupportedJwtException: Unsupported JWT token", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.UNSUPPORTED_TOKEN_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("handleIllegalArgumentException: JWT claims string is empty", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.EMPTY_CLAIMS_ERROR);
    }

    @ExceptionHandler(NicknameDuplicateException.class)
    protected ResponseEntity<ErrorResponse> handleNicknameDuplicateException(NicknameDuplicateException ex) {
        log.error("handleNicknameDuplicateException: Nickname is already in use", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.NICKNAME_DUPLICATE_ERROR);
    }

    @ExceptionHandler(ReviewExistException.class)
    protected ResponseEntity<ErrorResponse> handleReviewExistException(ReviewExistException ex) {
        log.error("handleReviewExistException: Review is already exist", ex);
        return ErrorResponse.toResponseEntity(ErrorCode.REVIEW_DUPLICATE_ERROR);
    }

    // ==================================================================================================================

    /**
     * [Exception] 모든 Exception 경우 발생
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Exception", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }
}

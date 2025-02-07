package com.drinkeg.drinkeg.global.exception;

import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.ReasonDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ReasonDTO errorReasonHttpStatus = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("필수 요청 데이터가 누락되었습니다: " + e.getRequestPartName())
                .code("MISSING_REQUEST_PART")
                .build();

        return handleExceptionInternal(e, errorReasonHttpStatus, null, request);
    }

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<?> onThrowException(GeneralException generalException, HttpServletRequest request) {
        ReasonDTO errorReasonHttpStatus = generalException.getErrorStatus();
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ReasonDTO errorReasonHttpStatus = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .code("ARGUMENT_ERROR")
                .build();
        return handleExceptionInternal(e, errorReasonHttpStatus, null, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ReasonDTO errorReasonHttpStatus = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .code("VALIDATION_ERROR")
                .build();
        return handleExceptionInternal(e, errorReasonHttpStatus, headers, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ReasonDTO reason, HttpHeaders headers, HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        return handleExceptionInternal(e, reason, headers, reason.getHttpStatus(), webRequest);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ReasonDTO reason, HttpHeaders headers, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                request
        );
    }
}
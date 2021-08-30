package jsz.dk.signmanagement.interceptor.advice;

import jsz.dk.signmanagement.common.BaseException;
import jsz.dk.signmanagement.common.entity.ApiException;
import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.common.entity.ResponseParent;
import jsz.dk.signmanagement.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.interceptor.advice
 * @ClassName: BaseExceptionAdvice
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/13 19:43
 */
@RestControllerAdvice
@ResponseBody
@Slf4j
public class BaseExceptionAdvice {
    /**
     * 处理其他所以未知的异常
     */
    @ExceptionHandler({Exception.class})
    public ResponseParent<?> globalExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseParent.fail(ResponseCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理其他所以未知的异常
     */
    @ExceptionHandler({CustomException.class})
    public ResponseParent<?> globalExceptionHandler(CustomException e) {
        log.error(e.getMessage(), e);
        return ResponseParent.fail(ResponseCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler({BaseException.class})
    public ResponseParent<?> businessExceptionHandler(BaseException e) {
        log.error(e.getMessage(), e);
        return ResponseParent.fail(e.getCode(), e.getMessage());
    }

    /**
     * 404 异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseParent<?> handlerNoHandlerFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseParent.fail(ResponseCode.NOT_FOUND);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseParent<?> handlerHttpMessageNotReadableException(
            HttpMessageNotReadableException e){
        log.error("丢失请求参数主题:{}" ,e.getMessage());
        return ResponseParent.fail(ResponseCode.PARAM_MISS);
    }

    /**
     * 405 异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseParent<?> handlerHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.error("不支持的请求方式: {}",e.getMessage());
        return ResponseParent.fail(ResponseCode.METHOD_NOT_SUPPORTED);
    }

    /**
     * 415 异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseParent<?> handlerHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResponseParent.fail(ResponseCode.MEDIA_TYPE_NOT_SUPPORTED);
    }
}

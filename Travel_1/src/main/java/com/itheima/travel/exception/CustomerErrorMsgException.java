package com.itheima.travel.exception;

/**
 用户自定义异常,主要起到语义作用
 */
public class CustomerErrorMsgException extends Exception {

    public CustomerErrorMsgException(String message) {
        super(message);
    }
}

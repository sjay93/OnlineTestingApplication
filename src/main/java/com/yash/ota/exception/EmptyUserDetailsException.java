package com.yash.ota.exception;

public class EmptyUserDetailsException extends Exception {
    public EmptyUserDetailsException(String errMsg) {
        super(errMsg);
    }
}

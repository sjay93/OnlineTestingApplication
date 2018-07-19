package com.yash.ota.exception;

public class DuplicateUserException extends Exception {
    public DuplicateUserException(String errMsg) {
        super(errMsg);
    }
}

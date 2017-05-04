package com.jay.six.utils;

/**
 * Created by jayli on 2017/5/3 0003.
 */

public class StringIsEmptyException extends Exception {

    public StringIsEmptyException() {
    }

    public StringIsEmptyException(String message) {
        super(message);
    }

    public StringIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringIsEmptyException(Throwable cause) {
        super(cause);
    }

    public StringIsEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

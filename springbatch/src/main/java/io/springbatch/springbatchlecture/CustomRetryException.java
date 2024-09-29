package io.springbatch.springbatchlecture;

public class CustomRetryException extends Exception {
    public CustomRetryException(String msg) {
        super(msg);
    }
}

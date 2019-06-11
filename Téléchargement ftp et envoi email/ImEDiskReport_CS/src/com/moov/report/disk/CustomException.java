package com.moov.report.disk;

import java.io.IOException;

public class CustomException extends IOException {

    private static final long serialVersionUID = 1L;

    public CustomException(String msg) {
        super(msg);
    }

    public CustomException(String msg, Exception cause) {
        super(msg, cause);
    }
}

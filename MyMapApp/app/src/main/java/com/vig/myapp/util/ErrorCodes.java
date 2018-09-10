package com.vig.myapp.util;


public class ErrorCodes {
    public static ErrorCodes instance;

    public static ErrorCodes getInstance() {
        if (instance == null) {
            instance = new ErrorCodes();
        }
        return instance;
    }

    //For web request and response
    public static String DEFAULT_MESSAGE = "Loading Failed";
}

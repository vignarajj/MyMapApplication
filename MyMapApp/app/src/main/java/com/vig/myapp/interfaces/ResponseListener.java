package com.vig.myapp.interfaces;


public interface ResponseListener {
    void onResponse(String responseStr);
    void onFailure(String message);
}

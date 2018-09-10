package com.vig.myapp.request;

import android.content.Context;

import com.vig.myapp.interfaces.ResponseListener;

public class WebServices {

    public static WebServices instance;

    public static WebServices getInstance() {
        if (instance == null) {
            instance = new WebServices();
        }
        return instance;
    }


    public void getUsersData(Context context, final ResponseListener responseListener) {
        String completeUrl = Urls.getUsersUrl;
        WebRequest.getInstance().getRequest(completeUrl, context, new ResponseListener() {
            @Override
            public void onResponse(String responseStr) {
                responseListener.onResponse(responseStr);
            }

            @Override
            public void onFailure(String message) {
                responseListener.onFailure(message);
            }
        });
    }


}

package com.vig.myapp.request;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.vig.myapp.interfaces.ResponseListener;
import com.vig.myapp.util.ErrorCodes;
import com.vig.myapp.util.TLSSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class WebRequest {
    public static WebRequest instance;
    OkHttpClient client;
    public static int CONNECTION_TIMEOUT = 60000;
    public static int CONNECTION_READOUT = 60000;
    public static int CONNECTION_WRITEOUT = 60000;

    public static WebRequest getInstance() {
        if (instance == null) {
            instance = new WebRequest();
        }
        return instance;
    }

    public void setStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public ConnectionSpec setConnectionSpec() {
        final ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
        final TrustManagerFactory tmf;
        return spec;
    }

    public TrustManagerFactory setAllTrustManagerFactory() {
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        return tmf;
    }

    public void getRequest(final String url, final Context context, final ResponseListener responseListener) {
        Log.i("method", "getRequest");
        Log.i("url", url);
        setStrictMode();
        try {
            final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(CONNECTION_WRITEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(CONNECTION_READOUT, TimeUnit.MILLISECONDS)
//                    .connectionSpecs(Collections.singletonList(setConnectionSpec()))
                    .connectionSpecs(specs())
                    .sslSocketFactory(new TLSSocketFactory(), (X509TrustManager) setAllTrustManagerFactory().getTrustManagers()[0]);
            ;
            client = clientBuilder.build();

//            Util.logLargeString("sslSocketFactory", new Gson().toJson(client.sslSocketFactory()));
//            Util.logLargeString("accessToken",  UtilPreferences.getAccessToken(context));
            Request request = new Request.Builder()
                    .url(url)
//                    .addHeader("Authorization", "Bearer " + UtilPreferences.getAccessToken(context))
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("error#", url+" : "+e.getMessage());
                    String errorMessage= "Loading Failed";
                    if(e.getMessage()!=null){
                        errorMessage = e.getMessage();
                    }
                    responseListener.onFailure("Exception - " + errorMessage);
//                    Util.sendAPIError(context, url, errorMessage, 1);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
                    if (responseString != null) {
//                        Util.logLargeString("response_st", responseString);
//                        Util.sendAPIError(context, url, responseString, 0);
                        responseListener.onResponse(responseString);
                    } else {
                        Log.e("response", "null");
//                        Util.sendAPIError(context, url, "No response", 1);
                        responseListener.onFailure("Exception - " + ErrorCodes.DEFAULT_MESSAGE);
                    }
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
//        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            Log.e("error#", url+" : "+e.getMessage());
            responseListener.onFailure("Exception - " + e.getMessage());
        }
    }

    public void postRequest(final String url, String postData, final Context context, final ResponseListener responseListener) {
        Log.i("method", "postRequest");
        Log.i("url", url);
        setStrictMode();
        try {
            final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(CONNECTION_WRITEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(CONNECTION_READOUT, TimeUnit.MILLISECONDS)
                    .connectionSpecs(Collections.singletonList(setConnectionSpec()))
                    .sslSocketFactory(new TLSSocketFactory(), (X509TrustManager) setAllTrustManagerFactory().getTrustManagers()[0]);
//                    .connectionSpecs(Collections.singletonList(spec))
//                    .sslSocketFactory(new TLSSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0]);
            client = clientBuilder.build();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//            RequestBody body = new FormBody.Builder()
//                    .add(parameter, postData)
//                    .build();
            JSONObject jsonObject = new JSONObject(postData);
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
//                    .addHeader("Authorization", "Bearer " + UtilPreferences.getAccessToken(context))
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("error#", url+" : "+e.getMessage());
                    String errorMessage= "";
                    if(e.getMessage()!=null){
                        errorMessage = e.getMessage();
                    }
                    responseListener.onFailure("Exception - " + ErrorCodes.DEFAULT_MESSAGE);
//                    Util.sendAPIError(context, url, errorMessage, 1);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
                    if (responseString != null) {
//                        Util.sendAPIError(context, url, responseString, 0);
                        responseListener.onResponse(responseString);
                    } else {
                        Log.e("response", "null");
//                        Util.sendAPIError(context, url, "No response", 1);
                        responseListener.onFailure("Exception - " + ErrorCodes.DEFAULT_MESSAGE);
                    }
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException| JSONException e) {
            Log.e("error#", url+" : "+e.getMessage());
            responseListener.onFailure("Exception - " + e.getMessage());
        }
    }

    public List<ConnectionSpec> specs(){
        List<ConnectionSpec> specs = new ArrayList<>();
        specs.add(setConnectionSpec());
        specs.add(ConnectionSpec.CLEARTEXT);
        return specs;
    }
}

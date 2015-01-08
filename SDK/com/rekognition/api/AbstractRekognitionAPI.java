package com.rekognition.api;

import android.util.Log;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.IRekognitionResponseAdapter;
import com.rekognition.http.HttpManager;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

public abstract class AbstractRekognitionAPI {

    protected static String REKO_API_HOST_NAME = "https://rekognition.com/func/api/";
    private final List<HttpParameter> keyAndSecret;

    private static final String API_KEY_NAME = "api_key";
    private static final String API_SECRET_NAME = "api_secret";
    
    protected enum HttpMethod {
        GET, POST
    }

    private final HttpManager httpClient;

    public AbstractRekognitionAPI(String apiKey, String apiSecret) {
        this.httpClient = HttpManager.getInstance();
        keyAndSecret = new ArrayList<HttpParameter>();
        HttpParameter apiKeyParam = new HttpParameter(API_KEY_NAME, apiKey);
        HttpParameter apiSecretParam = new HttpParameter(API_SECRET_NAME, apiSecret);
        keyAndSecret.add(apiKeyParam);
        keyAndSecret.add(apiSecretParam);
    }

    protected <T extends IRekognitionResponseAdapter> T perform(String url, 
            Collection<HttpParameter> params, HttpMethod method, T adapter) 
                    throws RekognitionAPIException, AdapterInitException {
        params.addAll(this.keyAndSecret);
        if (method == HttpMethod.GET) {
            String response = this.httpClient.get(url, params);
            adapter.setResponseString(response);
        } else {
            String response = this.httpClient.post(url, params);
            adapter.setResponseString(response);
        }
        return adapter;
    }
}

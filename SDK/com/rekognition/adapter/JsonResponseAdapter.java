package com.rekognition.adapter;

import org.json.JSONObject;
import org.json.JSONException;

import android.util.Log;

public class JsonResponseAdapter implements IRekognitionResponseAdapter {

    JSONObject rootJson;

    String responseStr;

    JSONObject usageJsonObject;

    Integer quota = null;
    String status = null;
    String apiId = null;

    @Override
    public void setResponseString(String responseStr) throws AdapterInitException {
        if (responseStr == null || responseStr.isEmpty()) {
            throw new AdapterInitException("Empty/Null response string provided");
        }
        try {
            this.responseStr = responseStr;
            this.rootJson = new JSONObject(responseStr);
        } catch (JSONException ex) {
            Log.e(JsonResponseAdapter.class.toString(), "Cannot init json object from " + responseStr, ex);
            throw new AdapterInitException("Cannot init json object from " + responseStr);
        }
    }

    /**
     * @return the rootJson
     */
    public JSONObject getJsonObject() {
        return rootJson;
    }

    /**
     * @return the responseStr
     */
    public String getResponseStr() {
        return responseStr;
    }

    public Integer getQuota() throws JSONException {

        if (quota == null && this.rootJson.has("usage")) {
            if (this.rootJson.getJSONObject("usage").has("quota")) {
                this.quota = this.rootJson.getJSONObject("usage").getInt("quota");
            }
        }
        return this.quota;
    }

    public String getStatus() throws JSONException {
        if (status == null && this.rootJson.has("usage")) {
            if (this.rootJson.getJSONObject("usage").has("status")) {
                this.status = this.rootJson.getJSONObject("usage").getString("status");
            }
        }
        return this.status;
    }

    public String getApiId() throws JSONException {
        if (apiId == null && this.rootJson.has("usage")) {
            if (this.rootJson.getJSONObject("usage").has("api_id")) {
                this.apiId = this.rootJson.getJSONObject("usage").getString("api_id");
            }
        }
        return this.apiId;
    }
}

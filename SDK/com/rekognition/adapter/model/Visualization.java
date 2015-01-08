package com.rekognition.adapter.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;


public class Visualization implements IRekognitionModel {

    private JSONObject visualizationObj;

    private String tag = null;
    private String url = null;
    private List<String> index = null;

    @Override
    public void loadDataFromJSONObject(JSONObject visualizationObj) {
        this.visualizationObj = visualizationObj;
    }

    public String getTag() throws FieldNotFoundException, JSONException {
        if (this.tag == null) {
            if (this.visualizationObj.has("tag")) {
                this.tag = this.visualizationObj.getString("tag");
            } else {
                throw new FieldNotFoundException("tag");
            }
        }
        return tag;
    }

    public String getUrl() throws FieldNotFoundException, JSONException {
        if (this.url == null) {
            if (this.visualizationObj.has("url")) {
                this.url = this.visualizationObj.getString("url");
            } else {
                throw new FieldNotFoundException("url");
            }
        }
        return url;
    }

    public List<String> getIndex() throws FieldNotFoundException, JSONException {
        if (this.index == null) {
            if (this.visualizationObj.has("index")) {
                JSONArray indexArray = this.visualizationObj.getJSONArray("index");
                this.index = new ArrayList<String>();
                for (int i = 0 ; i < indexArray.length() ; i ++) {
                    this.index.add(indexArray.getString(i));
                }
            } else {
                throw new FieldNotFoundException("index");
            }
        } 
        return index;
    }

}

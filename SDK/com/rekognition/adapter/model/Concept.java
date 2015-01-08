package com.rekognition.adapter.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;


public class Concept implements IRekognitionModel {

    private JSONObject conceptObj = null;

    private String tag = null;
    private Double score = null;

    @Override
    public void loadDataFromJSONObject(JSONObject sceneObj) {
        this.conceptObj = sceneObj;
    }

    public String getTag() throws FieldNotFoundException, JSONException {
        if (this.tag == null) {
            if (this.conceptObj.has("tag")) {
                this.tag = this.conceptObj.getString("tag");
            } else {
                throw new FieldNotFoundException("concept.tag");
            }
        }
        return tag;
    }

    public Double getScore() throws FieldNotFoundException, JSONException {
        if (this.score == null) {
            if (this.conceptObj.has("score")) {
                this.score = this.conceptObj.getDouble("score");
            } else {
                throw new FieldNotFoundException("concept.score");
            }
        }
        return score;
    }
}

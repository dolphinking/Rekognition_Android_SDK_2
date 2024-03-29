package com.rekognition.adapter.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;


public class Scene implements IRekognitionModel {

    private JSONObject sceneObj = null;

    private String label = null;
    private Double score = null;

    @Override
    public void loadDataFromJSONObject(JSONObject sceneObj) {
        this.sceneObj = sceneObj;
    }

    public String getLabel() throws FieldNotFoundException, JSONException {
        if (this.label == null) {
            if (this.sceneObj.has("label")) {
                this.label = this.sceneObj.getString("label");
            } else {
                throw new FieldNotFoundException("scene.label");
            }
        }
        return label;
    }

    public Double getScore() throws FieldNotFoundException, JSONException {
        if (this.score == null) {
            if (this.sceneObj.has("score")) {
                this.score = this.sceneObj.getDouble("score");
            } else {
                throw new FieldNotFoundException("scene.score");
            }
        }
        return score;
    }
}

package com.rekognition.adapter.model;
import org.json.JSONObject;


public interface IRekognitionModel {

    void loadDataFromJSONObject(JSONObject jsonObj) throws FieldNotFoundException;

}

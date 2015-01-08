package com.rekognition.adapter.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;


public class NameSpaceStats implements IRekognitionModel {

    private JSONObject nameSpaceObj;

    private String nameSpace = null;
    private Integer numberOfUserId = null;
    private Integer numberOfTags = null;
    private Integer numberOfImages = null;

    @Override
    public void loadDataFromJSONObject(JSONObject nameSpaceObj) {
        this.nameSpaceObj = nameSpaceObj;
    }

    public String getNameSpace() throws FieldNotFoundException, JSONException {
        if (this.nameSpace == null) {
            if (this.nameSpaceObj.has("name_space")) {
                this.nameSpace = this.nameSpaceObj.getString("name_space");
            } else {
                throw new FieldNotFoundException("name_space_stats.name_space");
            }
        }
        return nameSpace;
    }

    public Integer getNumberOfUserId() throws FieldNotFoundException, JSONException {
        if (this.numberOfUserId == null) {
            if (this.nameSpaceObj.has("num_user_id")) {
                this.numberOfUserId = this.nameSpaceObj.getInt("num_user_id");
            } else {
                throw new FieldNotFoundException("name_space_stats.num_user_id");
            }
        }
        return numberOfUserId;
    }

    public Integer getNumberOfTags() throws FieldNotFoundException, JSONException {
        if (this.numberOfTags == null) {
            if (this.nameSpaceObj.has("num_tags")) {
                this.numberOfTags = this.nameSpaceObj.getInt("num_tags");
            } else {
                throw new FieldNotFoundException("name_space_stats.num_tags");
            }
        }
        return numberOfTags;
    }

    public Integer getNumberOfImages() throws FieldNotFoundException, JSONException {
        if (this.numberOfImages == null) {
            if (this.nameSpaceObj.has("num_img")) {
                this.numberOfImages = this.nameSpaceObj.getInt("num_img");
            } else {
                throw new FieldNotFoundException("name_space_stats.num_img");
            }
        }
        return numberOfImages;
    }

}

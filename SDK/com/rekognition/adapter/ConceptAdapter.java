package com.rekognition.adapter;

import com.rekognition.adapter.model.Concept;
import com.rekognition.adapter.model.Scene;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by batman on 1/7/15.
 */
public class ConceptAdapter extends JsonResponseAdapter {
    private JSONArray conceptJSONArray = null;
    private List<Concept>  conceptList = null;

    @Override
    public void setResponseString(String responseStr) throws AdapterInitException {
        super.setResponseString(responseStr);
        if (this.getJsonObject().has("scene_understanding")) {
            try {
                if (this.getJsonObject().getJSONObject("scene_understanding").has("matches")) {
                    this.conceptJSONArray = this.getJsonObject().getJSONObject("scene_understanding").getJSONArray("matches");
                }
                else
                {
                    throw new AdapterInitException("Cannot init SceneUnderstandingAdapter since matches is missing from the response json");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            throw new AdapterInitException("Cannot init SceneUnderstandingAdapter since scene_understanding is missing from the response json");
        }
    }

    public List<Concept> getConcepts () {
        if (this.conceptList == null) {
            this.conceptList = new ArrayList<Concept>();
            for (int i = 0 ; i < this.conceptJSONArray.length() ; i ++) {
                JSONObject conceptObj;
                try {
                    conceptObj = this.conceptJSONArray.getJSONObject(i);
                    Concept concept = new Concept();
                    concept.loadDataFromJSONObject(conceptObj);
                    this.conceptList.add(concept);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return this.conceptList;
    }
}

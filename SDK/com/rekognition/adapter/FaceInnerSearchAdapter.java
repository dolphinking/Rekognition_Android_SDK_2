package com.rekognition.adapter;

import com.rekognition.adapter.model.Face;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;


public class FaceInnerSearchAdapter extends JsonResponseAdapter {

    private List<Face.Match> matches = null;

    private JSONArray matchesArray = null;

    @Override
    public void setResponseString(String responseStr) throws AdapterInitException {
        super.setResponseString(responseStr);
        if (this.getJsonObject().has("matches")) {
            try {
                this.matchesArray = this.getJsonObject().getJSONArray("matches");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public List<Face.Match> getMatches() {
        if (this.matchesArray != null) {
            if (this.matches == null) {
                this.matches = new ArrayList<Face.Match>();
                for (int i = 0 ; i < this.matchesArray.length() ; i ++) {
                    JSONObject matchObj;
                    try {
                        matchObj = this.matchesArray.getJSONObject(i);
                        Face.Match match = new Face.Match();
                        match.loadDataFromJSONObject(matchObj);
                        this.matches.add(match);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return this.matches;
    }

}

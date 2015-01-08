package com.rekognition.adapter;

import com.rekognition.adapter.model.Scene;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class SceneUnderstandingAdapter extends JsonResponseAdapter {


    private JSONArray sceneArray = null;
    private List<Scene> scenes = null;

    @Override
    public void setResponseString(String responseStr) throws AdapterInitException {
        super.setResponseString(responseStr);
        if (this.getJsonObject().has("scene_understanding")) {
            try {
                this.sceneArray = this.getJsonObject().getJSONArray("scene_understanding");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            throw new AdapterInitException("Cannot init SceneUnderstandingAdapter since scene_understanding is missing from the response json");
        }
    }

    public List<Scene> getScenes () {
        if (this.scenes == null) {
            this.scenes = new ArrayList<Scene>();
            for (int i = 0 ; i < this.sceneArray.length() ; i ++) {
                JSONObject sceneObj;
                try {
                    sceneObj = this.sceneArray.getJSONObject(i);
                    Scene scene = new Scene();
                    scene.loadDataFromJSONObject(sceneObj);
                    this.scenes.add(scene);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return this.scenes;
    }

}

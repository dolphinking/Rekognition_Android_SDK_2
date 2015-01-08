package com.rekognition.adapter;

import com.rekognition.adapter.model.Visualization;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;


public class FaceVisualizationAdapter extends JsonResponseAdapter {

    private JSONArray visualizationArray;

    private List<Visualization> visualizations = null;

    @Override
    public void setResponseString(String responseStr) throws AdapterInitException {
        super.setResponseString(responseStr);
        try {
            this.visualizationArray = this.getJsonObject().getJSONArray("visualization");
        } catch (JSONException ex) {
            Log.e(FaceVisualizationAdapter.class.toString(), "Cannot get visualization field from json object" + responseStr, ex);
            throw new AdapterInitException("Cannot get visualization field from json object");
        }
    }

    /**
     * @return the visualizations
     */
    public List<Visualization> getVisualizations() {
        if (this.visualizations == null) {
            this.visualizations = new ArrayList<Visualization>();
            for (int i = 0 ; i < this.visualizationArray.length() ; i ++) {
                JSONObject visualizationObj;
                try {
                    visualizationObj = this.visualizationArray.getJSONObject(i);
                    Visualization visualization = new Visualization();
                    visualization.loadDataFromJSONObject(visualizationObj);
                    this.visualizations.add(visualization);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return visualizations;
    }


}

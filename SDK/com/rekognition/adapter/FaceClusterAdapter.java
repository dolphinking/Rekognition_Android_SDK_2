package com.rekognition.adapter;

import com.rekognition.adapter.model.Cluster;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

public class FaceClusterAdapter extends JsonResponseAdapter {


    private JSONArray clustersArray = null;

    private final List<Cluster> clusters = new ArrayList<Cluster>();

    @Override
    public void setResponseString(String responseStr) throws AdapterInitException {
        super.setResponseString(responseStr);
        try {
            this.clustersArray = this.getJsonObject().getJSONArray("clusters");
        } catch (JSONException ex) {
            Log.e(FaceClusterAdapter.class.toString(), "Cannot get clusters field from json object" + responseStr, ex);
            throw new AdapterInitException("Cannot get clusters field from json object");
        }
    }

    public List<Cluster> getClusters () {
        if (this.clusters.isEmpty()) {
            if (this.clustersArray != null) {
                for (int i = 0 ; i < this.clustersArray.length() ; i ++) {
                    JSONObject clusterObj;
                    try {
                        clusterObj = this.clustersArray.getJSONObject(i);
                        Cluster cluster = new Cluster();
                        cluster.loadDataFromJSONObject(clusterObj);
                        this.clusters.add(cluster);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } 

                }
            }
        }
        return this.clusters;
    }

}

package com.rekognition.adapter;

import com.rekognition.adapter.model.FieldNotFoundException;
import com.rekognition.adapter.model.NameSpaceStats;
import com.rekognition.adapter.model.UserIdStats;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

public class FaceStatsAdapter extends JsonResponseAdapter {

    private JSONArray nameSpaceStatsArray = null;
    private JSONArray userIdStatsArray = null;

    private List<NameSpaceStats> nameSpaceStats = null;
    private List<UserIdStats> userIdStats = null;

    @Override
    public void setResponseString(String responseStr) throws AdapterInitException {
        super.setResponseString(responseStr);
        if (this.getJsonObject().has("name_space_stats")) {
            try {
                this.nameSpaceStatsArray = this.getJsonObject().getJSONArray("name_space_stats");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (this.getJsonObject().has("user_id_stats")) {
            try {
                this.userIdStatsArray = this.getJsonObject().getJSONArray("user_id_stats");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public List<NameSpaceStats> getNameSpaceStats () {
        if (this.nameSpaceStatsArray != null) {
            if (this.nameSpaceStats == null) {
                nameSpaceStats = new ArrayList<NameSpaceStats>();
                for (int i = 0 ; i < this.nameSpaceStatsArray.length() ; i ++) {
                    JSONObject nameSpaceObj;
                    try {
                        nameSpaceObj = this.nameSpaceStatsArray.getJSONObject(i);
                        NameSpaceStats nss = new NameSpaceStats();
                        nss.loadDataFromJSONObject(nameSpaceObj);
                        this.nameSpaceStats.add(nss);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return this.nameSpaceStats;
        } else {
            return null;
        }
    }

    public List<UserIdStats> getUserIdStats() {
        if (this.userIdStatsArray != null) {
            if (this.userIdStats == null) {
                this.userIdStats = new ArrayList<UserIdStats>();
                for (int i = 0 ; i < this.userIdStatsArray.length() ; i ++) {
                    JSONObject userIdObj;
                    try {
                        userIdObj = this.userIdStatsArray.getJSONObject(i);
                        UserIdStats uis = new UserIdStats();
                        uis.loadDataFromJSONObject(userIdObj);
                        this.userIdStats.add(uis);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return this.userIdStats;
        } else {
            return null;
        }
    }


}

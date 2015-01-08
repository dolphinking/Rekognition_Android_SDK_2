package com.rekognition.api.impl;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.FaceClusterAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class FaceCluster extends AbstractRekognitionAPI{

    public FaceCluster(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public FaceClusterAdapter getResponse(String nameSpace, String userId, Integer aggressiveness) throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter("jobs", "face_cluster"));
        if (!nameSpace.isEmpty()) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        if (!userId.isEmpty()) {
            params.add(new HttpParameter("user_id", userId));
        }
        if (aggressiveness != null) {
            if (aggressiveness < 0) {
                aggressiveness = 0;
            } else if (aggressiveness > 100) {
                aggressiveness = 100;
            }
            params.add(new HttpParameter("aggressiveness", aggressiveness.toString()));
        }
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceClusterAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceCluster.class.toString(), ex.getMessage());
            return null;
        }
    }

}

package com.rekognition.api.impl;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.FaceStatsAdapter;
import com.rekognition.adapter.JsonResponseAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FaceStats extends AbstractRekognitionAPI {

    private static final Logger logger = Logger.getLogger(FaceStats.class.toString());

    public FaceStats(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public FaceStatsAdapter getNameSpaceStats () throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter("jobs", "face_name_space_stats"));
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceStatsAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

    public FaceStatsAdapter getUserIdStats (String nameSpace) throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter("jobs", "face_user_id_stats"));
        if (!nameSpace.isEmpty()) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceStatsAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

}

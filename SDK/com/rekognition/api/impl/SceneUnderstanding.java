package com.rekognition.api.impl;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.FaceVisualizationAdapter;
import com.rekognition.adapter.SceneUnderstandingAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SceneUnderstanding extends AbstractRekognitionAPI {

    private static final Logger logger = Logger.getLogger(SceneUnderstanding.class.toString());

    public SceneUnderstanding(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public SceneUnderstandingAdapter getResposne (String url) throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        if (url.isEmpty()) {
            throw new RekognitionAPIException("You have to provide an url for scene understanding");
        }
        params.add(new HttpParameter("jobs", "scene"));
        params.add(new HttpParameter("urls", url));
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new SceneUnderstandingAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

}

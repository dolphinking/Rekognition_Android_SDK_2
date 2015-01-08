package com.rekognition.api.impl;

import android.text.TextUtils;
import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.FaceAdapter;
import com.rekognition.adapter.FaceVisualizationAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class FaceVisualize extends AbstractRekognitionAPI {

    private static final Logger logger = Logger.getLogger(FaceVisualize.class.toString());

    public FaceVisualize(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public FaceVisualizationAdapter getResponse (boolean needDisplayImage, boolean showUntaggedFace, 
            String nameSpace, String userId, List<String> tags, Integer numOfTagsReturn, Integer numOfImageReturnPerTag) throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        StringBuilder jobsSb = new StringBuilder();
        jobsSb.append("face_visualize");
        if (!needDisplayImage) {
            jobsSb.append("_no_image");
        }
        if (showUntaggedFace) {
            jobsSb.append("_show_default_tag");
        }
        params.add(new HttpParameter("jobs", jobsSb.toString()));
        if (!nameSpace.isEmpty()) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        if (!userId.isEmpty()) {
            params.add(new HttpParameter("user_id", userId));
        }
        if (tags != null && !tags.isEmpty()) {
            params.add(new HttpParameter("tags", TextUtils.join(";", tags)));
        }
        if (numOfTagsReturn != null) {
            params.add(new HttpParameter("num_tag_return", numOfTagsReturn));
        }
        if (numOfImageReturnPerTag != null) {
            params.add(new HttpParameter("num_img_return_pertag", numOfImageReturnPerTag));
        }
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.GET, new FaceVisualizationAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

}

package com.rekognition.api.impl;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.FaceClusterAdapter;
import com.rekognition.adapter.JsonResponseAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.text.TextUtils;


public class FaceCrawl extends AbstractRekognitionAPI {


    public FaceCrawl(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public JsonResponseAdapter getResponse (List<Long> idsToCrawl, long fbId, String accessToken, String nameSpace, String userId) throws RekognitionAPIException {
        if (idsToCrawl == null || idsToCrawl.isEmpty()) {
            throw new RekognitionAPIException("Please provide the facebook ids to crawl");
        }
        if (accessToken.isEmpty()) {
            throw new RekognitionAPIException("Access Token is needed for crawling facebook pictures");
        }
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter("jobs", "face_crawl_" + concatFacebookIds(idsToCrawl)));
        params.add(new HttpParameter("fb_id", fbId));
        params.add(new HttpParameter("access_token", accessToken));
        if (!nameSpace.isEmpty()) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        if (!userId.isEmpty()) {
            params.add(new HttpParameter("user_id", userId));
        }
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.GET, new JsonResponseAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceCrawl.class.toString(), ex.getMessage());
            return null;
        }
    }

    private String concatFacebookIds (List<Long> idsToCrawl) {
        return "[" + TextUtils.join(";", idsToCrawl) + "]";
    }

}

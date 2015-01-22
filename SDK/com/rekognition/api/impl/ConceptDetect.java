package com.rekognition.api.impl;

import android.graphics.Bitmap;
import android.util.Log;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.ConceptAdapter;
import com.rekognition.adapter.FaceAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by batman on 1/7/15.
 */
public class ConceptDetect extends AbstractRekognitionAPI {
    private static final Logger logger = Logger.getLogger(SceneUnderstanding.class.toString());

    public ConceptDetect(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public ConceptAdapter getResposne (String url, int nMaxNumReturn) throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        if (url==null || url.isEmpty()) {
            throw new RekognitionAPIException("You have to provide an url for scene understanding");
        }
        params.add(new HttpParameter("jobs", "scene_understanding_3"));
        params.add(new HttpParameter("num_return", nMaxNumReturn));
        params.add(new HttpParameter("urls", url));
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new ConceptAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

    public ConceptAdapter getResposne (Bitmap bitmap, int nMaxNumReturn) throws RekognitionAPIException {
        if (bitmap == null) {
            throw new RekognitionAPIException("You have to provide an Bitmap for Concept Recognition.");
        }
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter("jobs", "scene_understanding_3"));
        params.add(new HttpParameter("num_return", nMaxNumReturn));
        params.add(new HttpParameter("uploaded_file", new HttpParameter.BitmapField(bitmap)));
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new ConceptAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceDetect.class.toString(), ex.getMessage());
            return null;
        }
    }
}

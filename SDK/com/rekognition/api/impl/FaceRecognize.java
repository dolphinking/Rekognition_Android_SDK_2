package com.rekognition.api.impl;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.FaceAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.text.TextUtils;


public class FaceRecognize extends AbstractRekognitionAPI {

    public FaceRecognize(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public FaceAdapter recognizeFaceWithDetect(List<FaceDetect.FaceDetectJobs> detectJobs, String url, 
            String nameSpace, String userId, Integer numberReturn, List<String> tags) throws RekognitionAPIException {
        try {
            List<HttpParameter> params = generateParams(detectJobs, url, null, nameSpace, userId, numberReturn, tags, true);
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceRecognize.class.toString(), ex.getMessage());
            return null;
        } catch (IOException ex) {
            Log.e(FaceRecognize.class.toString(), ex.getMessage());
            return null;
        }
    }

    public FaceAdapter recognizeFaceWithDetect(List<FaceDetect.FaceDetectJobs> detectJobs, byte[] base64_byte, 
            String nameSpace, String userId, Integer numberReturn, List<String> tags) throws RekognitionAPIException, IOException {
        try {
            List<HttpParameter> params = generateParams(detectJobs, null, base64_byte, nameSpace, userId, numberReturn, tags, true);
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceRecognize.class.toString(), ex.getMessage());
            return null;
        } catch (IOException ex) {
            Log.e(FaceRecognize.class.toString(), ex.getMessage());
            return null;
        }
    }

    public FaceAdapter recognizeFaceWithoutDetect(String url, String nameSpace, String userId, 
            Integer numberReturn, List<String> tags) throws RekognitionAPIException, IOException {
        try {
            List<HttpParameter> params = generateParams(null, url, null, nameSpace, userId, numberReturn, tags, false);
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceRecognize.class.toString(), ex.getMessage());
            return null;
        } catch (IOException ex) {
            Log.e(FaceRecognize.class.toString(), ex.getMessage());
            return null;
        }
    }

    public FaceAdapter recognizeFaceWithoutDetect(byte[] base64_byte, String nameSpace, String userId, 
            Integer numberReturn, List<String> tags) throws RekognitionAPIException, IOException {
        try {
            List<HttpParameter> params = generateParams(null, null, base64_byte, nameSpace, userId, numberReturn, tags, false);
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceRecognize.class.toString(), ex.getMessage());
            return null;
        } catch (IOException ex) {
            Log.e(FaceRecognize.class.toString(), ex.getMessage());
            return null;
        }
    }

    private List<HttpParameter> generateParams (List<FaceDetect.FaceDetectJobs> detectJobs, String url, 
            byte[] base64_byte, String nameSpace, String userId, Integer numberReturn, List<String> tags, boolean needDetect) throws IOException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        if (needDetect) {
            String detectJobsString = "";
            if (detectJobs != null && !detectJobs.isEmpty()) {
                detectJobsString = "_" + concatJobs(detectJobs);
            }
            params.add(new HttpParameter("jobs", "face_recognize" + detectJobsString));
        } else {
            params.add(new HttpParameter("jobs", "face_recognize_nodetect"));
        }
        if (url!=null && !url.isEmpty()) {
            params.add(new HttpParameter("urls", url));
        }
        if (base64_byte != null) {
            params.add(new HttpParameter("base64", new HttpParameter.Base64Field(base64_byte)));
        }
        if (!nameSpace.isEmpty()) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        if (!userId.isEmpty()) {
            params.add(new HttpParameter("user_id", userId));
        }
        if (numberReturn != null) {
            params.add(new HttpParameter("num_return", numberReturn.toString()));
        }
        if (tags != null && !tags.isEmpty()) {
            params.add(new HttpParameter("tags", concatTags(tags)));
        }
        return params;
    }

    private String concatJobs (List<FaceDetect.FaceDetectJobs> jobs) {
        StringBuilder sb = new StringBuilder();
        for (FaceDetect.FaceDetectJobs job : jobs) {
            sb.append(job.getValue()).append("_");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private String concatTags (List<String> tags) {
        return TextUtils.join(";", tags);
    }

}

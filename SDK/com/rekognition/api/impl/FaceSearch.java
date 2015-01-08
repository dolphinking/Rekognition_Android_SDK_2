package com.rekognition.api.impl;

import android.text.TextUtils;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.FaceAdapter;
import com.rekognition.adapter.FaceInnerSearchAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;;

public class FaceSearch extends AbstractRekognitionAPI {

    private static final Logger logger = Logger.getLogger(FaceSearch.class.toString());

    public FaceSearch(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public FaceAdapter outSearchWithFaceDetect(List<String> tags, List<FaceDetect.FaceDetectJobs> faceDetectJobs,
            String url, String nameSpace, String userId, Integer numberReturn) throws RekognitionAPIException {

        try {
            List<HttpParameter> params = generateParams(faceDetectJobs, url, null, nameSpace, userId, numberReturn, tags, true);
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

    public FaceAdapter outSearchWithFaceDetect(List<String> tags, List<FaceDetect.FaceDetectJobs> faceDetectJobs,
            byte[] base64_byte, String nameSpace, String userId, Integer numberReturn) throws RekognitionAPIException {

        try {
            List<HttpParameter> params = generateParams(faceDetectJobs, null, base64_byte, nameSpace, userId, numberReturn, tags, true);
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

    public FaceAdapter outSearchWithNoFaceDetect(List<String> tags, String url, String nameSpace,
            String userId, Integer numberReturn) throws RekognitionAPIException {

        try {
            List<HttpParameter> params = generateParams(null, url, null, nameSpace, userId, numberReturn, tags, false);
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

    public FaceAdapter outSearchWithNoFaceDetect(List<String> tags, byte[] base64_byte, String nameSpace,
            String userId, Integer numberReturn) throws RekognitionAPIException {
        try {
            List<HttpParameter> params = generateParams(null, null, base64_byte, nameSpace, userId, numberReturn, tags, false);
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

    public FaceInnerSearchAdapter innerSearch(String queryTag, String imageIndex, String nameSpace, String userId, Integer numReturn) throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter("jobs", "face_inner_search"));
        if (queryTag.isEmpty()) {
            throw new RekognitionAPIException("You need to provide query_tag for inner search");
        }
        params.add(new HttpParameter("query_tag", queryTag));
        if (imageIndex.isEmpty()) {
            throw new RekognitionAPIException("You need to provide img_index for inner search");
        }
        params.add(new HttpParameter("img_index", imageIndex));
        if (!nameSpace.isEmpty()) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        if (!userId.isEmpty()) {
            params.add(new HttpParameter("user_id", userId));
        }
        if (numReturn != null) {
            params.add(new HttpParameter("num_return", numReturn));
        }
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.GET, new FaceInnerSearchAdapter());
        } catch (AdapterInitException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

    private List<HttpParameter> generateParams(List<FaceDetect.FaceDetectJobs> detectJobs, String url,
            byte[] base64_byte, String nameSpace, String userId, Integer numberReturn, List<String> tags, boolean needDetect) throws IOException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        StringBuilder sb = new StringBuilder("face_search");
        if (tags != null && !tags.isEmpty()) {
            sb.append(concatTags(tags));
        }
        if (needDetect) {
            if (detectJobs != null && !detectJobs.isEmpty()) {
                sb.append("_").append(concatJobs(detectJobs));
            }
        } else {
            sb.append("_nodetect");
        }
        params.add(new HttpParameter("jobs", sb.toString()));
        if (!url.isEmpty()) {
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
        return params;
    }

    private String concatJobs(List<FaceDetect.FaceDetectJobs> jobs) {
        StringBuilder sb = new StringBuilder();
        for (FaceDetect.FaceDetectJobs job : jobs) {
            sb.append(job.getValue()).append("_");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private String concatTags(List<String> tags) {
        return "[" + TextUtils.join(":", tags) + "]";
    }

}

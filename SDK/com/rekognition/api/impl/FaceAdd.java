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


public class FaceAdd extends AbstractRekognitionAPI {


    private enum FaceAddJobs {
        FaceAdd {
            @Override
            public String getValue() {
                return "face_add";
            }
        },
        Aggressive {
            @Override
            public String getValue() {
                return "aggressive";
            }
        },
        NoDetect {
            @Override
            public String getValue() {
                return "nodetect";
            }
        };

        public abstract String getValue();
    }

    public FaceAdd(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public FaceAdapter getResponse(String pictureUrl, boolean aggessive, boolean nodetect, 
            String nameSpace, String userId, String tag) throws RekognitionAPIException, IOException {
        List<HttpParameter> params = generateParameters(pictureUrl, aggessive, nodetect, nameSpace, userId, tag, null);
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceAdd.class.toString(), ex.getMessage());
            return null;
        }
    }

    public FaceAdapter getResponse(byte[] base64_byte, boolean aggessive, boolean nodetect, 
            String nameSpace, String userId, String tag) throws RekognitionAPIException, IOException {
        List<HttpParameter> params = generateParameters(null, aggessive, nodetect, nameSpace, userId, tag, base64_byte);
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceAdd.class.toString(), ex.getMessage());
            return null;
        }
    }

    private List<HttpParameter> generateParameters(String pictureUrl, boolean aggessive, 
            boolean nodetect, String nameSpace, String userId, String tag, byte[] base64_byte) throws IOException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        HttpParameter jobParam = new HttpParameter("jobs", getJobsString(aggessive, nodetect));
        params.add(jobParam);
        if (pictureUrl != null) {
            params.add(new HttpParameter("urls", pictureUrl));
        }
        if (nameSpace != null) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        if (userId != null) {
            params.add(new HttpParameter("user_id", userId));
        }
        if (tag != null) {
            params.add(new HttpParameter("tag", tag));
        }
        if (base64_byte != null) {
            params.add(new HttpParameter("base64", new HttpParameter.Base64Field(base64_byte)));
        }
        return params;
    }

    private String getJobsString (boolean aggessive, boolean nodetect) {
        List<FaceAddJobs> jobs = new ArrayList<FaceAddJobs>();
        jobs.add(FaceAddJobs.FaceAdd);
        if (aggessive) {
            jobs.add(FaceAddJobs.Aggressive);
        }
        if (nodetect) {
            jobs.add(FaceAddJobs.NoDetect);
        }
        return concatJobs(jobs);
    }

    private String concatJobs (List<FaceAddJobs> jobs) {
        StringBuilder sb = new StringBuilder();
        for (FaceAddJobs job : jobs) {
            sb.append(job.getValue()).append("_");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}

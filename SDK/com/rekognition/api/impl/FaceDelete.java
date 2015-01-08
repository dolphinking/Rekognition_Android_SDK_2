package com.rekognition.api.impl;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.JsonResponseAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.text.TextUtils;


public class FaceDelete extends AbstractRekognitionAPI {

    public FaceDelete(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public JsonResponseAdapter getResponse (String nameSpace, String userId, String tag, List<String> imageIndex) throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter("jobs", "face_delete"));
        if (!nameSpace.isEmpty()) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        if (!userId.isEmpty()) {
            params.add(new HttpParameter("user_id", userId));
        }
        if (!tag.isEmpty()) {
            params.add(new HttpParameter("tag", tag));
        }
        if (imageIndex != null && !imageIndex.isEmpty()) {
            params.add(new HttpParameter("img_index", TextUtils.join(";", imageIndex)));
        }
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.GET, new JsonResponseAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceDelete.class.toString(), ex.getMessage());
            return null;
        }
    }

}

package com.rekognition.api.impl;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.JsonResponseAdapter;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class FaceRename extends AbstractRekognitionAPI {

    public FaceRename(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }

    public JsonResponseAdapter getResponse (String existingTagName, String newTagName, String nameSpace, 
            String userId, String imageIndex) throws RekognitionAPIException {
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        if (existingTagName.isEmpty()) {
            throw new RekognitionAPIException("You have to provide the existing tag name in rename API");
        }
        if (newTagName.isEmpty()) {
            throw new RekognitionAPIException("You have to provide the new tag name in rename API");
        }
        params.add(new HttpParameter("jobs", "face_rename"));
        params.add(new HttpParameter("tag", existingTagName));
        params.add(new HttpParameter("new_tag", newTagName));
        if (!nameSpace.isEmpty()) {
            params.add(new HttpParameter("name_space", nameSpace));
        }
        if (!userId.isEmpty()) {
            params.add(new HttpParameter("user_id", userId));
        }
        if (!imageIndex.isEmpty()) {
            params.add(new HttpParameter("img_index", imageIndex));
        }
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new JsonResponseAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceRename.class.toString(), ex.getMessage());
            return null;
        }
    }

}

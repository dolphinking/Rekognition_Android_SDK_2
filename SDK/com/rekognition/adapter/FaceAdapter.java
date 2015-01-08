package com.rekognition.adapter;

import com.rekognition.adapter.model.Face;
import com.rekognition.api.impl.LocalFaceDetector.FaceInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class FaceAdapter extends JsonResponseAdapter {

    private static final String FACE_DETECTION_KEY = "face_detection";

    private JSONArray faceDetectedJsonArray = null;
    private final List<Face> faces = new ArrayList<Face>();

    @Override
    public void setResponseString(String responseStr) throws AdapterInitException {
        super.setResponseString(responseStr);
        try {
            this.faceDetectedJsonArray = this.getJsonObject().getJSONArray(FACE_DETECTION_KEY);
        } catch (JSONException ex) {
            Log.e(FaceAdapter.class.toString() ,"Cannot get certain field from json object" + responseStr, ex);
            throw new AdapterInitException("Cannot get certain field from json object");
        }
    }

    public List<Face> getFaces(FaceInfo[] faceInfoList) {       
        return getFaces(new ArrayList<FaceInfo>(Arrays.asList(faceInfoList)));
    }
    
    public List<Face> getFaces(List<FaceInfo> faceInfoList) {
        if (this.faces.isEmpty()) {
            if (this.faceDetectedJsonArray != null) {
                for (int i = 0 ; i < this.faceDetectedJsonArray.length() ; i ++) {
                    JSONObject faceObj;
                    try {
                        faceObj = this.faceDetectedJsonArray.getJSONObject(i);
                        Face face = new Face();
                        if (faceInfoList!=null && !faceInfoList.isEmpty() && (i < faceInfoList.size())) {
                            face.loadDataFromJSONObjectAndOffset(faceObj, faceInfoList.get(i));
                        }
                        else
                        {
                            face.loadDataFromJSONObject(faceObj);
                        }
                        this.faces.add(face);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();            
                    }
                }
            }
        }
        return this.faces;
    }

    public List<Face> getFaces() {
        return getFaces(new ArrayList<FaceInfo>());
    }
    
}

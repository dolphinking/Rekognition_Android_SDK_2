package com.rekognition.api.impl;

import com.rekognition.adapter.AdapterInitException;
import com.rekognition.adapter.FaceAdapter;
import com.rekognition.adapter.model.Face;
import com.rekognition.api.AbstractRekognitionAPI;
import com.rekognition.api.impl.LocalFaceDetector.FaceInfo;
import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.graphics.Bitmap;


public class FaceDetect extends AbstractRekognitionAPI {
    
    public static enum FaceDetectJobs {
        Face {
            @Override
            public String getValue() {
                return "face";
            }
        },
        Aggressive {
            @Override
            public String getValue() {
                return "aggressive";
            }
        },
        Part {
            @Override
            public String getValue() {
                return "part";
            }
        },
        PartDetail {
            @Override
            public String getValue() {
                return "part_detail";
            }
        },
        Gender {
            @Override
            public String getValue() {
                return "gender";
            }
        },
        Emotion {
            @Override
            public String getValue() {
                return "emotion";
            }
        },
        Race {
            @Override
            public String getValue() {
                return "race";
            }
        },
        Age {
            @Override
            public String getValue() {
                return "age";
            }
        },
        Glass {
            @Override
            public String getValue() {
                return "glass";
            }
        },
        MouthOpenWide {
            @Override
            public String getValue() {
                return "mouth_open_wide";
            }
        },
        EyeClosed {
            @Override
            public String getValue() {
                return "eye_closed";
            }
        },
        NoDetect {
            @Override
            public String getValue() {
                return "no_detect";
            }
        };

        public abstract String getValue();
    }

    public FaceDetect(String apiKey, String apiSecret) {
        super(apiKey, apiSecret);
    }
    
    public FaceAdapter getResponse(List<FaceDetectJobs> jobs, String pictureUrl) throws RekognitionAPIException {
        if (!jobs.contains(FaceDetectJobs.Face)) {
            jobs.add(FaceDetectJobs.Face);
        }
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        HttpParameter jobParam = new HttpParameter("jobs", concatJobs(jobs));
        //System.out.println("=======Jobs : " + concatJobs(jobs));
        HttpParameter urlParam = new HttpParameter("urls", pictureUrl);
        params.add(jobParam);
        params.add(urlParam);
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.GET, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceDetect.class.toString(), ex.getMessage());
            return null;
        }
    }

    public FaceAdapter getResponse(List<FaceDetectJobs> jobs, byte[] imageContent) throws RekognitionAPIException {
        if (!jobs.contains(FaceDetectJobs.Face)) {
            jobs.add(FaceDetectJobs.Face);
        }
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        HttpParameter jobParam = new HttpParameter("jobs", concatJobs(jobs));

        HttpParameter base64Param = new HttpParameter("base64", new HttpParameter.Base64Field(imageContent));
        params.add(jobParam);
        params.add(base64Param);
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceDetect.class.toString(), ex.getMessage());
            return null;
        }
    }
    
    public FaceAdapter getResponse(List<FaceDetectJobs> jobs, Bitmap bitmap) throws RekognitionAPIException, IOException {
        if (!jobs.contains(FaceDetectJobs.Face)) {
            jobs.add(FaceDetectJobs.Face);
        }
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        HttpParameter jobParam = new HttpParameter("jobs", concatJobs(jobs));
        params.add(jobParam);
        params.add(new HttpParameter("uploaded_file", new HttpParameter.BitmapField(bitmap)));        
        try {
            return this.perform(REKO_API_HOST_NAME, params, HttpMethod.POST, new FaceAdapter());
        } catch (AdapterInitException ex) {
            Log.e(FaceDetect.class.toString(), ex.getMessage());
            return null;
        }
    }

    private LocalFaceDetector mAndroidFaceDetector = null;
    
    private LocalFaceDetector getAndroidFaceDetector() {
        if ( mAndroidFaceDetector == null) {
            mAndroidFaceDetector = new LocalFaceDetector();
        }
        return mAndroidFaceDetector;
    }

    public List<Face> getFacesUsingLocalDetectorSerial(List<FaceDetectJobs> jobs, Bitmap bmp) throws RekognitionAPIException, IOException {
        FaceInfo[] face_infos = getAndroidFaceDetector().detectFace(bmp);
        jobs.add(FaceDetectJobs.NoDetect);
        List<Face> faceArray = new ArrayList<Face>();
        Bitmap[] faceBitmaps = new Bitmap[10];
        int nFaceFound = 0;
        for (int i=0; i<LocalFaceDetector.MAX_FACES; i++) {
            FaceInfo info = face_infos[i];
            if (info!=null){
                Bitmap faceBoundingBmp = info.faceBoundingBoxImage;
                faceBitmaps[nFaceFound]= faceBoundingBmp;
                nFaceFound += 1;
            }
        }

        if (nFaceFound > 0) {
            for (int i=0; i< nFaceFound; i++) {
                Bitmap foundBitmap = faceBitmaps[i];
                FaceAdapter resp = getResponse(jobs, foundBitmap);
                if (resp != null) {
                    faceArray.addAll(resp.getFaces());
                }
            }
        }
        return faceArray;
    }

    public List<Face> getFacesUsingLocalDetectorLocalParallel(List<FaceDetectJobs> jobs, Bitmap bmp) throws RekognitionAPIException, IOException {

        final FaceInfo[] face_infos = getAndroidFaceDetector().detectFace(bmp);
        jobs.add(FaceDetectJobs.NoDetect);
        List<Face> faceArray = new ArrayList<Face>();

        class FaceDetectRunnable implements Runnable {
            FaceDetectRunnable(List<Face> results, List<FaceDetectJobs> jobs, Bitmap faceBoundingBmp) {
                mResults = results;
                mJobs = jobs;
                mFaceBoundingBmp = faceBoundingBmp;
            }
            List<Face> mResults;
            List<FaceDetectJobs> mJobs;
            Bitmap mFaceBoundingBmp;
            @Override
            public void run() {
                Log.i("", String.valueOf(System.nanoTime()));
                FaceAdapter resp;
                try {
                    resp = getResponse(mJobs, mFaceBoundingBmp);
                    if (resp != null ) {
                        synchronized(mResults) {
                            mResults.addAll(resp.getFaces(face_infos));
                        }
                    }                    
                } catch (RekognitionAPIException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        // Kick off the requests in other thread
        List<Thread> threads = new ArrayList<Thread>();
        for (int i=0; i<LocalFaceDetector.MAX_FACES; i++) {
            FaceInfo info = face_infos[i];
            if (info!=null){
                Bitmap faceBoundingBmp = info.faceBoundingBoxImage;
                //Log.i("bmp size", String.format("%d, %d, %d", faceBoundingBmp.getWidth(), faceBoundingBmp.getHeight(), faceBoundingBmp.getByteCount()));
                FaceDetectRunnable faceDetect = new FaceDetectRunnable(faceArray, jobs, faceBoundingBmp);
                Thread tmpThread = new Thread(faceDetect);
                threads.add(tmpThread);
                tmpThread.start();
            }
        }

        // Wait for the requests to finish
        for (int i=0; i<threads.size(); i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return faceArray;
    }

    private String concatJobs (List<FaceDetectJobs> jobs) {
        StringBuilder sb = new StringBuilder();
        for (FaceDetectJobs job : jobs) {
            sb.append(job.getValue()).append("_");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        // Quality is another task, so seperate it with jobs with a comma;
        sb.append(",quality");
        return sb.toString();
    }    
}

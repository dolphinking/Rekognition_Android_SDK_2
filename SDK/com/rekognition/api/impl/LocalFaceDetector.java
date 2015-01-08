package com.rekognition.api.impl;

import java.util.HashMap;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;

public class LocalFaceDetector {
    
    public static class FaceInfo {
        public final Bitmap faceBoundingBoxImage;
        public final PointF topLeftPoint;
        public final double width, height;
        public final Face faceObj;
        public final double offsetFactor;
        FaceInfo(Bitmap boundingFaceImage,PointF _topLeft, double _width, double _height, Face face){
            faceBoundingBoxImage = boundingFaceImage;
            topLeftPoint = _topLeft;
            width = _width;
            height = _height;
            faceObj = face;
            offsetFactor = _width > _height ? _width / (double) boundingFaceImage.getWidth() : _height / (double) boundingFaceImage.getHeight(); 
        }
    }

    public static int MAX_FACES = 10;
    public static int IMAGE_PIXEL_WIDTH = 150;
    
    private HashMap<String, FaceDetector> mapFaceDetector = null;
    LocalFaceDetector(){
        mapFaceDetector = new HashMap<String, FaceDetector>();        
    }

    private FaceDetector getDetectorWithSize(int width, int height) {
        String key = String.format(Locale.US, "%d x %d", width, height);
        if (mapFaceDetector.containsKey(key) ) {
            return mapFaceDetector.get(key);
        }
        else {
            FaceDetector detector = new FaceDetector(width, height, MAX_FACES);
            mapFaceDetector.put(key, detector);
            return detector;
        }
    }
    public FaceInfo[] detectFace(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Log.d("image width and height: ", String.valueOf(width) + ", "+ String.valueOf(height));        
        long startTime = System.nanoTime();
        Face[] facesFound = new FaceDetector.Face[MAX_FACES];
        getDetectorWithSize(width, height).findFaces(bitmap, facesFound);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double seconds = (double)duration / 1000000000.0;
        Log.d("local face detect time used: ", String.valueOf(seconds));
        return getInfo(bitmap, facesFound);
    }

    private static FaceInfo[] getInfo(Bitmap bmp, Face[] faces) {
        FaceInfo[] face_info = new FaceInfo[MAX_FACES];        
        int face_index = 0;
        for (int i = 0; i< faces.length; i++) {
            Face face = faces[i];
            if (face != null) {
                if (face.confidence() > Face.CONFIDENCE_THRESHOLD) {
                    int eyeDistance = (int)face.eyesDistance();
                    PointF face_mid_point = new PointF();
                    int size = eyeDistance * 4;
                    face.getMidPoint(face_mid_point);
                    Log.d("mid point ", face_mid_point.toString());
                    PointF topLeft = getValidPoint(bmp, new PointF(face_mid_point.x - size/2, face_mid_point.y - size/2));
                    int boundingWidth = size;
                    int boudingHeight = size;
                    if (topLeft.x + size > bmp.getWidth()) {
                        boundingWidth = bmp.getWidth() - (int)topLeft.x;
                    }
                    if (topLeft.y + size > bmp.getHeight()) {
                        boudingHeight = bmp.getHeight() - (int)topLeft.y;
                    }
                    int boundBoxSize= boundingWidth > boudingHeight ? boudingHeight : boundingWidth;
                    int dstSize = boundBoxSize > IMAGE_PIXEL_WIDTH ? IMAGE_PIXEL_WIDTH : boundBoxSize;
                    Bitmap faceBoundingBox = Bitmap.createBitmap(bmp, (int)topLeft.x, (int)topLeft.y, boundBoxSize, boundBoxSize);
                    Log.d("LocalFaceDetector", String.format("topLeft x: %3.1f, %3.1f boudingBoxSize: %d", topLeft.x, topLeft.y, boundBoxSize));
                    Bitmap scaledBox = Bitmap.createScaledBitmap(faceBoundingBox, dstSize, dstSize, false);
                    face_info[face_index] = new FaceInfo(scaledBox, topLeft, faceBoundingBox.getWidth(), faceBoundingBox.getHeight(), face);
                    face_index++;
                }
                else {
                    Log.d("face low confidence: ", String.valueOf(face.confidence()));
                }
            }
        }
        Log.d("", "found " + String.valueOf(face_index) + " faces");
        return face_info;
    }

    private static PointF getValidPoint(Bitmap origBmp, PointF pnt) {
        if (pnt.x < 0) {
            pnt.x = 0;
        }

        if (pnt.y < 0) {
            pnt.y = 0;
        }

        if (pnt.x > origBmp.getWidth()) {
            pnt.x = origBmp.getWidth();
        }

        if (pnt.y > origBmp.getHeight()) {
            pnt.y = origBmp.getHeight();
        } 

        return pnt;

    }

}

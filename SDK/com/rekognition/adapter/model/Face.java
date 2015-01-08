package com.rekognition.adapter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.rekognition.api.impl.LocalFaceDetector.FaceInfo;


public class Face implements IRekognitionModel {

    private JSONObject faceObj;

    private EmotionMap emotionMap = null;
    private Pose pose = null;
    private Nose nose = null;
    private Mouth mouth = null;
    private Eyes eyes = null;
    private BoundingBox boundingBox = null;
    private Race race = null;
    private List<Match> matches = null;
    private FaceInfo localFaceInfo = null;
    private Quality quality = null;
    @Override
    public void loadDataFromJSONObject(JSONObject faceObj) {
        this.faceObj = faceObj;
    }
    
    public void loadDataFromJSONObjectAndOffset(JSONObject faceObj, FaceInfo faceInfo) {
        this.faceObj = faceObj;
        this.localFaceInfo = faceInfo;
    }

    public double getConfidence() throws FieldNotFoundException, JSONException {
        if (this.faceObj.has("confidence")) {
            return this.faceObj.getDouble("confidence");
        } else {
            throw new FieldNotFoundException("confidence");
        }
    }

    public double getSmile() throws FieldNotFoundException, JSONException {
        if (this.faceObj.has("smile")) {
            return this.faceObj.getDouble("smile");
        } else {
            throw new FieldNotFoundException("smile");
        }
    }

    public double getAge() throws FieldNotFoundException, JSONException {
        if (this.faceObj.has("age")) {
            return this.faceObj.getDouble("age");
        } else {
            throw new FieldNotFoundException("age");
        }
    }

    public double getGlasses() throws FieldNotFoundException, JSONException {
        if (this.faceObj.has("glasses")) {
            return this.faceObj.getDouble("glasses");
        } else {
            throw new FieldNotFoundException("glasses");
        }
    }

    public double getEyeClosed() throws FieldNotFoundException, JSONException {
        if (this.faceObj.has("eye_closed")) {

            return this.faceObj.getDouble("eye_closed");
        } else {
            throw new FieldNotFoundException("eye_closed");
        }
    }

    public boolean getEyeClosedBoolean() throws FieldNotFoundException, JSONException {
        return getEyeClosed() > 0.5;
    }

    public double getMouthOpenWide() throws FieldNotFoundException, JSONException {
        if (this.faceObj.has("mouth_open_wide")) {
            return this.faceObj.getDouble("mouth_open_wide");
        } else {
            throw new FieldNotFoundException("mouth_open_wide");
        }
    }

    public GenderType getGenderType() throws FieldNotFoundException, JSONException {
        Gender gender = new Gender(getGender());
        return gender.getGenderType();
    }
    public double getGender() throws FieldNotFoundException, JSONException {
        if (this.faceObj.has("sex")) {
            return this.faceObj.getDouble("sex");
        } else {
            throw new FieldNotFoundException("sex");
        }
    }

    public String getImageIndex() throws FieldNotFoundException, JSONException {
        if (this.faceObj.has("img_index")) {
            return this.faceObj.getString("img_index");
        } else {
            throw new FieldNotFoundException("img_index");
        }
    }

    public EmotionMap getEmotionMap() throws FieldNotFoundException {
        if (this.emotionMap == null) {
            this.emotionMap = new EmotionMap();
            this.emotionMap.loadDataFromJSONObject(faceObj);
        }
        return emotionMap;
    }
    
    public Quality getQuality() throws FieldNotFoundException {
        if (this.quality == null) {
            this.quality = new Quality();
            this.quality.loadDataFromJSONObject(faceObj);
        }
        return this.quality;
    }

    public Pose getPose() throws FieldNotFoundException {
        if (this.pose == null) {
            this.pose = new Pose();
            this.pose.loadDataFromJSONObject(faceObj);
        }
        return pose;
    }

    public Nose getNose() throws FieldNotFoundException {
        if (this.nose == null) {
            this.nose = new Nose();
            if (this.localFaceInfo != null) {
                this.nose.loadDataFromJSONObjectWithOffset(faceObj, this.localFaceInfo);
            }
            else {
                this.nose.loadDataFromJSONObject(faceObj);
            }            
        }
        return nose;
    }

    public Mouth getMouth() throws FieldNotFoundException {
        if (this.mouth == null) {
            this.mouth = new Mouth();
            if (this.localFaceInfo != null) {
                this.mouth.loadDataFromJSONObjectWithOffset(faceObj, this.localFaceInfo);
            }
            else {
                this.mouth.loadDataFromJSONObject(faceObj);
            }
        }
        return mouth;
    }

    public Eyes getEyes() throws FieldNotFoundException {
        if (this.eyes == null) {
            this.eyes = new Eyes();
            if (this.localFaceInfo != null) {
                this.eyes.loadDataFromJSONObjectWithOffset(faceObj, this.localFaceInfo);
            }
            else {
                this.eyes.loadDataFromJSONObject(faceObj);
            }            
        }
        return eyes;
    }

    public BoundingBox getBoundingBox() throws FieldNotFoundException {
        if (this.boundingBox == null) {
            this.boundingBox = new BoundingBox();
            if (this.localFaceInfo != null) {
                this.boundingBox.loadDataFromJSONObjectWithOffset(faceObj, this.localFaceInfo);
            }
            else {
                this.boundingBox.loadDataFromJSONObject(faceObj);
            }            
            
        }
        return boundingBox;
    }

    public Race getRace() throws FieldNotFoundException {
        if (this.race == null) {
            this.race = new Race();
            this.race.loadDataFromJSONObject(faceObj);
        }
        return race;
    }

    public List<Match> getMatches() throws FieldNotFoundException {
        if (this.matches == null) {
            this.matches = new ArrayList<Match>();
            try {
                JSONArray mathcesArray = this.faceObj.getJSONArray("matches");
                for (int i = 0 ; i < mathcesArray.length() ; i ++) {
                    JSONObject matchObj = mathcesArray.getJSONObject(i);
                    Match match = new Match();
                    match.loadDataFromJSONObject(matchObj);
                    this.matches.add(match);
                }
            } catch (Exception ex) {
                throw new FieldNotFoundException("matches");
            }
        }
        return matches;
    }

    public static class Match implements IRekognitionModel {

        private JSONObject matchObj;

        private String tag = null;
        private Double score = null;
        private String imageIndex = null;

        @Override
        public void loadDataFromJSONObject(JSONObject matchObj) {
            this.matchObj = matchObj;
        }

        public String getTag() throws FieldNotFoundException, JSONException {
            if (this.tag == null) {
                if (this.matchObj.has("tag")) {
                    this.tag = this.matchObj.getString("tag");
                } else {
                    throw new FieldNotFoundException("matches.tag");
                }
            }
            return tag;
        }

        public Double getScore() throws FieldNotFoundException, NumberFormatException, JSONException {
            if (this.score == null) {
                if (this.matchObj.has("score")) {
                    this.score = Double.valueOf(matchObj.getString("score"));
                } else {
                    throw new FieldNotFoundException("matches.score");
                }
            }
            return this.score;
        }

        public String getImageIndex() throws FieldNotFoundException, JSONException {
            if (this.imageIndex == null) {
                if (this.matchObj.has("img_index")) {
                    this.imageIndex = matchObj.getString("img_index");
                } else {
                    throw new FieldNotFoundException("matches.img_index");
                }
            }
            return imageIndex;
        }

    }

    public class Race implements IRekognitionModel {

        private final Map<String, Double> raceMap = new HashMap<String, Double>();

        @Override
        public void loadDataFromJSONObject(JSONObject faceObj) throws FieldNotFoundException {
            try {
                JSONObject raceObj = faceObj.getJSONObject("race");
                Iterator<String> keysIter = raceObj.keys();
                while (keysIter.hasNext()) {
                    String key = keysIter.next();
                    this.getRaceMap().put(key, raceObj.getDouble(key));
                }
            } catch (Exception ex) {
                throw new FieldNotFoundException("race");
            }
        }

        /**
         * @return the raceMap
         */
        public Map<String, Double> getRaceMap() {
            return raceMap;
        }

    }

    public enum EmotionType {
        HAPPY("happy"),
        SAD("sad"),
        SURPRISED("surprised"),
        CALM("calm"),
        CONFUSED("confused"),
        DISGUST("disgust"),
        ANGRY("angry");

        private String _emotionName;
        EmotionType(String emotionName){
            _emotionName = emotionName;
        }

        public String toString(){
            return _emotionName;
        }

        public static EmotionType getEmotionType(String name) throws Exception {
            if (name.equals("happy")) {
                return HAPPY;
            }
            else if (name.equals("sad")) {
                return SAD;
            }
            else if (name.equals("surprised")) {
                return SURPRISED;
            }
            else if (name.equals("calm")) {
                return CALM;
            }
            else if (name.equals("confused")) {
                return CONFUSED;
            }
            else if (name.equals("disgust")) {
                return DISGUST;
            }
            else if (name.equals("angry")) {
                return ANGRY;
            }
            else {
                throw new Exception("No Emotion type " + name);
            }
        }
    }
    public class EmotionMap implements IRekognitionModel {

        private final Map<EmotionType, Double> emotionMap = new HashMap<EmotionType, Double>();

        @Override
        public void loadDataFromJSONObject(JSONObject faceObj) throws FieldNotFoundException {
            try {
                JSONObject emotionObj = faceObj.getJSONObject("emotion");
                Iterator<String> keysIter = emotionObj.keys();
                while (keysIter.hasNext()) {
                    String key = keysIter.next();
                    EmotionType emotionType = EmotionType.getEmotionType(key);
                    this.getEmotionMap().put(emotionType, emotionObj.getDouble(key));
                }
            } catch (Exception ex) {
                throw new FieldNotFoundException("emotion");
            }
        }

        /**
         * @return the emotionMap
         */
        public Map<EmotionType, Double> getEmotionMap() {
            return emotionMap;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            Iterator<Entry<EmotionType, Double>> iter = emotionMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<EmotionType, Double> entry = iter.next();
                sb.append(entry.getKey());
                sb.append('=').append('"');
                sb.append(entry.getValue());
                sb.append('"');
                if (iter.hasNext()) {
                    sb.append(',').append(' ');
                }
            }
            return sb.toString();

        }
    }
    
    public class Quality implements IRekognitionModel {
        
        private double brightness;
        private double sharp;
        @Override
        public void loadDataFromJSONObject(JSONObject faceObj) throws FieldNotFoundException {
            try {
                JSONObject qualityObj = faceObj.getJSONObject("quality");
                brightness = qualityObj.getDouble("brn");
                sharp = qualityObj.getDouble("shn");
            } catch (Exception ex) {
                throw new FieldNotFoundException("quality");
            }
        }

        public double getBrightness(){
            return brightness;
        }

        public double getSharpness(){
            return sharp;
        }       
    }

    public class Pose implements IRekognitionModel {

        private Double roll = null;
        private Double yaw = null;
        private Double pitch = null;

        /**
         * @return the roll
         */
        public Double getRoll() {
            return roll;
        }

        /**
         * @return the yaw
         */
        public Double getYaw() {
            return yaw;
        }

        /**
         * @return the pitch
         */
        public Double getPitch() {
            return pitch;
        }

        @Override
        public void loadDataFromJSONObject(JSONObject faceObj) throws FieldNotFoundException {
            try {
                JSONObject poseObj = faceObj.getJSONObject("pose");
                if (poseObj.has("roll")) {
                    this.roll = poseObj.getDouble("roll");
                }
                if (poseObj.has("yaw")) {
                    this.yaw = poseObj.getDouble("yaw");
                }
                if (poseObj.has("pitch")) {
                    this.pitch = poseObj.getDouble("pitch");
                }
            } catch (Exception ex) {
                throw new FieldNotFoundException("pose");
            }
        }
    }

    public class Nose implements IRekognitionModel {

        private double noseX;
        private double noseY;

        /**
         * @return the noseX
         */
        public double getNoseX() {
            return noseX;
        }

        /**
         * @return the noseY
         */
        public double getNoseY() {
            return noseY;
        }

        public void loadDataFromJSONObjectWithOffset(JSONObject faceObj, FaceInfo offset) throws FieldNotFoundException {
            try {
                this.noseX = faceObj.getJSONObject("nose").getDouble("x") * offset.offsetFactor + offset.topLeftPoint.x;
                this.noseY = faceObj.getJSONObject("nose").getDouble("y") * offset.offsetFactor + offset.topLeftPoint.y ;
            } catch (Exception ex) {
                throw new FieldNotFoundException("nose");
            }
        }
        
        @Override
        public void loadDataFromJSONObject(JSONObject faceObj) throws FieldNotFoundException {
            try {
                this.noseX = faceObj.getJSONObject("nose").getDouble("x");
                this.noseY = faceObj.getJSONObject("nose").getDouble("y");
            } catch (Exception ex) {
                throw new FieldNotFoundException("nose");
            }
        }

    }

    public class Mouth implements IRekognitionModel {

        private double mouthLeftX;
        private double mouthLeftY;
        private double mouthRightX;
        private double mouthRightY;

        /**
         * @return the mouthLeftX
         */
        public double getMouthLeftX() {
            return mouthLeftX;
        }

        /**
         * @return the mouthLeftY
         */
        public double getMouthLeftY() {
            return mouthLeftY;
        }

        /**
         * @return the mouthRightX
         */
        public double getMouthRightX() {
            return mouthRightX;
        }

        /**
         * @return the mouthRightY
         */
        public double getMouthRightY() {
            return mouthRightY;
        }

        public void loadDataFromJSONObjectWithOffset(JSONObject faceObj, FaceInfo offset) throws FieldNotFoundException {
            try {
                JSONObject leftMouthObj = faceObj.getJSONObject("mouth_l");
                JSONObject rightMouthObj = faceObj.getJSONObject("mouth_r");
                this.mouthLeftX = leftMouthObj.getDouble("x") * offset.offsetFactor + offset.topLeftPoint.x;
                this.mouthLeftY = leftMouthObj.getDouble("y") * offset.offsetFactor + offset.topLeftPoint.y;
                this.mouthRightX = rightMouthObj.getDouble("x") * offset.offsetFactor + offset.topLeftPoint.x;
                this.mouthRightY = rightMouthObj.getDouble("y") * offset.offsetFactor + offset.topLeftPoint.y;
            } catch (Exception ex) {
                throw new FieldNotFoundException("mouth");
            }
        }
        
        @Override
        public void loadDataFromJSONObject(JSONObject faceObj) throws FieldNotFoundException {
            try {
                JSONObject leftMouthObj = faceObj.getJSONObject("mouth_l");
                JSONObject rightMouthObj = faceObj.getJSONObject("mouth_r");
                this.mouthLeftX = leftMouthObj.getDouble("x");
                this.mouthLeftY = leftMouthObj.getDouble("y");
                this.mouthRightX = rightMouthObj.getDouble("x");
                this.mouthRightY = rightMouthObj.getDouble("y");
            } catch (Exception ex) {
                throw new FieldNotFoundException("mouth");
            }
        }
    }

    public class Eyes implements IRekognitionModel {

        private double leftEyeX;
        private double leftEyeY;
        private double rightEyeX;
        private double rightEyeY;

        /**
         * @return the leftEyeX
         */
        public double getLeftEyeX() {
            return leftEyeX;
        }

        /**
         * @return the leftEyeY
         */
        public double getLeftEyeY() {
            return leftEyeY;
        }

        /**
         * @return the rightEyeX
         */
        public double getRightEyeX() {
            return rightEyeX;
        }

        /**
         * @return the rightEyeY
         */
        public double getRightEyeY() {
            return rightEyeY;
        }

        public void loadDataFromJSONObjectWithOffset(JSONObject faceObj, FaceInfo offset) throws FieldNotFoundException {
            try {
                JSONObject leftEyeObject = faceObj.getJSONObject("eye_left");
                JSONObject rightEyeObject = faceObj.getJSONObject("eye_right");
                this.leftEyeX = leftEyeObject.getDouble("x") * offset.offsetFactor + offset.topLeftPoint.x;
                this.leftEyeY = leftEyeObject.getDouble("y") * offset.offsetFactor + offset.topLeftPoint.y;
                this.rightEyeX = rightEyeObject.getDouble("x") * offset.offsetFactor + offset.topLeftPoint.x;
                this.rightEyeY = rightEyeObject.getDouble("y") * offset.offsetFactor + offset.topLeftPoint.y;
            } catch (Exception ex) {
                throw new FieldNotFoundException("eyes");
            }
            
        }
        @Override
        public void loadDataFromJSONObject(JSONObject faceObj) throws FieldNotFoundException {
            try {
                JSONObject leftEyeObject = faceObj.getJSONObject("eye_left");
                JSONObject rightEyeObject = faceObj.getJSONObject("eye_right");
                this.leftEyeX = leftEyeObject.getDouble("x");
                this.leftEyeY = leftEyeObject.getDouble("y");
                this.rightEyeX = rightEyeObject.getDouble("x");
                this.rightEyeY = rightEyeObject.getDouble("y");
            } catch (Exception ex) {
                throw new FieldNotFoundException("eyes");
            }
        }

    }

    public enum GenderType {
        MALE, FEMALE 
    }
    public class Gender {          
        private double gender_probability;
        Gender(double _gender) {
            gender_probability = _gender;
        }
        public GenderType getGenderType() {
            if (gender_probability > 0.5) {
                return GenderType.MALE;
            }
            else {
                return GenderType.FEMALE;
            }
        }
        public double getGenderProb(){
            return gender_probability;
        }        
    }

    public class BoundingBox implements IRekognitionModel {

        private double topLeftX;
        private double topLeftY;
        private double width;
        private double height;

        /**
         * @return the topLeftX
         */
         public double getTopLeftX() {
             return topLeftX;
         }

         /**
          * @return the topLeftY
          */
         public double getTopLeftY() {
             return topLeftY;
         }

         /**
          * @return the width
          */
         public double getWidth() {
             return width;
         }

         /**
          * @return the height
          */
         public double getHeight() {
             return height;
         }

         public void loadDataFromJSONObjectWithOffset(JSONObject faceObj, FaceInfo offset) throws FieldNotFoundException {
             try {
                 JSONObject boundingBoxObj = faceObj.getJSONObject("boundingbox");
                 JSONObject topLeftObj = boundingBoxObj.getJSONObject("tl");
                 JSONObject sizeObj = boundingBoxObj.getJSONObject("size");
                 this.topLeftX = topLeftObj.getDouble("x") * offset.offsetFactor + offset.topLeftPoint.x;
                 this.topLeftY = topLeftObj.getDouble("y") * offset.offsetFactor + offset.topLeftPoint.y;
                 this.height = sizeObj.getDouble("height") * offset.offsetFactor;
                 this.width = sizeObj.getDouble("width") * offset.offsetFactor;
             } catch (Exception ex) {
                 throw new FieldNotFoundException("boudingbox");
             }
         }
         @Override
         public void loadDataFromJSONObject(JSONObject faceObj) throws FieldNotFoundException {
             try {
                 JSONObject boundingBoxObj = faceObj.getJSONObject("boundingbox");
                 JSONObject topLeftObj = boundingBoxObj.getJSONObject("tl");
                 JSONObject sizeObj = boundingBoxObj.getJSONObject("size");
                 this.topLeftX = topLeftObj.getDouble("x");
                 this.topLeftY = topLeftObj.getDouble("y");
                 this.height = sizeObj.getDouble("height");
                 this.width = sizeObj.getDouble("width");
             } catch (Exception ex) {
                 throw new FieldNotFoundException("boudingbox");
             }
         }
    }
}

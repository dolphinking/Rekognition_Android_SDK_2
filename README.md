# Rekognition_Android_SDK_2

Rekognition Android SDK version 2.0

Sample:

    List<FaceDetectJobs> faceDetectJobs = new ArrayList<FaceDetectJobs>();
    faceDetectJobs.add(FaceDetectJobs.Age);
    faceDetectJobs.add(FaceDetectJobs.Aggressive);
    faceDetectJobs.add(FaceDetectJobs.Emotion);
    faceDetectJobs.add(FaceDetectJobs.EyeClosed);
    faceDetectJobs.add(FaceDetectJobs.Gender);
    faceDetectJobs.add(FaceDetectJobs.Part);
    faceDetectJobs.add(FaceDetectJobs.MouthOpenWide);
    
    FaceDetect instance = new FaceDetect(REKO_API_KEY, REKO_API_SECRET);
    FaceAdapter resp = instance.getResponse(faceDetectJobs, bitmap);
    List<Face> faces = resp.getFaces();
    
        

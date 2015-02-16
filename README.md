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
    
Concept Sample:
        Bitmap bitmap = getLocalBitmap();
        ConceptDetect instance = new ConceptDetect(REKO_API_KEY, REKO_API_SECRET);
        try {
            ConceptAdapter result = instance.getResposne(bitmap, 3000);
            List<Concept> concepts = result.getConcepts();
            for (Concept concept:concepts) {
                try {
                    Log.i("Concept: ", String.format("%s: %3.2f", concept.getTag(), concept.getScore()));
                } catch (FieldNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (RekognitionAPIException e) {
            e.printStackTrace();
        }

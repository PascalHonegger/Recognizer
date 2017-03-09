package com.example.informatik.cognitizer.Tasks;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.microsoft.cognitive.speakerrecognition.SpeakerIdentificationClient;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Identification;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.OperationLocation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Profile;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Category;
import com.microsoft.projectoxford.vision.contract.Face;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.informatik.cognitizer.Tasks.Constants.TASK_DELAY;

public class DescribeImageTask extends AsyncTask<Bitmap, Void, AnalysisResult> {
    // Store error message
    private Exception e = null;
    private VisionServiceClient visionServiceClient;

    public DescribeImageTask(VisionServiceClient visionServiceClient) {
        this.visionServiceClient = visionServiceClient;
    }

    @Override
    protected AnalysisResult doInBackground(Bitmap... args) {
        try {
            // Put the image into an input stream for detection.
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            args[0].compress(Bitmap.CompressFormat.JPEG, 100, output);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

            AnalysisResult analysisResult = visionServiceClient.describe(inputStream, 1);

            Log.d("result", analysisResult.description.captions.get(0).text);

            return analysisResult;
        } catch (Exception e) {
            this.e = e;    // Store error
        }

        return null;
    }

    @Override
    protected void onPostExecute(AnalysisResult data) {
        super.onPostExecute(data);
        if (e != null) {
            //TODO Exception handler?
            e.printStackTrace();
        }
    }
}
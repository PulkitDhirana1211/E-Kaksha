package com.example.ekaksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ExaminationActivity extends AppCompatActivity implements CameraXConfig.Provider {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private FaceNetModel model;
    private HashMap<String, float[]> registered = new HashMap<>();
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        start();

        startService(new Intent(this, BroadcastService.class));
        previewView = (PreviewView) findViewById(R.id.camera_preview);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("updated","called");
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());
        Size size = new Size(previewView.getWidth(), previewView.getHeight());
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(size)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(AsyncTask.THREAD_POOL_EXECUTOR, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                @SuppressLint("UnsafeExperimentalUsageError") Image image1 = image.getImage();
                int rot = image.getImageInfo().getRotationDegrees();
                Log.v("whvdqd", "jkwd " + rot);
                FaceDetectorOptions realTimeOpts =
                        new FaceDetectorOptions.Builder()
                                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                                .build();

                Image.Plane[] planes = image1.getPlanes();
                if (planes.length >= 3) {
                    for (Image.Plane plane : planes) {
                        plane.getBuffer().rewind();
                    }
                }
                Bitmap bitmap1 = toBitmap(image1);
                InputImage firebaseVisionImage1 = InputImage.fromMediaImage(image1, rot);
                FaceDetector detector = FaceDetection.getClient(realTimeOpts);
                final String[] label = new String[1];
                final float[] distance = new float[1];
                detector.process(firebaseVisionImage1).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> firebaseVisionFaces) {
                        Log.e("face detect", "No " + firebaseVisionFaces.size());
                        if (firebaseVisionFaces.size() != 1) {



                          findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);


                        }
                        else if (registered.size() > 0) {
                            float[] embiddings = model.getFaceEmbedding(bitmap1, firebaseVisionFaces.get(0).getBoundingBox(), false);
                            //LOGGER.i("dataset SIZE: " + registered.size());
                            final Pair<String, Float> nearest = findNearest(embiddings);
                            if (nearest != null) {

                                final String name = nearest.first;
                                label[0] = name;
                                if(name.equals("shivam"))
                                    findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                                else
                                    findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);

                                distance[0] = nearest.second;

                                Log.v("result", label[0] + distance[0]);


                            }
                        }
                        else
                            findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);


                        image.close();
                        detector.close();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("error", "failure");
                        image.close();
                        detector.close();
                    }
                });


            }
        });


        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
    }


    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
    private Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();
        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    public void start()
    {
        FaceDetectorOptions accurateOps = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build();
        model = new FaceNetModel(getBaseContext());
        try {
            File imagesDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/images");
            File[] imageSubDirs = imagesDir.listFiles();
            for (File file : imageSubDirs) {
                for (File imagename : file.listFiles()) {
                    String path = imagename.getAbsolutePath();
                    Log.v("path", path);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    //  Bitmap finalBitmap=Bitmap.createScaledBitmap(bitmap,360,360,false);
                    Log.v("shivam", "gupta");
                    InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
                    Log.v("shivam", "gupta111");
                    FaceDetector detector = FaceDetection.getClient(accurateOps);

                    detector.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                        @Override
                        public void onSuccess(List<Face> faces) {
                            Log.v("storage sizes", "w1jd" + faces.size());
                            if (faces.size() == 1) {

                                float[] embiddings = model.getFaceEmbedding(bitmap, faces.get(0).getBoundingBox(), true);
                                //   float[] embiddings =onFacesDetected(bitmap,faces.get(0));
                              //  registered.put(file.getName(), embiddings);
                                Log.v("filename", file.getName());
                                registered.put(file.getName(), embiddings);
                                detector.close();


                            }
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }






        //  frameAnalyser=new FrameAnalyser(this,button);


    }
    private Pair<String, Float> findNearest(float[] emb) {

        Pair<String, Float> ret = null;
        for (Map.Entry<String, float[]> entry : registered.entrySet()) {
            final String name = entry.getKey();
            final float[] knownEmb = ((float[]) entry.getValue());

            float distance = 0;
            for (int i = 0; i < emb.length; i++) {
                float diff = emb[i] - knownEmb[i];
                distance += diff*diff;
            }
            distance = (float) Math.sqrt(distance);
            if (ret == null || distance < ret.second) {
                ret = new Pair<>(name, distance);
            }
        }

        return ret;

    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i("timer", "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i("timer", "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            if (millisUntilFinished == 0)
                stopService(new Intent(this, BroadcastService.class));
            timer=(TextView)findViewById(R.id.maintimer) ;
            timer.setText(millisUntilFinished+"");
            //timer.setText(millisUntilFinished/60+":"+millisUntilFinished%60);
            Log.i("timer", "Countdown seconds remaining: " + millisUntilFinished / 1000);
        }
    }


}
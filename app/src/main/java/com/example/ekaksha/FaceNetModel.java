package com.example.ekaksha;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FaceNetModel {
    FaceNetModel(Context mContext) {
        this.context = mContext;
        Interpreter.Options options = new Interpreter.Options().setNumThreads(4);


        {
            try {
                interpreter = new Interpreter(FileUtil.loadMappedFile(context, "mobile_face_net.tflite"), options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Interpreter interpreter;

    private Context context;

    int imgSize = 112;


    public float[] getFaceEmbedding(Bitmap image, Rect crop, Boolean preRotate) {
        ByteBuffer byteBuffer = convertBitmapToBuffer(cropRectFromBitmap(image, crop, preRotate));
        return runFaceNet(byteBuffer)[0];
    }

    // Run the FaceNet model.
    private float[][] runFaceNet(ByteBuffer byteBuffer) {
        long t1 = System.currentTimeMillis();
        float[][] output = new float[1][192];
        interpreter.run(byteBuffer, output);
        Log.i("Performance", "FaceNet Inference Speed in ms : ${System.currentTimeMillis() - t1}");
        return output;
    }

    // Resize the given bitmap and convert it to a ByteBuffer
    private ByteBuffer convertBitmapToBuffer(Bitmap image) {
        ByteBuffer imageByteBuffer = ByteBuffer.allocateDirect(1 * imgSize * imgSize * 3 * 4);
        imageByteBuffer.order(ByteOrder.nativeOrder());
        Bitmap resizedImage = Bitmap.createScaledBitmap(image, imgSize, imgSize, false);
        for (int x = 0; x < imgSize; x++) {
            for (int y = 0; y < imgSize; y++) {
                int pixelValue = resizedImage.getPixel(x, y);
                imageByteBuffer.putFloat((((pixelValue >> 16 & 0xFF) - 128f) / 128f));
                imageByteBuffer.putFloat((((pixelValue >> 8 & 0xFF) - 128f) / 128f));
                imageByteBuffer.putFloat((((pixelValue & 0xFF) - 128f) / 128f));
            }
        }
        return imageByteBuffer;
    }

    // Crop the given bitmap with the given rect.
    private Bitmap cropRectFromBitmap(Bitmap source, Rect rect, Boolean preRotate) {
        if (!preRotate)
            source = rotateBitmap(source, 270f);
        Bitmap cropped = Bitmap.createBitmap(source,
                rect.left,
                rect.top,
                rect.width(),
                rect.height()
        );
        return cropped;
    }

    private Bitmap rotateBitmap(Bitmap source, Float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

    private float cosineSimilarity(float[] x1, float[] x2) {
        float dotProduct = 0.0f;
        float mag1 = 0.0f;
        float mag2 = 0.0f;
        for (int i = 0; i < x1.length; i++) {
            dotProduct += (x1[i] * x2[i]);
            mag1 += (float) Math.pow(x1[i], 2);
            mag2 += (float) Math.pow(x2[i], 2);
        }
        mag1 = (float) Math.sqrt(mag1);
        mag2 = (float) Math.sqrt(mag2);

        return dotProduct / ( mag1 * mag2 );
    }
}


package com.example.mobilenangcao;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import android.content.res.AssetFileDescriptor;
import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

public class IntentClassifier {
    private Interpreter tflite;
    private Context context;
    private Map<String, Integer> wordIndex;
    private List<String> labels;

    public IntentClassifier(Context context) {
        this.context = context;
        try {
            String[] assetFiles = context.getAssets().list("");
            if (assetFiles != null && assetFiles.length > 0) {
                Log.d("IntentClassifier", "Files in assets:");
                for (String file : assetFiles) {
                    Log.d("IntentClassifier", " - " + file);
                }
            } else {
                Log.w("IntentClassifier", "No files found in assets or assets is empty");
            }
            tflite = new Interpreter(loadModelFile());
            loadWordIndex();
            loadLabels();
        } catch (IOException e) {
            Log.e("IntentClassifier", "Initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        try {
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd("behavior_model.tflite");
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        } catch (IOException e) {
            Log.e("IntentClassifier", "Failed to load behavior_model.tflite: " + e.getMessage());
            throw e; // Ném lại exception để xử lý ở cấp cao hơn
        }
    }

    private void loadWordIndex() throws IOException {
        try {
            InputStream inputStream = context.getAssets().open("tokenizer_word_index.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);

            if (json.isEmpty()) {
                Log.e("IntentClassifier", "tokenizer_word_index.json is empty");
                return;
            }

            Gson gson = new Gson();
            wordIndex = gson.fromJson(json, new TypeToken<Map<String, Integer>>(){}.getType());

            if (wordIndex == null) {
                Log.e("IntentClassifier", "Failed to parse tokenizer_word_index.json");
            } else {
                Log.d("IntentClassifier", "WordIndex loaded with " + wordIndex.size() + " entries");
            }
        } catch (IOException e) {
            Log.e("IntentClassifier", "Error loading tokenizer_word_index.json: " + e.getMessage());
            throw e;
        }
    }

    private void loadLabels() throws IOException {
        try {
            InputStream inputStream = context.getAssets().open("label_classes.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);

            if (json.isEmpty()) {
                Log.e("IntentClassifier", "label_classes.json is empty");
                return;
            }

            Gson gson = new Gson();
            labels = gson.fromJson(json, new TypeToken<List<String>>(){}.getType());

            if (labels == null) {
                Log.e("IntentClassifier", "Failed to parse label_classes.json");
            } else {
                Log.d("IntentClassifier", "Labels loaded with " + labels.size() + " entries");
            }
        } catch (IOException e) {
            Log.e("IntentClassifier", "Error loading label_classes.json: " + e.getMessage());
            throw e;
        }
    }

    public String predictIntent(String text) {
        if (wordIndex == null) {
            Log.e("IntentClassifier", "wordIndex is null, cannot predict intent");
            return "Error: Model not initialized";
        }
        if (labels == null) {
            Log.e("IntentClassifier", "labels is null, cannot predict intent");
            return "Error: Model not initialized";
        }

        String[] words = text.toLowerCase().split("\\s+");
        float[] input = new float[50];
        Arrays.fill(input, 0);

        for (int i = 0; i < Math.min(words.length, 50); i++) {
            Integer index = wordIndex.getOrDefault(words[i], 1); // 1 là <OOV>
            input[i] = index.floatValue();
        }

        float[][] inputTensor = new float[1][50];
        inputTensor[0] = input;

        float[][] output = new float[1][labels.size()];
        tflite.run(inputTensor, output);

        int predictedId = argMax(output[0]);
        return labels.get(predictedId);
    }

    private int argMax(float[] array) {
        int maxIdx = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIdx]) {
                maxIdx = i;
            }
        }
        return maxIdx;
    }

    public void close() {
        if (tflite != null) {
            tflite.close();
        }
    }
}
package com.example.mobilenangcao;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

public class VoiceService extends Service {
    private SpeechRecognizer speechRecognizer;

    @Override
    public void onCreate() {
        super.onCreate();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) { }

            @Override
            public void onBeginningOfSpeech() { }

            @Override
            public void onRmsChanged(float rmsdB) { }

            @Override
            public void onBufferReceived(byte[] buffer) { }

            @Override
            public void onEndOfSpeech() { }

            @Override
            public void onError(int error) { }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> recognitionResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (String result : recognitionResults) {
                    if (result.equalsIgnoreCase("Hey Assistant")) {
                        // Kích hoạt hành động khi nhận được wake word
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) { }

            @Override
            public void onEvent(int eventType, Bundle params) { }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say 'Hey Assistant' to activate.");
        speechRecognizer.startListening(recognizerIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        speechRecognizer.stopListening();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


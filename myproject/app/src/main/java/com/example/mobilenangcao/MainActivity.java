package com.example.mobilenangcao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private ImageButton keyboardButton;
    private ImageButton speechButton;
    private ImageButton settingButton;
    private Button sendButton;
    private LinearLayout inputLayout;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private CommandHandler commandHandler;

    private boolean isKeyboardVisible = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        keyboardButton = findViewById(R.id.keyboardButton);
        speechButton = findViewById(R.id.speechButton);
        settingButton = findViewById(R.id.settingButton);
        inputLayout = findViewById(R.id.inputLayout);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        textToSpeech = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });

        commandHandler = new CommandHandler(this);

        // Ban đầu ẩn input layout
        inputLayout.setVisibility(View.GONE);

        // Hiển thị input layout và bàn phím khi nhấn nút bàn phím
        keyboardButton.setOnClickListener(v -> {
            if (!isKeyboardVisible) {
                inputLayout.setVisibility(View.VISIBLE);
                editText.requestFocus();
                showKeyboard();
                isKeyboardVisible = true;
            }
        });

        speechButton.setOnClickListener(v -> startSpeechRecognition());

        settingButton.setOnClickListener(v -> openSettingsTab());

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    commandHandler.handleCommand(text);
                }
                editText.setText("");
                // Ẩn bàn phím và input layout
                hideKeyboard();
                inputLayout.setVisibility(View.GONE);
                isKeyboardVisible = false;

                return true;
            }
            return false;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // Status bar màu tím
            window.setStatusBarColor(Color.parseColor("#9BAFD9"));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // Navigation bar màu tím
            window.setNavigationBarColor(Color.parseColor("#103783"));
        }


        // Ẩn bàn phím và input layout khi người dùng nhấn ngoài
        findViewById(R.id.inputLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputLayout.setVisibility(View.GONE);
                isKeyboardVisible = false;
                return false;
            }
        });
    }

    private void showKeyboard() {
        // Hiển thị bàn phím
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void startSpeechRecognition() {
        // Ghi âm và nhận diện giọng nói
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói gì đó...");
        startActivityForResult(intent, 1);
    }

    private void openSettingsTab() {
        // Hiển thị thông báo "Tính năng đang phát triển"
        Toast.makeText(MainActivity.this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                editText.setText(spokenText);
                commandHandler.handleCommand(spokenText);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float x = ev.getRawX() + v.getLeft() - location[0];
                float y = ev.getRawY() + v.getTop() - location[1];

                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    hideKeyboard();
                    inputLayout.setVisibility(View.GONE);
                    isKeyboardVisible = false;
                    v.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}

package com.example.voicerecognizerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voicerecognizerapp.service.voice.VoiceRecognizerService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecognitionResultsListener {
    private static final String TAG = "MainActivity";

    private static final int VOICE_REQUEST_CODE = 123;
    private static final int PERMISSIONS_REQUEST_CODE = 345;
    private static final long DURATION = 200;
    private EditText etSearch;
    private Button btRecognizer;
    private VoiceRecognizerService recognizerService;
    private ConstraintLayout main_layout;
    private ImageView ivBtRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etSearch = findViewById(R.id.et_search);
        main_layout = findViewById(R.id.main_background);
        btRecognizer = findViewById(R.id.bt_recognizer);
        ivBtRecognizer = findViewById(R.id.iv_bt_recognizer);
        recognizerService = new VoiceRecognizerService(this,this);
        verificaPermicoes();
        recognizerService = new VoiceRecognizerService(getApplicationContext(),this);
        btRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voiceBtAnimationState1();
                btRecognizer.setClickable(false);
                initVoiceRecognizer();
            }
        });

        ivBtRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voiceBtAnimationState1();
                ivBtRecognizer.setClickable(false);
                initVoiceRecognizer();
            }
        });
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        view.clearFocus();
    }


    private void initVoiceRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        try {
            recognizerService.startListening(intent);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,"Reconhecimento de voz nao suportado",Toast.LENGTH_SHORT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                etSearch.setText(list.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void verificaPermicoes() {
        Log.i(TAG, "pedindo permiçoes para o usuario");
        String[] permisoes = {Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permisoes[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), permisoes[1]) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, permisoes, PERMISSIONS_REQUEST_CODE);
        }
    }

    private void voiceBtAnimationState1(){
        Log.d(TAG, "voiceBtAnimation: ");
        ValueAnimator anim = ValueAnimator.ofInt(btRecognizer.getMeasuredWidth(), +400);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = btRecognizer.getLayoutParams();
                btRecognizer.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.custom_round_bar));
                layoutParams.width = val;
                btRecognizer.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(DURATION);
        anim.start();
    }
    private void voiceBtAnimationState2(){
        Log.d(TAG, "voiceBtAnimation: ");
        ValueAnimator anim = ValueAnimator.ofInt(btRecognizer.getMeasuredWidth(), 144);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = btRecognizer.getLayoutParams();
                layoutParams.width = val;
                btRecognizer.setLayoutParams(layoutParams);
                if(val == 144)
                    btRecognizer.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_button));
            }
        });
        anim.setDuration(DURATION);
        anim.start();
    }

    @Override
    public void results(String txt) {
        etSearch.setText(txt);
    }

    @Override
    public void progressbarControler(int i) {
        voiceBtAnimationState2();
        btRecognizer.setClickable(true);
        ivBtRecognizer.setClickable(true);
    }
}

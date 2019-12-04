package com.example.voicerecognizerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.voicerecognizerapp.Listener.RecognitionResultsListener;
import com.example.voicerecognizerapp.adapter.ResultsAdapter;
import com.example.voicerecognizerapp.model.Item;
import com.example.voicerecognizerapp.service.retrofit.RetrofitService;
import com.example.voicerecognizerapp.service.retrofit.SearchService;
import com.example.voicerecognizerapp.service.voice.VoiceRecognizerService;
import com.example.voicerecognizerapp.ui.AnimationDrawableCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecognitionResultsListener{
    private static final String TAG = "MainActivity";

    private static final int VOICE_REQUEST_CODE = 123;
    private static final int PERMISSIONS_REQUEST_CODE = 345;
    private static final long DURATION = 200;
    private EditText etSearch;
    private Button btRecognizer;
    private VoiceRecognizerService recognizerService;
    private ConstraintLayout main_layout;
    private ImageView ivBtRecognizer;
    private RecyclerView rvResults;
    private ResultsAdapter mAdapter;
    private AnimationDrawable micAnimation;
    private AnimationDrawable micAnimation2;
    private AnimationDrawable micAnimation3;
    private Drawable.Callback mic_call_back;

    private RetrofitService mRetrofitService;
    private SearchService mSearchService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_01);

        rvResults = findViewById(R.id.rv_results);
        etSearch = findViewById(R.id.et_search);
        main_layout = findViewById(R.id.main_layout_01);
        btRecognizer = findViewById(R.id.bt_recognizer);
        ivBtRecognizer = findViewById(R.id.iv_bt_recognizer);


        recognizerService = new VoiceRecognizerService(this,this);
        verificaPermicoes();
        recognizerService = new VoiceRecognizerService(getApplicationContext(),this);
        ivBtRecognizer.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.mic_animation));
        mic_call_back = new Drawable.Callback() {
            @Override
            public void invalidateDrawable(@NonNull Drawable drawable) {

            }

            @Override
            public void scheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable, long l) {

            }

            @Override
            public void unscheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable) {

            }
        };

//        mRetrofitService = new RetrofitService();
//        mSearchService = mRetrofitService.getSearchService();

        btRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainAnimation();
                voiceBtAnimationState1();
                btRecognizer.setClickable(false);
                initVoiceRecognizer();
            }
        });


        ivBtRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainAnimation();
                voiceBtAnimationState1();
                ivBtRecognizer.setClickable(false);
                initVoiceRecognizer();
            }
        });

    }

    private void initViews(){
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
        Item item = new Item();
        List<Item> items = new ArrayList<>();
        for(int i = 0;i < 4;i++){
            items.add(item);
        }
        initRv(items);
    }

    private void mainAnimation() {
        Log.d(TAG, "mainAnimation: ");
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.activity_main);
        ConstraintLayout cc1 = findViewById(R.id.main_layout_01);
        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateInterpolator(1.0f));
        transition.setDuration(500);

        TransitionManager.beginDelayedTransition(cc1, transition);
        constraintSet.applyTo(cc1);
        initViews();
    }

    private void initRv(List<Item> list) {
        mAdapter = new ResultsAdapter(list);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        rvResults.setLayoutManager(llm);
        rvResults.setHasFixedSize(true);
        rvResults.setAdapter(mAdapter);
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
        //ivBtRecognizer.setBackgroundResource(R.drawable.mic_animation);
        ivBtRecognizer.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.mic_animation));
        micAnimation = (AnimationDrawable) ivBtRecognizer.getDrawable();
        micAnimation.setCallback(new AnimationDrawableCallback(micAnimation,ivBtRecognizer) {
            @Override
            public void onAnimationAdvanced(int currentFrame, int totalFrames) {

            }

            @Override
            public void onAnimationCompleted() {
                Log.d(TAG, "onAnimationCompleted: ");
                micAnimation();
            }
        });
        micAnimation.setOneShot(true);
        micAnimation.start();

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
        ivBtRecognizer.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.mic_animation3));
        micAnimation3 = (AnimationDrawable) ivBtRecognizer.getDrawable();
        micAnimation3.setOneShot(true);
        micAnimation3.start();

    }
    private void micAnimation(){
        ivBtRecognizer.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.mic_animation2));
        micAnimation2 = (AnimationDrawable) ivBtRecognizer.getDrawable();
        micAnimation2.start();
    }

    @Override
    public void results(String txt) {
        etSearch.setText(txt);
//        search(txt);
    }

    @Override
    public void progressbarControler(int i) {
        voiceBtAnimationState2();
        btRecognizer.setClickable(true);
        ivBtRecognizer.setClickable(true);
    }
    private void search(String txt){
        mSearchService.search(txt);
    }
}

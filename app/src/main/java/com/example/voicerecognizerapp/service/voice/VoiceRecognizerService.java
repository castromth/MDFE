package com.example.voicerecognizerapp.service.voice;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.example.voicerecognizerapp.Listener.RecognitionResultsListener;

import java.util.ArrayList;


public class VoiceRecognizerService implements RecognitionListener{
    private static final String TAG = "VoiceRecognizerService";
    private SpeechRecognizer speechRecognizer;

    private RecognitionResultsListener mListener;
    public VoiceRecognizerService(Context c,RecognitionResultsListener recognizerListener){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(c);
        speechRecognizer.setRecognitionListener(this);
        mListener = recognizerListener;
        Log.d(TAG, "VoiceRecognizerService: ");
    }
    public void startListening(Intent intent){
        speechRecognizer.startListening(intent);
        Log.d(TAG, "startListening: ");
    }
    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d(TAG, "onReadyForSpeech: ");
    }
    @Override
    public void onBeginningOfSpeech() {
    }
    @Override
    public void onEndOfSpeech() {
        mListener.progressbarControler(0);
    }

    @Override
    public void onRmsChanged(float v) {

    }
    @Override
    public void onBufferReceived(byte[] bytes) {

    }
    @Override
    public void onError(int i) {

    }
    @Override
    public void onPartialResults(Bundle bundle) {

    }
    @Override
    public void onEvent(int i, Bundle bundle) {

    }
    @Override
    public void onResults(Bundle bundle) {
        Log.d(TAG, "onResults: ");
        ArrayList<String> matches = bundle
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = matches.get(0);
//        for (String result : matches)
//            text += result + "\n";
        mListener.results(text);
        mListener.progressbarControler(10);
    }


}

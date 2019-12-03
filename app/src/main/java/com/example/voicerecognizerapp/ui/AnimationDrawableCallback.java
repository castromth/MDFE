package com.example.voicerecognizerapp.ui;


import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;



public abstract class AnimationDrawableCallback implements Drawable.Callback {

    private static final String TAG = "AnimationDrawableCallba";

    private final int mTotalFrames;

    private final Drawable mLastFrame;

    private int mCurrentFrame;

    private Drawable.Callback mWrappedCallback;

    private boolean mIsCallbackTriggered = false;

    public AnimationDrawableCallback(AnimationDrawable animationDrawable, Drawable.Callback callback) {
        mTotalFrames = animationDrawable.getNumberOfFrames();
        mLastFrame = animationDrawable.getFrame(mTotalFrames - 1);
        mWrappedCallback = callback;
    }
    @Override
    public void invalidateDrawable(Drawable who) {
        if (mWrappedCallback != null) {
            mWrappedCallback.invalidateDrawable(who);
        }

        if (!mIsCallbackTriggered && mLastFrame != null && mLastFrame.equals(who.getCurrent())) {
            mIsCallbackTriggered = true;
            onAnimationCompleted();
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (mWrappedCallback != null) {
            mWrappedCallback.scheduleDrawable(who, what, when);
        }

        onAnimationAdvanced(mCurrentFrame, mTotalFrames);
        mCurrentFrame++;
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (mWrappedCallback != null) {
            mWrappedCallback.unscheduleDrawable(who, what);
        }
    }
    public abstract void onAnimationAdvanced(int currentFrame, int totalFrames);


    public abstract void onAnimationCompleted();


}

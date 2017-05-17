package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Edmund on 5/17/2017.
 */

public class CountdownTimer {
    private float originalTime;
    private float time;
    private boolean running;
    private CountdownTimerListener listener;

    public boolean isRunning(){return running;}

    public CountdownTimer(float originalTime, CountdownTimerListener listener){
        this.originalTime = originalTime;
        this.listener = listener;
    }

    public void reset(){
        this.time = originalTime;
    }

    public void start(){
        reset();
        this.running = true;
        listener.onCountdownTimerStart();
    }

    public void stop(){
        this.time = 0;
        this.running = false;
    }

    public void interrupt(){
        this.stop();
        listener.onCountdownTimerInterrupt();
    }

    public void step(float delta){
        this.time -= delta;
        listener.onCountdownTimerStep();
        if(this.time <= 0){
            this.stop();
            listener.onCountdownTimerSet();
        }
    }

    public String timeToString(){
        return Integer.toString((int)time);
    }

    interface CountdownTimerListener {
        void onCountdownTimerInterrupt();
        void onCountdownTimerSet();
        void onCountdownTimerStart();
        void onCountdownTimerStep();
    }

}

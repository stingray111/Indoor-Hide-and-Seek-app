package com.csci3310.indoorhns;

/**
 * Created by Edmund on 5/18/2017.
 */

public class Coordinate{
    private float x, y;
    public float getX(){return x;}
    public float getY(){return y;}

    public Coordinate(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void setCoordinate(float x, float y){
        this.x = x;
        this.y = y;
    }
}
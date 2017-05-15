package com.csci3310.indoorhns;

/**
 * Created by Edmund on 5/15/2017.
 */

public class Player {

    public enum Type {
        Hunter,
        Huntee
    }

    private Type type;
    private String name;
    private Coordinate coordinate;

    public Type getType(){return this.type;}
    public String getName(){return this.name;}
    public Coordinate getCoordinate(){return this.coordinate;}

    public Player(Type type, String name){
        this.type = type;
        this.name = name;
        this.coordinate = null;
    }

    static public class Coordinate{
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
}

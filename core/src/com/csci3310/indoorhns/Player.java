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
    private String androidID;

    public Type getType(){return this.type;}
    public String getName(){return this.name;}
    public String getAndroidID(){return this.androidID;}
    public Coordinate getCoordinate(){return this.coordinate;}

    public Player(Type type, String name, String androidID){
        this.type = type;
        this.name = name;
        this.androidID = androidID;
        this.coordinate = new Coordinate(-99, -99);
    }
}

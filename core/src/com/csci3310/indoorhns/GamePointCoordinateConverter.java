package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

/**
 * Created by edmund on 17/5/2017.
 */

public class GamePointCoordinateConverter {
    private HashMap<String, Coordinate> coordinateMap;

    private static String FILENAME = "coordinates.json";

    public GamePointCoordinateConverter(){
        coordinateMap = new HashMap<String, Coordinate>();
        FileHandle coordinateFile = Gdx.files.internal(FILENAME);
        JsonReader json = new JsonReader();
        JsonValue parsedJson = json.parse(coordinateFile);
        JsonValue.JsonIterator iterator = parsedJson.iterator();
        while(iterator.hasNext()) {
            JsonValue obj = iterator.next();
            coordinateMap.put(obj.getString("name"), new Coordinate(obj.getFloat("x"), obj.getFloat("y")));
        }
    }

    public Coordinate getCoordinate(String name){
        if(coordinateMap.containsKey(name)) {
            return coordinateMap.get(name);
        }else{
            return null;
        }
    }

    public int getFloor(String name){
        if(coordinateMap.containsKey(name)){
            if(name.substring(0, 2).equals("09")){
                return 9;
            }else{
                return 10;
            }
        }else{
            return -1;
        }
    }


//    public static float POINT_09R1_X = 113;
//    public static float POINT_09R1_Y = 162;
//
//    public static float POINT_09R2_X = 281;
//    public static float POINT_09R2_Y = 273;
//
//    public static float POINT_09R3_X = 429;
//    public static float POINT_09R3_Y = 229;
//
//    public static float POINT_09R4_X = 429;
//    public static float POINT_09R4_Y = 335;
//
//    public static float POINT_09C1_X = 178;
//    public static float POINT_09C1_Y = 94;
//
//    public static float POINT_09C2_X = 189;
//    public static float POINT_09C2_Y = 339;
//
//    public static float POINT_09C3_X = 386;
//    public static float POINT_09C3_Y = 341;
//
//    public static float POINT_09C4_X = 386;
//    public static float POINT_09C4_Y = 137;
//
//    public static float POINT_09C5_X = 179;
//    public static float POINT_09C5_Y = 273;
//
//    public static float POINT_09C6_X = 386;
//    public static float POINT_09C6_Y = 273;

}

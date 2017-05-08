package com.csci3310.indoorhns;

/**
 * Created by Edmund on 5/8/2017.
 */

public class AndroidConnector{

    private AndroidConnectorRequestListener rootActivityListener;

    public AndroidConnector(AndroidConnectorRequestListener rootActivityListener){
        this.rootActivityListener = rootActivityListener;
    }

    public AndroidConnectorRequestListener getRequestListener(){
        return rootActivityListener;
    }
}

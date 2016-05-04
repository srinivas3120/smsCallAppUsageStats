package com.srinivas.ele.model;

import java.util.Observable;

/**
 * Created by Ajay on 7/20/2015.
 */
public class BasicModel extends Observable
{
    public BasicModel(){
    }

    public void notifyObservers(Object data){
        setChanged();
        super.notifyObservers(data);
    }
}
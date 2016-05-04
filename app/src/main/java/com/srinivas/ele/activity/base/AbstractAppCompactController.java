package com.srinivas.ele.activity.base;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.srinivas.ele.model.BasicModel;

import java.util.Observer;

/**
 * Created by Ajay on 7/20/2015.
 */
public abstract class AbstractAppCompactController extends AppCompatActivity implements Observer  {
    private BasicModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            super.onCreate(savedInstanceState);
            model = getModel();
            model.addObserver(this);
            onCreatePost(savedInstanceState);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected abstract void onCreatePost(Bundle savedInstanceState);
    protected abstract BasicModel getModel();


  }

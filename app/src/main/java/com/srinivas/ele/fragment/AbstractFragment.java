package com.srinivas.ele.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinivas.ele.model.BasicModel;

import java.util.Observer;

/**
 * Created by Harsha on 7/31/2015.
 */
public abstract class AbstractFragment extends Fragment implements Observer {
    private BasicModel model;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try{
            model = getModel();
            model.addObserver(this);
            view = onCreateViewPost(inflater, container, savedInstanceState);
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    protected abstract View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract BasicModel getModel();

}

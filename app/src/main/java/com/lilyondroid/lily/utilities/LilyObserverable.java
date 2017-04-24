package com.lilyondroid.lily.utilities;

import java.util.Observable;

/**
 * Created by jason on 23/04/2017.
 */

public class LilyObserverable extends Observable {


    private  static LilyObserverable instance = new LilyObserverable();

    //Singleton
    public static LilyObserverable getInstance(){

        return instance;
    }

    public LilyObserverable(){

    }

    //Pass data to observers - the main activity
    public void updateValue(Object data){

        synchronized (this){
            setChanged();
            notifyObservers(data);
        }
    }
}

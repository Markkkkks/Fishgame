package com.example.zeus.fishgame.factory;

import android.content.res.Resources;

import com.example.zeus.fishgame.object.GameObject;
import com.example.zeus.fishgame.object.MyFish;
import com.example.zeus.fishgame.object.testFish;

/**
 * Created by zeus on 2016/11/15.
 */
public class GameObjectFactory {
    public GameObject createMyPlane(Resources resources){
        return new MyFish(resources);
    }
    public GameObject createtestFish(Resources resources){
        return new testFish(resources);
    }
}

package com.haroldwren.machine.pingpong.game;

import com.haroldwren.machine.PAppletProxy;

/**
 * Created by jack on 2017.04.04..
 */
public class Wall extends Bat {
    public Wall(boolean left) {
        super(left);
        PAppletProxy pAppletProxy = PAppletProxy.getInstance(null);
        this.setBatHeight(pAppletProxy.getHeight().floatValue());
    }
}

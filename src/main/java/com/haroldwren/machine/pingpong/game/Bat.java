/**
 * Created by jack on 2017.04.04..
 */
package com.haroldwren.machine.pingpong.game;

import com.haroldwren.machine.PAppletProxy;

import static processing.core.PApplet.constrain;

public class Bat {
    private Float xPoint, yPoint;
    private Float batWidth = 20f, batHeight = 30f;
    private Float yChange = 0f;
    private Integer drawHeight;

    /**
     * Create bat
     *
     * @param left
     */
    public Bat(boolean left) {
        PAppletProxy pAppletProxy = PAppletProxy.getInstance(null);
        setValues(left, pAppletProxy.getWidth(), pAppletProxy.getHeight());
    }

    /**
     * Set the bat values
     *
     * @param left
     * @param drawWidth
     * @param drawHeight
     */
    public void setValues(boolean left, Integer drawWidth, Integer drawHeight) {
        this.drawHeight = drawHeight;
        yPoint = (drawHeight / 2f);
        if (left) {
            xPoint = batWidth;
        } else {
            xPoint = drawWidth - batWidth;
        }
    }

    /**
     * Update the bat position
     */
    public void update() {
        yPoint += yChange;
        yPoint = constrain(yPoint, batHeight /2, drawHeight - batHeight /2);
    }

    /**
     * Move the bat
     *
     * @param steps
     */
    public void move(Float steps) {
        yChange = steps;
    }

    public Float getxPoint() {
        return xPoint;
    }
    public Float getyPoint() {
        return yPoint;
    }
    public Float getBatWidth() {
        return batWidth;
    }
    public Float getBatHeight() {
        return batHeight;
    }
    public void setBatHeight(Float batHeight) {
        this.batHeight = batHeight;
    }
}

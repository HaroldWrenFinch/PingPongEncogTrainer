package com.haroldwren.machine.pingpong.game;

import com.haroldwren.machine.PAppletProxy;

import static jdk.nashorn.internal.objects.NativeMath.random;
import static processing.core.PApplet.*;
import static processing.core.PConstants.PI;

/**
 * Created by jack on 2017.04.04..
 */
public class Ball {
    private Scores scores;
    private Float xPoint;
    private Float yPoint;
    private Float xSpeed;
    private Float ySpeed;
    private Float radius = 12f;
    private Integer drawWidth;
    private Integer drawHeight;
    private Float xPreviousPoint;
    private Float yPreviousPoint;
    private Long succeededFrame = 0L;
    private boolean canIncreaseFrameNO = true;

    /**
     * Crate a ball.
     *
     * @param scores
     */
    public Ball(Scores scores) {
        PAppletProxy pAppletProxy = PAppletProxy.getInstance(null);
        setValues(scores, pAppletProxy.getWidth(), pAppletProxy.getHeight());
    }

    /**
     * Set the ball values
     *
     * @param scores
     * @param drawWidth
     * @param drawHeight
     */
    public void setValues(Scores scores, Integer drawWidth, Integer drawHeight) {
        this.scores = scores;
        this.drawWidth = drawWidth;
        this.drawHeight = drawHeight;
        xPoint = drawWidth / 2f;
        yPoint = drawHeight / 2f;
        xPreviousPoint = xPoint;
        yPreviousPoint = yPoint;
        reset();
    }

    /**
     * check if the left bat hit the ball
     *
     * @param bat
     */
    public void checkBatLeft(Bat bat) {
        if (yPoint - radius < bat.getyPoint() + bat.getBatHeight()/2
                && yPoint + radius > bat.getyPoint() - bat.getBatHeight()/2
                && xPoint - radius < bat.getxPoint() + bat.getBatWidth()/2) {
            if (xPoint > bat.getxPoint()) {
                Float diff = yPoint - (bat.getyPoint() - bat.getBatHeight()/2);
                Float rad = radians(45);
                Float angle = map(diff, 0, bat.getBatHeight(), -rad, rad);
                xSpeed = 5 * cos(angle);
                ySpeed = 5 * sin(angle);
                xPoint = bat.getxPoint() + bat.getBatWidth()/2 + radius;
                //xSpeed *= -1;
            }
        }
    }

    /**
     * check if the right bat hit the ball
     *
     * @param bat
     */
    public void checkBatRight(Bat bat) {
        if (yPoint - radius < bat.getyPoint() + bat.getBatHeight()/2 &&
                yPoint + radius > bat.getyPoint() - bat.getBatHeight()/2 &&
                xPoint + radius > bat.getxPoint() - bat.getBatWidth()/2) {
            if (xPoint < bat.getxPoint()) {
                Float diff = yPoint - (bat.getyPoint() - bat.getBatHeight()/2);
                Float angle = map(diff, 0, bat.getBatHeight(), radians(225), radians(135));
                xSpeed = 5 * cos(angle);
                ySpeed = 5 * sin(angle);
                xPoint = bat.getxPoint() - bat.getBatWidth()/2 - radius;
            }
        }
    }

    /**
     * Update the ball position
     */
    public void update() {
        xPoint = xPoint + xSpeed;
        yPoint = yPoint + ySpeed;
    }

    /**
     * reset the ball
     */
    private void reset() {
        xPoint = drawWidth / 2f;
        yPoint = drawHeight / 2f;
        Float lower = -PI/4;
        Float upper = PI/4;
        Float angle = (float) (Math.random() * (upper - lower)) + lower;
        xSpeed = 5 * cos(angle);
        ySpeed = 5 * sin(angle);

        if (random(1) < 0.5) {
            xSpeed *= -1;
        }
    }

    /**
     * Check edges top, bottom, and bat
     */
    public void edges() {
        if (yPoint < radius || yPoint + radius > drawHeight) {
            ySpeed *= -1;
        }

        if (xPoint - radius > drawWidth) {
            this.scores.increaseLeftScore();
            canIncreaseFrameNO = false;
            reset();
        } else {
            if(canIncreaseFrameNO) {
                succeededFrame++;
            }
        }

        if (xPoint + radius < 0) {
            this.scores.increaseRighScore();
            reset();
        }
    }

    /**
     * show the ball
     */
    public void show() {
        xPreviousPoint = xPoint;
        yPreviousPoint = yPoint;
    }

    public Float getxPoint() {
        return xPoint;
    }
    public Float getyPoint() {
        return yPoint;
    }
    public Float getxPreviousPoint() {
        return xPreviousPoint;
    }
    public Float getyPreviousPoint() {
        return yPreviousPoint;
    }
    public Long getSucceededFrame() {
        return succeededFrame;
    }
}

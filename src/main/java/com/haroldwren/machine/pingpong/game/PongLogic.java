package com.haroldwren.machine.pingpong.game;

import com.haroldwren.machine.PAppletProxy;
import com.haroldwren.machine.pingpong.trainer.NeuralMoveLogic;
import org.encog.ml.MLRegression;
import processing.core.PApplet;

import java.util.function.Consumer;

/**
 * Created by jack on 2017.04.08..
 */
public class PongLogic {
    public static final double MAX_ITERATION = 200000.00;
    private Bat right;
    private Wall wall;
    private Ball ball;
    private PApplet pApplet;
    private Consumer<MLRegression> funcRight;

    public PongLogic(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    public void setup() {
        PAppletProxy pAppletProxy = PAppletProxy.getInstance(pApplet); // initialize the draw instance.
        pAppletProxy.setGivenHeight(480);
        pAppletProxy.setGivenWidth(640);
        Scores score = new Scores();
        ball = new Ball(score);
        right = new Bat(false);
        wall = new Wall(true);

        NeuralMoveLogic moveLogic = new NeuralMoveLogic();
        funcRight = (network) -> (moveLogic).doNeuralLogic(right, ball, network);
    }

    public void run(MLRegression network) {
        funcRight.accept(network);

        ball.checkBatRight(right);
        ball.checkBatLeft(wall);

        wall.update();
        right.update();

        ball.update();
        ball.edges();

        ball.show();
    }

    public Ball getBall() {
        return ball;
    }
}

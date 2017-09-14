package com.haroldwren.machine.pingpong.trainer;

import com.haroldwren.machine.pingpong.game.PongLogic;
import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;

import static java.lang.Math.pow;

/**
 * Created by jack on 2017.04.10..
 */
public class PongNeuralScore implements CalculateScore {
    /**
     * The training set.
     */
    private final MLDataSet training;

    /**
     * Construct a training set score calculation.
     *
     * @param training
     *            The training data to use.
     */
    public PongNeuralScore(final MLDataSet training) {
        this.training = training;
    }

    /**
     * Calculate the score for the network.
     * @param method The network to calculate for.
     * @return The score.
     */
    public double calculateScore(final MLMethod method) {
        PongLogic pongLogic = new PongLogic(null);
        pongLogic.setup();

        for(long i=0;i<PongLogic.MAX_ITERATION;i++) {
            pongLogic.run((BasicNetwork) method);
        }
        long succeededFrame = pongLogic.getBall().getSucceededFrame();
        if(succeededFrame == PongLogic.MAX_ITERATION) {
            succeededFrame = 0;
        }

        return pow(succeededFrame, 1);
    }

    /**
     * A training set based score should always seek to lower the error,
     * as a result, this method always returns true.
     * @return Returns true.
     */
    public boolean shouldMinimize() {
        return false;
    }

    @Override
    public boolean requireSingleThreaded() {
        return false;
    }

}



package com.haroldwren.machine.pingpong.trainer;

import com.haroldwren.machine.pingpong.game.PongLogic;
import org.encog.Encog;
import org.encog.ml.CalculateScore;
import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;
import org.encog.neural.neat.PersistNEATPopulation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jack on 2017.04.10..
 */
public class NeuralNetworkTrainer {
    private static final double SUCCESS_RATE = 0.9;
    private static final double MIN_SCORE = PongLogic.MAX_ITERATION * SUCCESS_RATE;
    private static final String NETWORK_FILE_NAME = "pong";
    private static final String DEFAULT_FILE_EXTENSION = "eg";
    private static final int POPULATION_SIZE = 200;
    private static final int OUTPUT_NEURON_SIZE = 1;
    private static final Boolean WRITE_PERCENTS_TO_FILE = true;

    public static void main(String[] args) throws IOException {
        NEATPopulation pop = new NEATPopulation(5, OUTPUT_NEURON_SIZE, POPULATION_SIZE);
        pop.setInitialConnectionDensity(1.0); // not required, but speeds training
        pop.reset();

        CalculateScore score = new PongNeuralScore(null);
        final EvolutionaryAlgorithm train = NEATUtil.constructNEATTrainer(pop,score);

        Boolean[] savedTrain = new Boolean[] {false, false, false, false, false, false, false, false, false, false};
        int savedTrainNO = 0;
        do {
            train.iteration();
            System.out.println("Epoch #" + train.getIteration() + " Score:" + train.getError()+ ", Species:" + pop.getSpecies().size());
            if(WRITE_PERCENTS_TO_FILE) {
                if(!savedTrain[savedTrainNO] && (train.getError() > (MIN_SCORE * (savedTrainNO+1) * 0.1))) {
                    savedTrain[savedTrainNO] = true;
                    writeToDisk(pop, false, "pong_"+(savedTrainNO+1)+"0_percent");
                    if(savedTrainNO < savedTrain.length -1) {
                        savedTrainNO++;
                    }
                }
            }
        } while(train.getError() < MIN_SCORE);

        NEATNetwork network = (NEATNetwork)train.getCODEC().decode(train.getBestGenome());
        System.out.println("Input: " + network.getInputCount());
        System.out.println("Output: " + network.getOutputCount());

        writeToDisk(pop, true, "perfect");
        //EncogUtility.evaluate(network, evaluateSet);
    }

    public static void writeToDisk(NEATPopulation pop, boolean shutdown, String fileName) throws IOException {
        File networkFile = File.createTempFile(NETWORK_FILE_NAME, DEFAULT_FILE_EXTENSION);

        PersistNEATPopulation pnp = new PersistNEATPopulation();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(networkFile);
            pnp.save(outputStream, pop); //pop is your NEATPopulation object
            outputStream.flush();
            outputStream.close();
            if(fileName != null && !fileName.isEmpty()) {
                Path source = Paths.get(networkFile.toString());
                Files.move(source, source.resolveSibling(fileName+"."+DEFAULT_FILE_EXTENSION));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(shutdown) {
            Encog.getInstance().shutdown();
        }
    }
}

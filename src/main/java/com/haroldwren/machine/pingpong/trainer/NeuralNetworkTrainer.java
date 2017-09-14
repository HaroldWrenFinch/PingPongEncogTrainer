package com.haroldwren.machine.pingpong.trainer;

import com.haroldwren.machine.pingpong.game.PongLogic;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.CalculateScore;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.io.IOException;
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

    public static BasicNetwork createNetwork() {
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 5)) ;
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 60)) ;
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, OUTPUT_NEURON_SIZE)) ;
        network.getStructure().finalizeStructure();
        network.reset();
        return network;
    }

    public static void main(String[] args) throws IOException {
        BasicNetwork network = createNetwork();

        CalculateScore score = new PongNeuralScore(null);

        MLTrain train;
        train = new NeuralSimulatedAnnealing(
                network, score, 10, 2, POPULATION_SIZE);

//        train = new MLMethodGeneticAlgorithm(new MethodFactory(){
//            @Override
//            public MLMethod factor() {
//                final BasicNetwork result = createNetwork();
//                ((MLResettable)result).reset();
//                return result;
//            }},score,POPULATION_SIZE);

//        NEATPopulation pop = new NEATPopulation(5, OUTPUT_NEURON_SIZE, POPULATION_SIZE);
//        pop.setInitialConnectionDensity(1.0); // not required, but speeds training
//        pop.reset();
//        final EvolutionaryAlgorithm train = NEATUtil.constructNEATTrainer(pop,score);

        Boolean[] savedTrain = new Boolean[] {false, false, false, false, false, false, false, false, false, false};
        int savedTrainNO = 0;
        do {
            train.iteration();
            System.out.println("Epoch #" + train.getIteration() + " Score:" + train.getError()); //", Species:" + pop.getSpecies().size());
            if(WRITE_PERCENTS_TO_FILE) {
                if(!savedTrain[savedTrainNO] && (train.getError() > (MIN_SCORE * (savedTrainNO+1) * 0.1))) {
                    savedTrain[savedTrainNO] = true;
//                    writeToDisk(pop, false, "pong_"+(savedTrainNO+1)+"0_percent");
                    writeToDisk(network, false, "pong_"+(savedTrainNO+1)+"0_percent");
                    if(savedTrainNO < savedTrain.length -1) {
                        savedTrainNO++;
                    }
                }
            }
        } while(train.getError() < MIN_SCORE);

//        NEATNetwork network = (NEATNetwork)train.getCODEC().decode(train.getBestGenome());
        System.out.println("Input: " + network.getInputCount());
        System.out.println("Output: " + network.getOutputCount());

//        writeToDisk(pop, true, "perfect");
        writeToDisk(network, true, "perfect");
        //EncogUtility.evaluate(network, evaluateSet);
    }

    public static void writeToDisk(BasicNetwork network, boolean shutdown, String fileName) throws IOException {
        File networkFile = File.createTempFile(NETWORK_FILE_NAME+fileName, DEFAULT_FILE_EXTENSION);

        try {
            EncogDirectoryPersistence.saveObject(networkFile, network) ;
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

//    public static void writeToDisk(NEATPopulation pop, boolean shutdown, String fileName) throws IOException {
//        File networkFile = File.createTempFile(NETWORK_FILE_NAME, DEFAULT_FILE_EXTENSION);
//
//        PersistNEATPopulation pnp = new PersistNEATPopulation();
//        OutputStream outputStream = null;
//        try {
//            outputStream = new FileOutputStream(networkFile);
//            pnp.save(outputStream, pop); //pop is your NEATPopulation object
//            outputStream.flush();
//            outputStream.close();
//            if(fileName != null && !fileName.isEmpty()) {
//                Path source = Paths.get(networkFile.toString());
//                Files.move(source, source.resolveSibling(fileName+"."+DEFAULT_FILE_EXTENSION));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if(shutdown) {
//            Encog.getInstance().shutdown();
//        }
//    }
}

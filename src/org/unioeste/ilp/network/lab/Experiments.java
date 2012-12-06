/*
 * "Copyright 2012 Lucas André de Alencar"
 * 
 * This file is part of ILPNetworkTraining.
 * 
 * ILPNetworkTraining is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * ILPNetworkTraining is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with ILPNetworkTraining.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.unioeste.ilp.network.lab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.unioeste.ilp.network.AbstractNeuralNetwork;
import org.unioeste.ilp.network.NeuralNetwork;
import org.unioeste.ilp.network.train.Trainer;
import org.unioeste.ilp.network.util.OutputHandler;

/**
 * Encapsulates experiments with basic neural networks.
 * 
 * @author Lucas André de Alencar
 *
 */
public class Experiments extends AbstractExperiments {
	
	public Experiments(NeuralNetwork networkModel) {
		super(networkModel);
	}
	
	public Experiments() {
		super();
	}
	
	/**
	 * Experiment that varies the number of units on the hidden layer.
	 * The networks tested only have 1 hidden layer.
	 * 
	 * @param minUnit Min Units
	 * @param maxUnit Max Units
	 * @param trainingSet Training set
	 * @param testSet Test set
	 * @param dir Directory where to save the network
	 * @throws IOException
	 */
	public void variantLayerUnits(int minUnit, int maxUnit, MLDataSet trainingSet, MLDataSet testSet, String dir) throws IOException {
		int [] hiddenLayer = new int[1];
		for (int i = minUnit; i <= maxUnit; i++) {
			hiddenLayer[0] = i;
			NeuralNetwork nn = networkFactory.constructNetwork(hiddenLayer, trainingSet);
			String networkInfo = OutputHandler.networkToString(nn.getNumInputs(), nn.getHiddenLayers(), nn.getNumOutputs());
			experimentIndividualNetwork(nn, trainingSet, testSet, dir, networkInfo);
		}
	}
	
	/**
	 * Experiment that varies the number of hidden layers on the network.
	 * The number of units on the hidden layers is static and equal at all the layers.
	 * 
	 * @param minLayer Min layers
	 * @param maxLayer Max layers
	 * @param unitsOnLayer Numer of units on hidden layers
	 * @param trainingSet Training set
	 * @param testSet Test set
	 * @param dir Directory where to save the network
	 * @throws IOException
	 */
	public void variantLayer(int minLayer, int maxLayer, int unitsOnLayer,
			MLDataSet trainingSet, MLDataSet testSet, String dir) throws IOException {
		
		for (int i = minLayer; i <= maxLayer; i++) {
			int [] hiddenLayers = generateLayers(i, unitsOnLayer);
			NeuralNetwork nn = networkFactory.constructNetwork(hiddenLayers, trainingSet);
			String networkInfo = OutputHandler.networkToString(nn.getNumInputs(), nn.getHiddenLayers(), nn.getNumOutputs());
			experimentIndividualNetwork(nn, trainingSet, testSet, dir, networkInfo);
		}
	}
	
	/**
	 * Experiment that varies the number of layers and units on the network.
	 * The number of layers varies with the variation of the units.
	 * The hidden layers has a static and equal number of units, varing
	 * in the end of each variation on the number of layers.
	 * 
	 * @param minLayer Min layers
	 * @param maxLayer Max layers
	 * @param minUnits Min units
	 * @param maxUnits Max units
	 * @param trainingSet
	 * @param testSet
	 * @param dir
	 * @throws IOException
	 */
	public void variantLayerAndUnits(int minLayer, int maxLayer, 
			int minUnits, int maxUnits, 
			MLDataSet trainingSet, MLDataSet testSet, String dir) throws IOException {
		
		for (int i = minUnits; i <= maxUnits; i++)
			variantLayer(minLayer, maxLayer, i, trainingSet, testSet, dir);
	}
	
	private int[] generateLayers(int numLayers, int unitsOnLayers) {
		int [] layers = new int[numLayers];
		for (int i = 0; i < layers.length; i++) {
			layers[i] = unitsOnLayers;
		}
		return layers;
	}
	
	@Override
	protected void trainNetwork(AbstractNeuralNetwork nn, String fileName) throws IOException {
		List<String> output = new ArrayList<String>();
		output = Trainer.train(nn);
		OutputHandler.writeOutput(fileName, output);
	}
	
	@Override
	protected void testNetwork(AbstractNeuralNetwork nn, MLDataSet testSet, String fileName) throws IOException {
		List<String> output = new ArrayList<String>();
		int fa = 0, fr = 0;
		for (MLDataPair pair : testSet) {
			MLData result = nn.compute(pair);
			output.add(pair.getIdeal().getData(0) + "\t" + result.getData(0));
			if (isFA(pair.getIdeal(), result)) fa++;
			if (isFR(pair.getIdeal(), result)) fr++;
		}
		// Writes the results to a file
		OutputHandler.writeOutput(fileName, output);
		reporter.report(fa, (int) testSet.getRecordCount() / 2, fr, (int) testSet.getRecordCount() / 2);
	}
	
	private boolean isFA(MLData ideal, MLData result) {
		return ideal.getData(0) == 0 && Math.round(result.getData(0)) == 1;
	}
	
	private boolean isFR(MLData ideal, MLData result) {
		return ideal.getData(0) == 1 && Math.round(result.getData(0)) == 0;
	}
}

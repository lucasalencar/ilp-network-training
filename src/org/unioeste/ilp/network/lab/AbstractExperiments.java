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

import java.io.File;
import java.io.IOException;

import org.encog.ml.data.MLDataSet;
import org.unioeste.ilp.network.AbstractNeuralNetwork;
import org.unioeste.ilp.network.util.AbstractNeuralNetworkFactory;
import org.unioeste.ilp.network.util.OutputHandler;

/**
 * Abstract class that models the experiments that can be done.
 * 
 * Manages the reports made with the results from the tests made.
 * 
 * Has the default steps on how a training should be with an neural network.
 * 
 * @author Lucas André de Alencar
 *
 */
public abstract class AbstractExperiments {

	protected AbstractNeuralNetworkFactory networkFactory;
	protected Reporter reporter;
	
	public AbstractExperiments() {
		this.reporter = new Reporter();
	}
	
	public AbstractExperiments(AbstractNeuralNetwork networkModel) {
		this.reporter = new Reporter();
		this.networkFactory = new AbstractNeuralNetworkFactory(networkModel);
	}
	
	/**
	 * Writes the results on the path specified with the results added to the report.
	 * 
	 * @param path Path
	 * @throws IOException
	 */
	public void writeReport(String path) throws IOException {
		reporter.commitReport();
		reporter.writeReport(path);
	}
	
	/**
	 * Default steps on an experiment with a neural network. It trains the network with the training set, saves it
	 * on a file and then tests it with the test set. All the results on the tests are added on the
	 * report.
	 * 
	 * @param network Neural Network
	 * @param trainingSet Training Set
	 * @param testSet Test Set
	 * @param dir Directory where to save the network
	 * @param experimentInfo Name given for the experiment
	 * @throws IOException
	 */
	public void experimentIndividualNetwork(AbstractNeuralNetwork network, 
			MLDataSet trainingSet, MLDataSet testSet, String dir, String experimentInfo) throws IOException {
		
		// Train neural network
		String fileName = dir + "/training " + experimentInfo;
		trainNetwork(network, fileName);
		
		// Saves neural network
		String [] dirs = OutputHandler.separateDirs(dir);
		dirs[1] = "networks";
		String netPath = OutputHandler.mountDir(dirs);
		File file = new File(netPath + "/" + experimentInfo);
		network.save(file);
		
		// Tests neural network
		fileName = dir + "/test " + experimentInfo;
		testNetwork(network, testSet, fileName);
		
		reporter.report(experimentInfo);
	}
	
	protected abstract void trainNetwork(AbstractNeuralNetwork nn, String fileName)  throws IOException;
	
	protected abstract void testNetwork(AbstractNeuralNetwork nn, MLDataSet testSet, String fileName) throws IOException;
}

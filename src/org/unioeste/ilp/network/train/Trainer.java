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

package org.unioeste.ilp.network.train;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.encog.ml.train.MLTrain;
import org.unioeste.ilp.network.AbstractNeuralNetwork;

/**
 * Responsible for training the neural networks.
 * 
 * Encapsulates the default way the networks are
 * trained.
 * 
 * @author Lucas André de Alencar
 *
 */
public class Trainer {

	/**
	 * Performs the training until reach the max error determined.
	 * 
	 * @param training MLTrain
	 * @param max_error Max error
	 * @return Training final error
	 */
	public static double train(MLTrain training, double max_error) {
		int epoch = 1;
		do {
			training.iteration();
			epoch++;
		} while (training.getError() > max_error);
		System.out.println("Iteration #" + epoch + " Error = " + training.getError());
		
		training.finishTraining();
		return training.getError();
	}
	
	/**
	 * Performs the network's training iterations.
	 * The training continues until it reach the max error
	 * or the max number of iterations set on the network.
	 * 
	 * @param network AbstractNeuralNetwork
	 * @return List of iterations
	 */
	public static List<String> train(AbstractNeuralNetwork network) {
		MLTrain training = network.getTrainStrategy();
		List<String> output = new ArrayList<String>();
		double error = 0;
		int epoch = 1;
		do {
			training.iteration();
			error = training.getError();
			output.add(epoch + "\t" + String.format(Locale.US, "%.20f", error));
//			System.out.println("Iteration #" + epoch + " Error = " + training.getError());
			epoch++;
		} while (continueIterations(network, training.getError(), epoch));
		network.updateTrainError();
		System.out.println("Ended training: Iteration #" + --epoch + " Error = " + training.getError());
		
		training.finishTraining();
		return output;
	}
	
	/**
	 * Determines if the training may continue.
	 * Checks the network permited max training error and max number of iteraitions. 
	 * 
	 * @param network AbstractNeuralNetwork
	 * @param error Current training error
	 * @param iteration Current iteration
	 * @return May continue?
	 */
	private static boolean continueIterations(AbstractNeuralNetwork network, double error, int iteration) {
		if (network.getMaxIterations() > 0 && iteration > 0)
			return network.getMaxIterations() >= iteration && network.getMaxError() < error;
		else
			return network.getMaxError() < error;
	}
}

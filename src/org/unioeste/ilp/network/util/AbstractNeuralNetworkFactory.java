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

package org.unioeste.ilp.network.util;

import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.unioeste.ilp.network.AbstractNeuralNetwork;
import org.unioeste.ilp.network.NeuralNetwork;
import org.unioeste.ilp.network.ReplicatorNeuralNetwork;

/**
 * Responsible for building generic neural networks
 * substituting some attributes to use on different
 * iterations on some training.
 * 
 * @author Lucas André de Alencar
 *
 */
public class AbstractNeuralNetworkFactory {

	private static final String NOT_INSTANCE_OF_ERROR = "Can't factory Network with %1$ as model.";
	
	// Network model
	private AbstractNeuralNetwork networkModel;
	
	/**
	 * Constructor that receives the network model used to factory
	 * the factories.
	 * 
	 * @param networkModel AbstractNeuralNetwork
	 */
	public AbstractNeuralNetworkFactory(AbstractNeuralNetwork networkModel) {
		this.networkModel = networkModel;
	}
	
	private void validateNeuralNetwork() {
		if (!(networkModel instanceof NeuralNetwork))
			throw new IllegalStateException(String.format(NOT_INSTANCE_OF_ERROR, "NeuralNetwork"));
	}
	
	private void validateReplicatorNeuralNetwork() {
		if (!(networkModel instanceof ReplicatorNeuralNetwork))
			throw new IllegalStateException(String.format(NOT_INSTANCE_OF_ERROR, "ReplicatorNeuralNetwork"));
	}
	
	/**
	 * Constructs a basic neural network with the hidden layer and dataset specified.
	 * 
	 * @param hiddenLayer Array of layers
	 * @param dataset MLDataSet
	 * @return NeuralNetwork
	 */
	public NeuralNetwork constructNetwork(int [] hiddenLayer, MLDataSet dataset) {
		validateNeuralNetwork();
		NeuralNetwork network = new NeuralNetwork(networkModel.getNumInputs(), hiddenLayer, networkModel.getNumOutputs());
//		dataset = DataSetNormalizer.normalize(dataset);
		network.setTrainStrategy(new ResilientPropagation(network.getNetwork(), dataset));
		network.setMaxError(0.1);
		return network;
	}
	
	/**
	 * Factory a replicator network based on the model specified but
	 * with the hidden layers passed as parameter.
	 * 
	 * To understand how the hidden layers' array works, see the AbstractNeuralNetwork
	 * documentation for more information.
	 * 
	 * @param hiddenLayers Array of layers
	 * @return ReplicatorNeuralNetwork
	 */
	public ReplicatorNeuralNetwork factoryReplicatorNetwork(int [] hiddenLayers) {
		validateReplicatorNeuralNetwork();
		ReplicatorNeuralNetwork network = new ReplicatorNeuralNetwork(networkModel.getNumInputs(), hiddenLayers);
		network.setMaxError(networkModel.getMaxError());
		network.setMaxIterations(networkModel.getMaxIterations());
		return network;
	}
	
	/**
	 * Factory a replicator neural network based on the model specified but
	 * with the max error passed as parameter.
	 * 
	 * @param maxError Max error
	 * @return ReplicatorNeuralNetwork
	 */
	public ReplicatorNeuralNetwork factoryReplicatorNetwork(double maxError) {
		validateReplicatorNeuralNetwork();
		ReplicatorNeuralNetwork network = new ReplicatorNeuralNetwork(networkModel.getNumInputs(), networkModel.getHiddenLayers());
		network.setMaxIterations(networkModel.getMaxIterations());
		network.setMaxError(maxError);
		return network;
	}
	
	/**
	 * Factory a replicator neural network based on the model specified.
	 * 
	 * @return ReplicatorNeuralNetwork
	 */
	public ReplicatorNeuralNetwork factoryReplicatorNetwork() {
		validateReplicatorNeuralNetwork();
		ReplicatorNeuralNetwork network = new ReplicatorNeuralNetwork(networkModel.getNumInputs(), networkModel.getHiddenLayers());
		network.setMaxIterations(networkModel.getMaxIterations());
		network.setMaxError(networkModel.getMaxError());
		return network;
	}
}

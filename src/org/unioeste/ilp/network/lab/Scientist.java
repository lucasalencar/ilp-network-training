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
import java.sql.SQLException;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.unioeste.ilp.network.NeuralNetwork;
import org.unioeste.ilp.network.datasets.SeparatedDataSet;
import org.unioeste.ilp.network.util.DataSetProvider;
import org.unioeste.ilp.network.util.OutputHandler;

/**
 * Responsible for preparing and executing the experiments 
 * with the networks.
 * 
 * @author Lucas André de Alencar
 *
 */
public class Scientist {
	
	public static final String DEFAULT_EXPERIMENT_DIR = "experiments";
	public static final String TRAINING_INFO_FILE = "rates";
	
	public static String initDirs(String training, int userId, int patternId) {
		String [] dirs = new String[4];
		dirs[0] = DEFAULT_EXPERIMENT_DIR;
		dirs[1] = training;
		dirs[2] = "user " + userId;
		dirs[3] = "pattern " + patternId;
		return OutputHandler.mountDir(dirs);
	}
	
	protected static void finishTraining(AbstractExperiments e, String path) throws IOException {
		e.writeReport(path + "/" + TRAINING_INFO_FILE);
		Encog.getInstance().shutdown();
	}
	
	private static void executeTraining(int minLayer, int maxLayer, 
			int minUnits, int maxUnits, 
			SeparatedDataSet dataset, String path) throws IOException {
		
		Experiments e = new Experiments(new NeuralNetwork(minUnits, null, 1));
		e.variantLayerAndUnits(minLayer, maxLayer, minUnits, maxUnits, dataset.getTrainingData(), dataset.getTestData(), path);
		finishTraining(e, path);
	}
	
	/**
	 * Prepares for training the network specified.
	 * 
	 * @param network Network
	 * @param trainingSet Training set
	 * @param testSet Test set
	 * @param trainingName Training name
	 * @param userId User
	 * @param patternId Pattern
	 * @throws IOException
	 */
	public static void trainIndividualNetwork(NeuralNetwork network, MLDataSet trainingSet, MLDataSet testSet, String trainingName, int userId, int patternId) throws IOException {
		String path = initDirs(trainingName, userId, patternId);
		Experiments e = new Experiments();
		String networkInfo = OutputHandler.networkToString(network.getNumInputs(), network.getHiddenLayers(), network.getNumOutputs());
		e.experimentIndividualNetwork(network, trainingSet, testSet, path, networkInfo);
		finishTraining(e, path);
	}
	
	/**
	 * Makes experiments with the user and pattern specified.
	 * Experiments: varies the number of units on a layer and the number of layers.
	 * 
	 * All the results are stored on files at experiments folder.
	 * 
	 * @param userId Authentic user
	 * @param patternId Pattern used
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void pTraining(int userId, int patternId) throws ClassNotFoundException, SQLException, IOException {
		SeparatedDataSet dataset = DataSetProvider.getPositiveDataSet(userId, patternId);
		String path = initDirs("pTraining", userId, patternId);
		int inputSize = dataset.getTrainingData().getInputSize();
		executeTraining(1, 3, inputSize, inputSize * 2, dataset, path);
		System.out.println("Experimentos P com o usuário " + userId + " e padrão " + patternId + " concluídos.");
	}
	
	/**
	 * Makes experiments with user and pattern specified, including on the training set
	 * negative samples.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void pnTraining(int userId, int patternId) throws ClassNotFoundException, SQLException, IOException {
		SeparatedDataSet dataset = DataSetProvider.getPNDataSet(userId, patternId);
		String path = initDirs("pnTraining", userId, patternId);
		int inputSize = dataset.getTrainingData().getInputSize();
		executeTraining(1, 3, inputSize, inputSize * 2,  dataset, path);
		System.out.println("Experimentos PN com o usuário " + userId + " e padrão " + patternId + " concluídos.");
	}
	
	/**
	 * Prepares to test with training set with only positive samples.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void individualPTraining(int userId, int patternId) throws IOException, ClassNotFoundException, SQLException {
		SeparatedDataSet dataset = DataSetProvider.getPositiveDataSet(userId, patternId);
		int [] hiddenLayers = {dataset.getTrainingData().getInputSize()};
		
		NeuralNetwork network = new NeuralNetwork(dataset.getTrainingData().getInputSize(), hiddenLayers, 1);
		network.setMaxError(0.001);
		network.setMaxIterations(10000);
		network.setTrainStrategy(new ResilientPropagation(network.getNetwork(), dataset.getTrainingData()));
		
		trainIndividualNetwork(network, dataset.getTrainingData(), dataset.getTestData(), "individualP", userId, patternId);
		System.out.println("Treinamento P individual com usuário " + userId + " e padrão " + patternId + " concluído.");
	}
	
	/**
	 * Prepares to test with training set with positive and negative samples.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void individualPNTraining(int userId, int patternId) throws ClassNotFoundException, SQLException, IOException {
		SeparatedDataSet dataset = DataSetProvider.getPNDataSet(userId, patternId);
		int [] hiddenLayers = {dataset.getTrainingData().getInputSize()};
		
		NeuralNetwork network = new NeuralNetwork(dataset.getTrainingData().getInputSize(), hiddenLayers, 1);
		network.setMaxError(0.001);
		network.setMaxIterations(10000);
		network.setTrainStrategy(new ResilientPropagation(network.getNetwork(), dataset.getTrainingData()));
		
		trainIndividualNetwork(network, dataset.getTrainingData(), dataset.getTestData(), "individualPN", userId, patternId);
		System.out.println("Treinamento PN individual com usuário " + userId + " e padrão " + patternId + " concluído.");
	}
}

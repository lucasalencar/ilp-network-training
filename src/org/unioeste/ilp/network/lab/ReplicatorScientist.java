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

import org.unioeste.ilp.network.ReplicatorNeuralNetwork;
import org.unioeste.ilp.network.daos.SampleDao;
import org.unioeste.ilp.network.datasets.LabeledDataSet;
import org.unioeste.ilp.network.datasets.ReplicatorDataSet;
import org.unioeste.ilp.network.db.SQLiteConnectionSource;
import org.unioeste.ilp.network.util.LabeledDataSetHelper;
import org.unioeste.ilp.network.util.OutputHandler;
import org.unioeste.ilp.network.util.ReplicatorDataSetProvider;

/**
 * Class that prepares all the necessary arrangements
 * to execute the experiments for the replicator neural networks.
 * 
 * @author Lucas André de Alencar
 *
 */
public class ReplicatorScientist extends Scientist {

	private static ReplicatorNeuralNetwork createReplicatorNetworkModel(int inputSize, int [] hiddenLayers) {
		ReplicatorNeuralNetwork network = new ReplicatorNeuralNetwork(inputSize, hiddenLayers);
		network.setMaxError(0.0001);
		network.setMaxIterations(50000);
		return network;
	}
	
	private static ReplicatorNeuralNetwork createReplicatorNetworkModel(LabeledDataSet trainingSet, int [] hiddenLayers) {
		int inputSize = trainingSet.getInputSize();
		return createReplicatorNetworkModel(inputSize, hiddenLayers);
	}
	
	/**
	 * Prepares the training for a single replicator neural network specified.
	 * 
	 * @param network Replicator neural network
	 * @param trainingSet Training set
	 * @param testSet Test set
	 * @param trainingName Training name
	 * @param userId User
	 * @param patternId Pattern
	 * @throws IOException
	 */
	public static void trainIndividualReplicatorNetwork(ReplicatorNeuralNetwork network, LabeledDataSet trainingSet, LabeledDataSet testSet, String trainingName, int userId, int patternId) throws IOException {
		String path = initDirs(trainingName, userId, patternId);
		ReplicatorExperiments e = new ReplicatorExperiments();
		String networkInfo = OutputHandler.networkToString(network.getNumInputs(), network.getHiddenLayers(), network.getNumOutputs());
		e.experimentIndividualNetwork(network, trainingSet, testSet, path, networkInfo);
		finishTraining(e, path);
	}
	
	private static void execVariantWrapperLayersUnits(int minUnits, int maxUnits, ReplicatorNeuralNetwork networkModel, 
			LabeledDataSet trainingSet, LabeledDataSet testSet, String path) throws IOException {

		ReplicatorExperiments e = new ReplicatorExperiments(networkModel);
		e.variantWrapperLayersUnits(minUnits, maxUnits, trainingSet, testSet, path);
		finishTraining(e, path);
	}
	
	/**
	 * Prepares for the training with variant units on the hidden layers.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void replicatorVariantUnitsTraining(int userId, int patternId) throws ClassNotFoundException, SQLException, IOException {
		ReplicatorDataSet dataset = ReplicatorDataSetProvider.getReplicatorDataSet(userId, patternId);
		String path = initDirs("replicatorVariantUnits", userId, patternId);
		int inputSize = dataset.getTrainingSet().getInputSize();
		int minUnits = 3;
		
		int [] hiddenLayers = {minUnits, 3, minUnits};
		LabeledDataSet trainingSet = LabeledDataSetHelper.normalize(dataset.getTrainingSet());
		LabeledDataSet testSet = LabeledDataSetHelper.normalize(dataset.getTestSet());
		ReplicatorNeuralNetwork networkModel = createReplicatorNetworkModel(trainingSet, hiddenLayers);
		
		execVariantWrapperLayersUnits(minUnits, inputSize, networkModel, trainingSet, testSet, path);
		System.out.println("Experimentos com RNN variando unidades com o usuário " + userId + " e padrão " + patternId + " concluídos.\n");
	}
	
	public static void replicatorVariantCentralUnitsTraining(int userId, int patternId) throws ClassNotFoundException, SQLException, IOException {
		ReplicatorDataSet dataset = ReplicatorDataSetProvider.getReplicatorDataSet(userId, patternId);
		String path = initDirs("replicatorVariantCentralUnits", userId, patternId);
		int inputSize = dataset.getTrainingSet().getInputSize();
		
		int [] hiddenLayers = {inputSize / 2, 3, inputSize / 2};
		LabeledDataSet trainingSet = LabeledDataSetHelper.normalize(dataset.getTrainingSet());
		LabeledDataSet testSet = LabeledDataSetHelper.normalize(dataset.getTestSet());
		ReplicatorNeuralNetwork networkModel = createReplicatorNetworkModel(trainingSet, hiddenLayers);
		
		ReplicatorExperiments e = new ReplicatorExperiments(networkModel);
		e.variantCentralLayerUnits(3, inputSize, inputSize, trainingSet, testSet, path);
		finishTraining(e, path);
		System.out.println("Experimentos com RNN variando unidades com o usuário " + userId + " e padrão " + patternId + " concluídos.\n");
	}
	
	private static void execVariantMaxError(double minError, double maxError, double decrement, ReplicatorNeuralNetwork networkModel,
			LabeledDataSet trainingSet, LabeledDataSet testSet, String path) throws IOException {
		
		ReplicatorExperiments e = new ReplicatorExperiments(networkModel);
		e.variantMaxError(minError, maxError, decrement, trainingSet, testSet, path);
		finishTraining(e, path);
	}
	
	/**
	 * Prepares for the tests with variant max error.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void replicatorVariantMaxError(int userId, int patternId) throws ClassNotFoundException, SQLException, IOException {
		ReplicatorDataSet dataset = ReplicatorDataSetProvider.getReplicatorDataSet(userId, patternId);
		String path = initDirs("replicatorVariantMaxError", userId, patternId);
		int inputSize = dataset.getTrainingSet().getInputSize();
		
		int [] hiddenLayers = {inputSize / 2, 3, inputSize / 2};
		LabeledDataSet trainingSet = LabeledDataSetHelper.normalize(dataset.getTrainingSet());
		LabeledDataSet testSet = LabeledDataSetHelper.normalize(dataset.getTestSet());
		ReplicatorNeuralNetwork networkModel = createReplicatorNetworkModel(trainingSet, hiddenLayers);
		
		execVariantMaxError(0.1, 0.000001, 0.1, networkModel, trainingSet, testSet, path);
		System.out.println("Experimentos com RNN variando erro máximo com o usuário " + userId + " e padrão " + patternId + " concluídos.\n");
	}
	
	private static int inputSize(int userId, int patternId) throws SQLException, ClassNotFoundException {
		SQLiteConnectionSource conn = new SQLiteConnectionSource(SQLiteConnectionSource.DEFAULT_DB_PATH);
		SampleDao sampleDao = new SampleDao(conn.getConnectionSource());
		return sampleDao.count(userId, patternId) * 3 - 1;
	}
	
	/**
	 * Prepares for the tests with variant number of examples used on the training.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void replicatorVariantNumExamples(int userId, int patternId) throws SQLException, ClassNotFoundException, IOException {
		int inputSize = inputSize(userId, patternId);
		int [] hiddenLayers = {inputSize / 2, 3, inputSize / 2};
		
		ReplicatorNeuralNetwork network = createReplicatorNetworkModel(inputSize, hiddenLayers);
		String path = initDirs("replicatorVariantNumExamples", userId, patternId);
		
		ReplicatorExperiments e = new ReplicatorExperiments(network);
		e.variantNumExamplesPercent(5, 80, 5, userId, patternId, path);
		finishTraining(e, path);
		System.out.println("Experimentos com RNN variando número de exemplos com o usuário " + userId + " e padrão " + patternId + " concluídos.\n");
	}
	
	/**
	 * Prepares the ultimate test that involves user 15 (user on database that has the greater number of samples)
	 * for the pattern passed.
	 * 
	 * @param patternId Pattern
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void ultimateTest(int patternId) throws SQLException, ClassNotFoundException, IOException {
		int inputSize = inputSize(15, patternId);
		int [] hiddenLayers = {inputSize / 2, 8, inputSize / 2};
		
		ReplicatorNeuralNetwork network = createReplicatorNetworkModel(inputSize, hiddenLayers);
		String path = initDirs("ultimateTest", 15, patternId);
		
		ReplicatorExperiments e = new ReplicatorExperiments(network);
		e.variantNumExamplesPercent(5, 80, 5, 15, patternId, 100, path);
		finishTraining(e, path);
		System.out.println("Experimentos com RNN variando número de exemplos com o usuário " + 15 + " e padrão " + patternId + " concluídos.\n");
	}
}

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.unioeste.ilp.network.AbstractNeuralNetwork;
import org.unioeste.ilp.network.ReplicatorNeuralNetwork;
import org.unioeste.ilp.network.datasets.LabeledDataSet;
import org.unioeste.ilp.network.datasets.ReplicatorDataSet;
import org.unioeste.ilp.network.train.Trainer;
import org.unioeste.ilp.network.util.DataSetHelper;
import org.unioeste.ilp.network.util.LabeledDataSetHelper;
import org.unioeste.ilp.network.util.OutputHandler;
import org.unioeste.ilp.network.util.ReplicatorDataSetProvider;

/**
 * Encapsulates experiments with replicator neural networks.
 * 
 * @author Lucas André de Alencar
 *
 */
public class ReplicatorExperiments extends AbstractExperiments {

	public ReplicatorExperiments() {
		super();
	}
	
	public ReplicatorExperiments(ReplicatorNeuralNetwork networkModel) {
		super(networkModel);
	}
	
	/**
	 * Experiment that varies the number of wrapper layer's units.
	 * The wrapper layers are the second and forth one, that wraps
	 * the middle layer. The number of units varies on these layers.
	 * 
	 * @param minUnit Min units
	 * @param maxUnit Max units
	 * @param trainingSet Training set
	 * @param testSet Test set
	 * @param dir Directory where to save the network
	 * @throws IOException
	 */
	public void variantWrapperLayersUnits(int minUnit, int maxUnit, LabeledDataSet trainingSet, LabeledDataSet testSet, String dir) throws IOException {
		System.out.println("Treinamento: variando unidades em camadas ocultas");
		int [] hiddenLayers = new int[3];
		for (int i = minUnit; i <= maxUnit; i++) {
			hiddenLayers[0] = i;
			hiddenLayers[1] = 3;
			hiddenLayers[2] = i;
			
			ReplicatorNeuralNetwork network = networkFactory.factoryReplicatorNetwork(hiddenLayers);
			MLTrain training = new Backpropagation(network.getNetwork(), trainingSet);
			network.setTrainStrategy(training);
			
			String networkInfo = OutputHandler.networkToString(network.getNumInputs(), network.getHiddenLayers(), network.getNumOutputs());
			
			System.out.println("===================================");
			System.out.println("rede=" + networkInfo + "; min=" + minUnit + "; max=" + maxUnit + "; treinamento=" + trainingSet.size() + "; testes=" + testSet.size());
			
			experimentIndividualNetwork(network, trainingSet, testSet, dir, networkInfo);
		}
	}
	
	/**
	 * Experiment that varies the number of units on the middle layer.
	 * 
	 * @param minUnit Min units
	 * @param maxUnit Max units
	 * @param wrapperLayerUnits Number of units on the wrapper layers
	 * @param trainingSet Training set
	 * @param testSet Test set
	 * @param dir Directory where to save the network
	 * @throws IOException
	 */
	public void variantCentralLayerUnits(int minUnit, int maxUnit, int wrapperLayerUnits, LabeledDataSet trainingSet, LabeledDataSet testSet, String dir) throws IOException {
		System.out.println("Treinamento: variando unidades em camadas oculta central");
		int [] hiddenLayers = {wrapperLayerUnits, 3, wrapperLayerUnits};
		for (int i = minUnit; i <= maxUnit; i++) {
			hiddenLayers[1] = i;
			
			ReplicatorNeuralNetwork network = networkFactory.factoryReplicatorNetwork(hiddenLayers);
			MLTrain training = new Backpropagation(network.getNetwork(), trainingSet);
			network.setTrainStrategy(training);
			
			String networkInfo = OutputHandler.networkToString(network.getNumInputs(), network.getHiddenLayers(), network.getNumOutputs());
			
			System.out.println("===================================");
			System.out.println("rede=" + networkInfo + "; min=" + minUnit + "; max=" + maxUnit + "; treinamento=" + trainingSet.size() + "; testes=" + testSet.size());
			
			experimentIndividualNetwork(network, trainingSet, testSet, dir, networkInfo);
		}
	}
	
	/**
	 * Experiment that varies the permited max error on the training.
	 * The max error is decremented on each iteration.
	 * 
	 * @param minError Min error
	 * @param maxError Max error
	 * @param decrement Decrement
	 * @param trainingSet Training set
	 * @param testSet Test set
	 * @param dir Directory where to save the network
	 * @throws IOException
	 */
	public void variantMaxError(double minError, double maxError, double decrement, LabeledDataSet trainingSet, LabeledDataSet testSet, String dir) throws IOException {
		System.out.println("Treinamento: variando erro máximo");
		for (double error = minError; error >= maxError; error *= decrement) {
			ReplicatorNeuralNetwork network = networkFactory.factoryReplicatorNetwork(error);
			MLTrain training = new Backpropagation(network.getNetwork(), trainingSet);
			network.setTrainStrategy(training);
			
			System.out.println("===================================");
			System.out.println("error=" + error + "; min=" + minError + "; max=" + maxError + "; decremento=" + decrement + "; treinamento=" + trainingSet.size() + "; testes=" + testSet.size());
			
			experimentIndividualNetwork(network, trainingSet, testSet, dir, "" + String.format(Locale.US, "%.10f", error));
		}
	}
	
	/**
	 * Experiment that varies the number of examples used on each training.
	 * The increment is based on the number of examples on the dataset.
	 * 
	 * @param minExamples Min examples
	 * @param maxExamples Max examples
	 * @param increment Increment
	 * @param userId User
	 * @param patternId Pattern
	 * @param dir Directory where to save the network
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void variantNumExamplesUnit(int minExamples, int maxExamples, int increment, int userId, int patternId, String dir) throws ClassNotFoundException, SQLException, IOException {
		System.out.println("Treinamento: variando número de exemplos por unidade");
		MLDataSet positive = ReplicatorDataSetProvider.getReplicatorSamples(userId, patternId, ReplicatorDataSetProvider.DEFAULT_SAMPLES_SET_SIZE);
		ReplicatorNeuralNetwork network = networkFactory.factoryReplicatorNetwork();
		
		for (int numExamples = minExamples; numExamples <= maxExamples; numExamples += increment) {
			int testSetSize = ((int) positive.getRecordCount() - numExamples);
			MLDataSet addTestSet = ReplicatorDataSetProvider.getNegativeReplicatorSamples(userId, patternId, testSetSize);
			ReplicatorDataSet dataset = ReplicatorDataSetProvider.separateDataSet(positive, addTestSet, testSetSize);
			dataset = normalizeReplicatorDataSet(dataset);
			
			MLTrain training = new Backpropagation(network.getNetwork(), dataset.getTrainingSet());
			network.setTrainStrategy(training);
			
			System.out.println("===================================");
			System.out.println("exemplos=" + numExamples + "; min=" + minExamples + "; max" + maxExamples + "; incremento=" + increment + "; treinamento=" + dataset.getTrainingSet().size() + "; testes=" + dataset.getTestSet().size());
			
			experimentIndividualNetwork(network, dataset.getTrainingSet(), dataset.getTestSet(), dir, "" + numExamples);
		}
	}
	
	/**
	 * Experiment that varies the number of examples on each training.
	 * The increment is based on the percentage of elements on the dataset.
	 * 
	 * @param minPercent Min percent
	 * @param maxPercent Max percent
	 * @param percentIncrement Percent increment
	 * @param userId User
	 * @param patternId Pattern
	 * @param sampleSize Sample size
	 * @param dir Directory where to save the network
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void variantNumExamplesPercent(double minPercent, double maxPercent, int percentIncrement, int userId, int patternId, int sampleSize, String dir) throws ClassNotFoundException, SQLException, IOException {
		System.out.println("Treinamento: variando número de exemplos por porcentagem");
		MLDataSet positive = ReplicatorDataSetProvider.getReplicatorSamples(userId, patternId, sampleSize);
		ReplicatorNeuralNetwork network = networkFactory.factoryReplicatorNetwork();
		
		for (double percent = minPercent; percent <= maxPercent; percent += percentIncrement) {
			int numExamples = DataSetHelper.datasetNumElementsPercent(positive, percent);
			int testSetSize = ((int) positive.getRecordCount() - numExamples);
			MLDataSet addTestSet = ReplicatorDataSetProvider.getNegativeReplicatorSamples(userId, patternId, testSetSize);
			ReplicatorDataSet dataset = ReplicatorDataSetProvider.separateDataSet(positive, addTestSet, testSetSize);
			dataset = normalizeReplicatorDataSet(dataset);
			
			MLTrain training = new Backpropagation(network.getNetwork(), dataset.getTrainingSet());
			network.setTrainStrategy(training);
			
			System.out.println("===================================");
			System.out.println("porcentagem=" + percent + "; min=" + minPercent + "; max=" + maxPercent + "; incremento=" + percentIncrement + "; treinamento=" + dataset.getTrainingSet().size() + "; testes=" + dataset.getTestSet().size());
			
			experimentIndividualNetwork(network, dataset.getTrainingSet(), dataset.getTestSet(), dir, "" + numExamples);
		}
	}
	
	/**
	 * Same as variantNumExamplesPercent but has a default value of samples size associated.
	 * 
	 * @param minPercent Min percent
	 * @param maxPercent Max percent
	 * @param percentIncrement Percent increment
	 * @param userId User
	 * @param patternId Pattern
	 * @param dir Directory where to save the network
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void variantNumExamplesPercent(double minPercent, double maxPercent, int percentIncrement, int userId, int patternId, String dir) throws ClassNotFoundException, SQLException, IOException {
		variantNumExamplesPercent(minPercent, maxPercent, percentIncrement, userId, patternId, ReplicatorDataSetProvider.DEFAULT_SAMPLES_SET_SIZE, dir);
	}
	
	private ReplicatorDataSet normalizeReplicatorDataSet(ReplicatorDataSet dataset) {
		LabeledDataSet trainingSet = LabeledDataSetHelper.normalize(dataset.getTrainingSet());
		LabeledDataSet testSet = LabeledDataSetHelper.normalize(dataset.getTestSet());
		return new ReplicatorDataSet(trainingSet, testSet);
	}
	
	@Override
	protected void trainNetwork(AbstractNeuralNetwork network, String fileName) throws IOException {
		List<String> output = new ArrayList<String>();
		output = Trainer.train(network);
		OutputHandler.writeOutput(fileName, output);
	}

	@Override
	protected void testNetwork(AbstractNeuralNetwork nn, MLDataSet testSet, String fileName) throws IOException {
		List<String> output = new ArrayList<String>();
		int fa = 0, fr = 0;
		
		LabeledDataSet labeledTestSet = (LabeledDataSet) testSet;
		ReplicatorNeuralNetwork network = (ReplicatorNeuralNetwork) nn;
		
		for (int i = 0; i < labeledTestSet.size(); i++) {
			MLDataPair pair = labeledTestSet.get(i);
			MLData result = network.compute(pair);
			
			double error = network.calculateError(pair.getIdeal(), result);
			output.add(labeledTestSet.getLabel(i) + "\t" + String.format(Locale.US, "%.20f", error));
			if (isFA(labeledTestSet.getLabel(i), network.getTrainError(), error)) fa++;
			if (isFR(labeledTestSet.getLabel(i), network.getTrainError(), error)) fr++;
		}
		// Writes the results to a file
		OutputHandler.writeOutput(fileName, output);
		reporter.report(fa, labeledTestSet.getNumNegatives(), fr, labeledTestSet.getNumPositives());
	}
	
	private boolean isFA(double label, double trainError, double actualError) {
		return label == 0.0 && trainError > actualError;
	}

	private boolean isFR(double label, double trainError, double actualError) {
		return label == 1.0 && trainError <= actualError;
	}
}

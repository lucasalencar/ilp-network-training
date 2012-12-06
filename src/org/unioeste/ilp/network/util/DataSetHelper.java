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

import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.unioeste.ilp.network.ReplicatorNeuralNetwork;

/**
 * Encapsulates some methods used on dataset manipulations.
 * 
 * Most of the classes used are from Encog.
 * 
 * @author Lucas André de Alencar
 *
 */
public class DataSetHelper {
	
	/**
	 * Merges 2 MLDataSet in 1.
	 * 
	 * @param d1 Dataset 1
	 * @param d2 Dataset 2
	 * @return MLDataSet merged
	 */
	public static MLDataSet merge(MLDataSet d1, MLDataSet d2) {
		MLDataSet newData = new BasicMLDataSet();
		copyDataSetTo(d1, newData);
		copyDataSetTo(d2, newData);
		return newData;
	}
	
	/**
	 * Copies the MLDataSet to another instance of MLDataSet.
	 * 
	 * @param data Origin
	 * @param newData Destination
	 */
	public static void copyDataSetTo(MLDataSet data, MLDataSet newData) {
		copyDataSetTo(data, newData, 0, data.size() - 1);
	}
	
	/**
	 * Copies a subset of a MLDataSet to another instance of MLDataSet.
	 * 
	 * @param data Origin
	 * @param newData Destination
	 * @param begin Begin
	 * @param end End
	 */
	public static void copyDataSetTo(MLDataSet data, MLDataSet newData, int begin, int end) {
		for (int i = begin; i <= end; i++) {
			MLDataPair pair = data.get(i);
			newData.add(pair);
		}
	}
	
	/**
	 * Randomizes the examples on the MLDataSet.
	 * 
	 * @param dataset Original
	 * @return MLDataSet randomized
	 */
	public static MLDataSet randomize(MLDataSet dataset) {
		List<Integer> visited = new ArrayList<Integer>();
		for (int i = 0; i < dataset.getRecordCount(); i++) {
			visited.add(i);
		}
		
		MLDataSet newDataset = new BasicMLDataSet();
		while (visited.size() > 0) {
			int i = (int) (Math.random() * visited.size()); 
			newDataset.add(dataset.get(i));
			visited.remove(i);
		}
		return newDataset;
	}
	
	/**
	 * Randomizes the 2 datasets on one, respecting the order of each example.
	 * Just mix the examples randomically in one dataset.
	 * 
	 * @param positiveDataset Dataset 1
	 * @param negativeDataset Dataset 2
	 * @return MLDataSet mixed randomically
	 */
	public static MLDataSet randomize(MLDataSet positiveDataset, MLDataSet negativeDataset) {
		List<MLDataPair> positives = datasetToList(positiveDataset);
		List<MLDataPair> negatives = datasetToList(negativeDataset);
		
		MLDataSet newDataset = new BasicMLDataSet();
		while (positives.size() > 0 || negatives.size() > 0) {
			newDataset.add(randomDataPair(positives, negatives));
		}
		return newDataset;
	}
	
	/**
	 * Converts a MLDataSet to a List of MLDataPair.
	 * 
	 * @param dataset MLDataSet
	 * @return List of MLDataPair
	 */
	private static List<MLDataPair> datasetToList(MLDataSet dataset) {
		List<MLDataPair> list = new ArrayList<MLDataPair>();
		for (int i = 0; i < dataset.getRecordCount(); i++) {
			list.add(dataset.get(i));
		}
		return list;
	}
	
	/**
	 * Chooses a pair from one of the 2 lists randomically.
	 * 
	 * @param dt1 Dataset 1
	 * @param dt2 Dataset 2
	 * @return random MLDataPair
	 */
	private static MLDataPair randomDataPair(List<MLDataPair> dt1, List<MLDataPair> dt2) {
		boolean positive = Math.random() > 0.5;
		
		if (positive && dt1.size() > 0) {
			return getAndRemove(dt1, 0);
		} else if (!positive && dt2.size() > 0) {
			return getAndRemove(dt2, 0);
		}
		
		if (dt1.size() > 0) {
			return getAndRemove(dt1, 0);
		} else if (dt2.size() > 0) {
			return getAndRemove(dt2, 0);
		}
		
		return null;
	}
	
	/**
	 * Gets the pair from the list and removes it from the list.
	 * 
	 * @param list List of MLDataPair
	 * @param index int
	 * @return MLDataPair
	 */
	private static MLDataPair getAndRemove(List<MLDataPair> list, int index) {
		MLDataPair pair = list.get(index);
		list.remove(index);
		return pair;
	}
	
	/**
	 * Counts the number of elements that corresponds to the percentage specified.
	 * 
	 * @param dataset MLDataSet
	 * @param percent double
	 * @return Number of elements from the dataset
	 */
	public static int datasetNumElementsPercent(MLDataSet dataset, double percent) {
		return (int) Math.round(dataset.getRecordCount() * (percent / 100));
	}
	
	/**
	 * Maps the input dataset to a 2 dimension matrix.
	 * 
	 * @param dataset MLDataSet
	 * @return 2 dimension double matrix
	 */
	public static double[][] mapInputDataset(MLDataSet dataset) {
		double[][] mapped = new double[(int) dataset.getRecordCount()][dataset.getInputSize()];
		for (int i = 0; i < dataset.getRecordCount(); i++) {
			mapped[i] = dataset.get(i).getInputArray();
		}
		return mapped;
	}
	
	/**
	 * Maps the ideal dataset toa 2 dimension matrix.
	 * 
	 * @param dataset MLDataSet
	 * @return 2 dimension double matrix
	 */
	public static double[][] mapIdealDataset(MLDataSet dataset) {
		double[][] mapped = new double[(int) dataset.getRecordCount()][dataset.getInputSize()];
		for (int i = 0; i < dataset.getRecordCount(); i++) {
			mapped[i] = dataset.get(i).getIdealArray();
		}
		return mapped;
	}
	
	/**
	 * Normalizes the dataset to use on a replicator neural network.
	 * Uses the limits specified on the ReplicatorNeuralNetwork class.
	 * 
	 * @param dataset MLDataSet
	 * @return MLDataSet normalized
	 */
	public static MLDataSet normalizeReplicator(MLDataSet dataset) {
		MLDataSet normalized = new BasicMLDataSet();
		double[][] inputs = DataSetNormalizer.normalize(
				ReplicatorNeuralNetwork.input_low_norm, ReplicatorNeuralNetwork.input_high_norm,
				ReplicatorNeuralNetwork.output_low_norm, ReplicatorNeuralNetwork.output_high_norm,
				mapInputDataset(dataset)
		);
		double[][] ideals = DataSetNormalizer.normalize(
				ReplicatorNeuralNetwork.input_low_norm, ReplicatorNeuralNetwork.input_high_norm,
				ReplicatorNeuralNetwork.output_low_norm, ReplicatorNeuralNetwork.output_high_norm,
				mapIdealDataset(dataset)
		);
		for (int i = 0; i < inputs.length; i++) {
			MLDataPair pair = BasicMLDataPair.createPair(dataset.getInputSize(), dataset.getIdealSize());
			dataset.getRecord(i, pair);
			pair.setInputArray(inputs[i]);
			pair.setIdealArray(ideals[i]);
			normalized.add(pair);
		}
		return normalized;
	}
	
	/**
	 * Normalizes just the inputs list on the dataset.
	 * Uses the limits specified on the ReplicatorNeuralNetwork class.
	 * 
	 * @param dataset MLDataSet
	 * @return MLDataSet
	 */
	public static MLDataSet normalizeInput(MLDataSet dataset) {
		MLDataSet normalized = new BasicMLDataSet();
		double[][] n = DataSetNormalizer.normalize(
				ReplicatorNeuralNetwork.input_low_norm, ReplicatorNeuralNetwork.input_high_norm,
				ReplicatorNeuralNetwork.output_low_norm, ReplicatorNeuralNetwork.output_high_norm,
				mapInputDataset(dataset)
		);
		for (int i = 0; i < n.length; i++) {
			MLDataPair pair = BasicMLDataPair.createPair(dataset.getInputSize(), dataset.getIdealSize());
			dataset.getRecord(i, pair);
			pair.setInputArray(n[i]);
			normalized.add(pair);
		}
		return normalized;
	}
	
	/**
	 * Normalizes just the ideal list on the dataset.
	 * Uses the limits specified on the ReplicatorNeuralNetwork class.
	 * 
	 * @param dataset MLDataSet
	 * @return MLDataSet
	 */
	public static MLDataSet normalizeIdeal(MLDataSet dataset) {
		MLDataSet normalized = new BasicMLDataSet();
		double[][] n = DataSetNormalizer.normalize(
				ReplicatorNeuralNetwork.input_low_norm, ReplicatorNeuralNetwork.input_high_norm,
				ReplicatorNeuralNetwork.output_low_norm, ReplicatorNeuralNetwork.output_high_norm,
				mapIdealDataset(dataset)
		);
		for (int i = 0; i < n.length; i++) {
			MLDataPair pair = BasicMLDataPair.createPair(dataset.getInputSize(), dataset.getIdealSize());
			dataset.getRecord(i, pair);
			pair.setIdealArray(n[i]);
			normalized.add(pair);
		}
		return normalized;
	}
}

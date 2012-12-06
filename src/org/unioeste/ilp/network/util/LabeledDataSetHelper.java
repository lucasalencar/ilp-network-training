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

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.unioeste.ilp.network.ReplicatorNeuralNetwork;
import org.unioeste.ilp.network.datasets.LabeledDataSet;

/**
 * Encapsulates commom methods used with LabeledDataSet class.
 * 
 * @author Lucas André de Alencar
 *
 */
public class LabeledDataSetHelper extends DataSetHelper {

	/**
	 * Normalizes the datasets included in the labeled data set.
	 * Uses the limits of normalization defined on the ReplicatorNeuralNetwork class.
	 * 
	 * @param dataset LabeledDataSet
	 * @return LabeledDataSet
	 */
	public static LabeledDataSet normalize(LabeledDataSet dataset) {
		LabeledDataSet normalized = new LabeledDataSet();
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
			normalized.add(pair, dataset.getLabel(i));
		}
		return normalized;
	}
	
}

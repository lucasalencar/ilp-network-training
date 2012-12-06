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

package org.unioeste.ilp.network.datasets;

/**
 * Specific dataset used by Replicator Neural Networks.
 * 
 * Contains 2 datasets, one for training and other for tests.
 * 
 * @author Lucas André de Alencar
 *
 */
public class ReplicatorDataSet {

	private LabeledDataSet trainingSet;
	private LabeledDataSet testSet;
	
	public ReplicatorDataSet() {
		trainingSet = new LabeledDataSet();
		testSet = new LabeledDataSet();
	}
	
	public ReplicatorDataSet(LabeledDataSet trainingSet, LabeledDataSet testSet) {
		this.trainingSet = trainingSet;
		this.testSet = testSet;
	}
	
	public void setTrainingSet(LabeledDataSet trainingSet) {
		this.trainingSet = trainingSet;
	}
	
	public void setTestSet(LabeledDataSet testSet) {
		this.testSet = testSet;
	}
	
	public LabeledDataSet getTrainingSet() {
		return trainingSet;
	}
	
	public LabeledDataSet getTestSet() {
		return testSet;
	}
}

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

import java.util.List;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;

/**
 * Dataset that separates the data into 2 partitions.
 * One used for training and other for tests.
 * 
 * The dataset may be used as a normal dataset and when necessary,
 * the test dataset can be added.
 * 
 * @author Lucas André de Alencar
 *
 */
public class SeparatedDataSet extends BasicMLDataSet {

	private static final long serialVersionUID = 7780117384199947990L;

	private BasicMLDataSet testData;
	
	public SeparatedDataSet() {
		super();
	}
	
	public SeparatedDataSet(final double[][] input, final double[][] ideal) {
		super(input, ideal);
	}
	
	public SeparatedDataSet(final List<MLDataPair> theData) {
		super(theData);
	}
	
	public SeparatedDataSet(final MLDataSet set) {
		super(set);
	}
	
	public MLDataSet getTestData() {
		return this.testData;
	}
	
	public MLDataSet getTrainingData() {
		return this;
	}
	
	public void setTrainingData(MLDataSet dataset) {
		setData(((BasicMLDataSet) dataset).getData());
	}
	
	public void add(MLDataSet theData, int begin, int end) {
		for (int i = begin; i <= end; i++) {
			this.add(theData.get(i));
		}
	}
	
	public void setTestSet(double[][] input, double[][] ideal) {
		testData = new BasicMLDataSet(input, ideal);
	}
	
	public void setTestSet(final List<MLDataPair> theData) {
		testData = new BasicMLDataSet(theData);
	}
	
	public void setTestSet(final MLDataSet set) {
		testData = new BasicMLDataSet(set);
	}
	
	public void setTestSet(final MLDataSet set, int begin, int end) {
		BasicMLDataSet dataset = new BasicMLDataSet();
		for (int i = begin; i <= end; i++) {
			dataset.add(set.get(i));
		}
		this.testData = dataset;
	}
}

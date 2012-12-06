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

import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;

/**
 * Dataset that maps all the labels on an array of values.
 * Each row on the dataset has an label.
 * 
 * Separates the labels in 1.0 (positive) and 0.0 (negative).
 * 
 * @author Lucas André de Alencar
 *
 */
public class LabeledDataSet extends BasicMLDataSet {
	
	private static final long serialVersionUID = 5798083182160514599L;
	
	protected List<Double> labels;
	
	public LabeledDataSet() {
		super();
		initLabels();
	}
	
	private void initLabels() {
		labels = new ArrayList<Double>();
	}
	
	public LabeledDataSet(MLDataSet dataset) {
		super(dataset);
		initLabels();
	}
	
	public LabeledDataSet(MLDataSet dataset, List<Double> labels) {
		super(dataset);
		this.labels = labels;
	}
	
	private void validateLabels(List<MLDataPair> dataset, List<Double> labels) {
		if (dataset.size() != labels.size())
			throw new IllegalStateException("Number os labels different from the dataset size.");
	}
	
	private void validateLabels(List<Double> labels, int begin, int end) {
		if (labels.size() == end - begin)
			throw new IllegalStateException("Number os labels different from the dataset size. Verify if begin and end matchs with the labels size.");
	}
	
	private void validateLabels(List<MLDataPair> dataset, double [] labels) {
		if (dataset.size() != labels.length)
			throw new IllegalStateException("Number os labels different from the dataset size.");
	}
	
	private void validateLabels(double [] labels, int begin, int end) {
		if (labels.length == end - begin)
			throw new IllegalStateException("Number os labels different from the dataset size. Verify if begin and end matchs with the labels size.");
	}
	
	public void setLabels(List<Double> labels) {
		this.labels = labels;
	}
	
	public List<Double> getLabels() {
		return labels;
	}
	
	public void setLabels(double [] labels) {
		for (int i = 0; i < labels.length; i++) {
			this.labels.add(labels[i]);
		}
	}
	
	public double getLabel(int index) {
		return this.labels.get(index);
	}
	
	/**
	 * Add the data pair with specified label.
	 * 
	 * @param inputData Data pair
	 * @param label Label
	 */
	public void add(MLDataPair inputData, double label) {
		super.add(inputData);
		this.labels.add(label);
	}
	
	/**
	 * Adds the subset data with specified labels.
	 * 
	 * @param theData Dataset
	 * @param begin Begin position
	 * @param end End position
	 * @param labels Labels
	 */
	public void add(MLDataSet theData, int begin, int end, List<Double> labels) {
		validateLabels(labels, begin, end);
		for (int i = begin; i <= end; i++) {
			this.add(theData.get(i));
		}
		this.labels.addAll(labels);
	}
	
	/**
	 * Adds the subset data with specified labels.
	 * 
	 * @param theData Dataset
	 * @param begin Begin position
	 * @param end End position
	 * @param labels Labels
	 */
	public void add(MLDataSet theData, int begin, int end, double [] labels) {
		validateLabels(labels, begin, end);
		for (int i = begin; i <= end; i++) {
			this.add(theData.get(i));
		}
		
		for (int i = 0; i < labels.length; i++) {
			this.labels.add(labels[i]);
		}
	}
	
	/**
	 * Adds all the dataset with the labels specified.
	 * 
	 * @param theData Dataset
	 * @param labels Labels
	 */
	public void add(MLDataSet theData, List<Double> labels) {
		this.add(theData, 0, theData.size() - 1, labels);
	}
	
	/**
	 * Adds all the dataset with the labels specified.
	 * 
	 * @param theData Dataset
	 * @param labels Labels
	 */
	public void add(MLDataSet theData, double [] labels) {
		this.add(theData, 0, theData.size() - 1, labels);
	}
	
	/**
	 * Substitutes the dataset and labels on the set.
	 * 
	 * @param theData Dataset
	 * @param labels Labels
	 */
	public void setData(List<MLDataPair> theData, List<Double> labels) {
		validateLabels(theData, labels);
		super.setData(theData);
		this.labels.addAll(labels);
	}
	
	/**
	 * Substitutes the dataset and labels on the set.
	 * 
	 * @param theData Dataset
	 * @param labels Labels
	 */
	public void setData(List<MLDataPair> theData, double [] labels) {
		validateLabels(theData, labels);
		super.setData(theData);
		for (int i = 0; i < labels.length; i++) {
			this.labels.add(labels[i]);
		}
	}
	
	public String toString() {
		String s = 0 + "\t" + labels.get(0) + "\t" + get(0);
		for (int i = 1; i < labels.size(); i++) {
			s = s.concat("\n" + i + "\t" + labels.get(i) + "\t" + get(i));
		}
		return s;
	}
	
	private int sumNumLabels(double label) {
		int sum = 0;
		for (int i = 0; i < labels.size(); i++) {
			if (labels.get(i).equals(label)) sum++;
		}
		return sum;
	}
	
	/**
	 * Counts the number of positive labels on dataset.
	 * 
	 * @return Number of positive labels
	 */
	public int getNumPositives() {
		return sumNumLabels(1.0);
	}
	
	/**
	 * Conts the number of negative labels on dataset.
	 * 
	 * @return Number of negative labels
	 */
	public int getNumNegatives() {
		return sumNumLabels(0.0);
	}
}

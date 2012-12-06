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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLDataSet;
import org.unioeste.ilp.network.datasets.LabeledDataSet;
import org.unioeste.ilp.network.datasets.ReplicatorDataSet;
import org.unioeste.ilp.network.db.SQLiteConnectionSource;
import org.unioeste.ilp.network.train.TrainingSetHandler;

/**
 * Responsible for providing the datasets used
 * on the trainings with replicator neural networks.
 * 
 * @author Lucas André de Alencar
 *
 */
public class ReplicatorDataSetProvider {

	public static final int TEST_PERCENT = 30;
	public static final int DEFAULT_SAMPLES_SET_SIZE = 50;
	
	/**
	 * Separates the dataset into to sets and puts it on a replicator dataset.
	 * 
	 * @param dataset MLDataSet
	 * @param addTestSet Examples added on the test set
	 * @param testSetSize Test set size
	 * @return ReplicatorDataSet
	 */
	public static ReplicatorDataSet separateDataSet(MLDataSet dataset, MLDataSet addTestSet, int testSetSize) {
		LabeledDataSet trainingSet = new LabeledDataSet();
		LabeledDataSet testSet = new LabeledDataSet();
		
		int trainingSetSize = dataset.size() - testSetSize;
		trainingSet.add(dataset, 0, trainingSetSize - 1, generateLabels(trainingSetSize, 1.0));
		testSet.add(dataset, trainingSetSize, dataset.size() - 1, generateLabels(testSetSize, 1.0));
		testSet.add(addTestSet, generateLabels(addTestSet.size(), 0.0));
		
		return new ReplicatorDataSet(trainingSet, testSet);
	}
	
	protected static List<Double> generateLabels(int size, double label) {
		List<Double> labels = new ArrayList<Double>();
		for (int i = 0; i < size; i++) {
			labels.add(label);
		}
		return labels;
	}
	
	/**
	 * Gets positive examples related with user and patterns,
	 * limited by max attempts.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max attempts
	 * @return MLDataSet
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static MLDataSet getReplicatorSamples(int userId, int patternId, int maxAttempts) throws ClassNotFoundException, SQLException {
		TrainingSetHandler tsHandler = new TrainingSetHandler(SQLiteConnectionSource.DEFAULT_DB_PATH);
		MLDataSet dataset = tsHandler.getReplicatorSamples(userId, patternId, maxAttempts);
		tsHandler.closeConnection();
		return dataset;
	}
	
	/**
	 * Gets negative samples related with pattern but not with user,
	 * limited by max attempts.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max attempts
	 * @return MLDataSet
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static MLDataSet getNegativeReplicatorSamples(int userId, int patternId, int maxAttempts) throws SQLException, ClassNotFoundException {
		TrainingSetHandler tsHandler = new TrainingSetHandler(SQLiteConnectionSource.DEFAULT_DB_PATH);
		MLDataSet dataset = tsHandler.getNegativeReplicatorSamples(userId, patternId, maxAttempts);
		tsHandler.closeConnection();
		return dataset;
	}
	
	/**
	 * Gets a replicator dataset with positive and negative examples related with
	 * user and pattern.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return ReplicatorDataSet
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ReplicatorDataSet getReplicatorDataSet(int userId, int patternId) throws ClassNotFoundException, SQLException {
		MLDataSet dataset = getReplicatorSamples(userId, patternId, DEFAULT_SAMPLES_SET_SIZE);		
		int testSetSize = DataSetHelper.datasetNumElementsPercent(dataset, TEST_PERCENT); // Test set size specified by dataset percentage	
		ReplicatorDataSet sepDataSet = (ReplicatorDataSet) separateDataSet(dataset, getNegativeReplicatorSamples(userId, patternId, testSetSize), testSetSize);
		return sepDataSet;
	}
	
	/**
	 * Gets a replicator dataset with positive and negative examples related with
	 * user and pattern, limited by max attempts.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max attempts
	 * @return ReplicatorDataSet
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ReplicatorDataSet getReplicatorDataSet(int userId, int patternId, int maxAttempts) throws ClassNotFoundException, SQLException {
		MLDataSet dataset = getReplicatorSamples(userId, patternId, maxAttempts);
		int testSetSize = DataSetHelper.datasetNumElementsPercent(dataset, TEST_PERCENT); // Test set size specified by dataset percentage	
		ReplicatorDataSet sepDataSet = (ReplicatorDataSet) separateDataSet(dataset, getNegativeReplicatorSamples(userId, patternId, testSetSize), testSetSize);
		return sepDataSet;
	}
}

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

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.unioeste.ilp.network.datasets.SeparatedDataSet;
import org.unioeste.ilp.network.db.SQLiteConnectionSource;
import org.unioeste.ilp.network.train.TrainingSetHandler;

/**
 * Responsible for providing the datasets used on the trainings.
 * 
 * @author Lucas André de Alencar
 *
 */
public class DataSetProvider {

	protected static final int TEST_PERCENT = 30;
	
	/**
	 * Gets the positive sample for training or test.
	 * 
	 * @param userId User ID
	 * @param patternId Pattern ID
	 * @return DataSet with positive samples
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected static MLDataSet getSample(int userId, int patternId) throws ClassNotFoundException, SQLException {
		TrainingSetHandler tsHandler = new TrainingSetHandler(SQLiteConnectionSource.DEFAULT_DB_PATH);
		MLDataSet dataset = tsHandler.getSamples(userId, patternId);
		tsHandler.closeConnection();
		return dataset;
	}
	
	/**
	 * Gets negative samples for tests. Returns any other attempt except from the user specified.
	 * 
	 * @param userId User ID
	 * @param patternId Pattern ID
	 * @param maxAttempts Number of attempts
	 * @return DataSet with negative samples
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected static MLDataSet getNegativeSamples(int userId, int patternId, int maxAttempts) throws ClassNotFoundException, SQLException {
		TrainingSetHandler tsHandler = new TrainingSetHandler(SQLiteConnectionSource.DEFAULT_DB_PATH);
		MLDataSet dataset = tsHandler.getNegativeSamples(userId, patternId, maxAttempts);
		tsHandler.closeConnection();
		return dataset;
	}
	
	/**
	 * Creates a separated data set with a training set
	 * and a test set specified size with some examples to add
	 * to the test set.  
	 * 
	 * @param dataset MLDataSet
	 * @param addTestSet MLDataSet to add to the test set
	 * @param testSetSize Size of the test set
	 * @return SeparatedDataSet
	 */
	public static SeparatedDataSet separateDataSet(MLDataSet dataset, MLDataSet addTestSet, int testSetSize) {
		SeparatedDataSet sepDataSet = new SeparatedDataSet();
		sepDataSet.add(dataset, 0, dataset.size() - testSetSize - 1);
		
		MLDataSet testDataset = new BasicMLDataSet();
		DataSetHelper.copyDataSetTo(dataset, testDataset, dataset.size() - testSetSize, dataset.size() - 1);
		testDataset = DataSetHelper.randomize(testDataset, addTestSet);
		sepDataSet.setTestSet(testDataset);
		
		return sepDataSet;
	}
	
	/**
	 * Gets separated datasets with just positive examples related to user and pattern.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return SeparatedDataSet
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static SeparatedDataSet getPositiveDataSet(int userId, int patternId) throws ClassNotFoundException, SQLException {
		MLDataSet dataset = getSample(userId, patternId);
		int testSetSize = DataSetHelper.datasetNumElementsPercent(dataset, TEST_PERCENT); // Test set size specified by dataset percentage	
		SeparatedDataSet sepDataSet = separateDataSet(dataset, getNegativeSamples(userId, patternId, testSetSize), testSetSize);
		return sepDataSet;
	}
	
	/**
	 * Gets separated datasets with positive and negative examples
	 * related with user and pattern.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return SeparatedDataSet
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static SeparatedDataSet getPNDataSet(int userId, int patternId) throws ClassNotFoundException, SQLException {
		MLDataSet positiveDataset = DataSetProvider.getSample(userId, patternId);
		MLDataSet negativeDataset = DataSetProvider.getNegativeSamples(userId, patternId, (int) positiveDataset.getRecordCount());
		
		// Test set size specified by dataset percentage
		int testSetSize = DataSetHelper.datasetNumElementsPercent(positiveDataset, TEST_PERCENT);
		
		SeparatedDataSet sepDataSet = new SeparatedDataSet();
		sepDataSet.add(positiveDataset, 0, positiveDataset.size() - testSetSize - 1);
		sepDataSet.add(negativeDataset, 0, negativeDataset.size() - testSetSize - 1);
		
		MLDataSet positiveTestDataset = new BasicMLDataSet();
		MLDataSet negativeTestDataset = new BasicMLDataSet();
		
		DataSetHelper.copyDataSetTo(positiveDataset, positiveTestDataset, positiveDataset.size() - testSetSize, positiveDataset.size() - 1);
		DataSetHelper.copyDataSetTo(negativeDataset, negativeTestDataset, negativeDataset.size() - testSetSize, negativeDataset.size() - 1);
		MLDataSet testDataset = DataSetHelper.randomize(positiveTestDataset, negativeTestDataset);
		sepDataSet.setTestSet(testDataset);
		return sepDataSet;
	}
}

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

package org.unioeste.ilp.network.train;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.unioeste.ilp.network.models.Attempt;
import org.unioeste.ilp.network.models.Sample;

/**
 * Handles with some operations with the Training set.
 * 
 * Converts the dataset on strutures used on Encog.
 * 
 * @author Lucas André de Alencar
 *
 */
public class TrainingSetHandler extends GeneralTrainingSetHandler {

	// Number of Characteristics
	private static final int NUMBER_OF_CHARACT = 3;
	
	public TrainingSetHandler(String dbPath) throws ClassNotFoundException, SQLException {
		super(dbPath);
	}
	
	/**
	 * Gets samples related with the user and pattern.
	 * Contructs the input and ideal matrix separated.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return MLDataSet dataset
	 * @throws SQLException
	 */
	public MLDataSet getSamples(int userId, int patternId) throws SQLException {
		createSampleDao();
		createAttemptDao();
		
		int numDots = sampleDao.count(userId, patternId);
		int numAttempts = attemptDao.count(userId, patternId);
		List<Sample> samples = sampleDao.getUserSamples(userId, patternId);
		
		double [][] inputs = constructInputMatrix(samples, numAttempts, numDots * NUMBER_OF_CHARACT);
		double [][] ideals = constructIdealMatrix(numAttempts, 1, 1);
		
		return new BasicMLDataSet(inputs, ideals);
	}
	
	/**
	 * Gets samples related with the user and pattern.
	 * Constructs only the input matrix, making the ideal equals
	 * the input, fulfilling the requirements of a replicator
	 * neural network. 
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return MLDataSet dataset
	 * @throws SQLException
	 */
	public MLDataSet getReplicatorSamples(int userId, int patternId) throws SQLException {
		createSampleDao();
		createAttemptDao();
		
		int numDots = sampleDao.count(userId, patternId);
		int numAttempts = attemptDao.count(userId, patternId);
		List<Sample> samples = sampleDao.getUserSamples(userId, patternId);
		
		double [][] inputs = constructInputMatrix(samples, numAttempts, numDots * NUMBER_OF_CHARACT);
		
		return new BasicMLDataSet(inputs, inputs);
	}
	
	/**
	 * Gets samples related with the user and pattern.
	 * Constructs only the input matrix, making the ideal equals
	 * the input, fulfilling the requirements of a replicator
	 * neural network.
	 * 
	 * Limits the number of attempts by max attempts specified.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max attempts
	 * @return MLDataSet dataset
	 * @throws SQLException
	 */
	public MLDataSet getReplicatorSamples(int userId, int patternId, int maxAttempts) throws SQLException {
		createSampleDao();
		createAttemptDao();
		
		int numDots = sampleDao.count(userId, patternId);
		List<Attempt> attempts = attemptDao.getPositiveAttempts(userId, patternId, maxAttempts);
		List<Sample> samples = sampleDao.getSamplesFromAttempts(attempts);
		
		double [][] inputs = constructInputMatrix(samples, maxAttempts, numDots * NUMBER_OF_CHARACT);
		return new BasicMLDataSet(inputs, inputs);
	}
	
	/**
	 * Gets negative examples from the database related with
	 * the pattern, but not related with user.
	 * Constructs only the input matrix, making the ideal equals
	 * the input, fulfilling the requirements of a replicator
	 * neural network.
	 * Limits the number of attempts by max attempts specified.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max attempts
	 * @return MLDataSet dataset
	 * @throws SQLException
	 */
	public MLDataSet getNegativeReplicatorSamples(int userId, int patternId, int maxAttempts) throws SQLException {
		createSampleDao();
		createAttemptDao();
		
		int numDots = sampleDao.count(userId, patternId);
		List<Attempt> attempts = attemptDao.getNegativeRandomAttempts(userId, patternId, maxAttempts);
		List<Sample> samples = sampleDao.getSamplesFromAttempts(attempts);
		
		double [][] inputs = constructInputMatrix(samples, maxAttempts, numDots * NUMBER_OF_CHARACT);
		return new BasicMLDataSet(inputs, inputs);
	}
	
	/**
	 * Get negative examples from database related with the
	 * pattern but not related with user, limiting the max attempts.
	 * Constructs the inputs and ideals matrix separated.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max attempts
	 * @return MLDataSet dataset
	 * @throws SQLException
	 */
	public MLDataSet getNegativeSamples(int userId, int patternId, int maxAttempts) throws SQLException {
		createSampleDao();
		createAttemptDao();
		
		int numDots = sampleDao.count(userId, patternId);
		List<Attempt> attempts = attemptDao.getNegativeRandomAttempts(userId, patternId, maxAttempts);
		List<Sample> samples = sampleDao.getSamplesFromAttempts(attempts);
		
		double [][] inputs = constructInputMatrix(samples, maxAttempts, numDots * NUMBER_OF_CHARACT);
		double [][] ideals = constructIdealMatrix(maxAttempts, 1, 0);
		
		return new BasicMLDataSet(inputs, ideals);
	}
	
	/**
	 * Constructs the input matrix with columns equals number of inputs
	 * on the dataset and row equals number of examples.
	 * 
	 * @param samples Samples
	 * @param numberOfExamples Number of examples
	 * @param numberOfInput Number of inputs
	 * @return 2 dimensions double matrix
	 * @throws SQLException
	 */
	private double[][] constructInputMatrix(List<Sample> samples, int numberOfExamples, int numberOfInput) throws SQLException {
		double [][] inputs = new double[numberOfExamples][numberOfInput - 1];
		createAttemptDao();
		int attempt_id = samples.get(0).getAttempt(attemptDao.getDao()).getId(), i = 0, j = 0;
		double last_event_time = 0;
		
		Iterator<Sample> itSamples = samples.iterator();
		while (itSamples.hasNext()) {
			Sample sample = itSamples.next();
			Attempt attempt = sample.getAttempt(attemptDao.getDao());
			if (attempt.getId() != attempt_id) {
				i++; j = 0; attempt_id = attempt.getId();
			}
			
			if (j != 0)
				inputs[i][j++] = sample.getEvent_time() - last_event_time;
			
			inputs[i][j++] = sample.getPressure();
			inputs[i][j++] = sample.getPressure_area();
			
			last_event_time = sample.getEvent_time();
		}
		return inputs;
	}
	
	/**
	 * Constructs the ideal matrix with columns equal number of outputs
	 * and rows equals number of examples, labeling the rows with
	 * the specified ideal.
	 * 
	 * The ideal flag can be 0 for false and 1 for true.
	 * 
	 * @param numberOfExamples Number of examples
	 * @param numberOfOutputs Number of outputs
	 * @param ideal int
	 * @return 2 dimension double matrix
	 */
	private double[][] constructIdealMatrix(int numberOfExamples, int numberOfOutputs, int ideal) {
		double [][] ideals = new double[numberOfExamples][numberOfOutputs];
		for (int i = 0; i < numberOfExamples; i++) {
			for (int j = 0; j < numberOfOutputs; j++) {
				ideals[i][j] = ideal;
			}
		}
		return ideals;
	}
}

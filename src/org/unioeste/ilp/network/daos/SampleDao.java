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

package org.unioeste.ilp.network.daos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.unioeste.ilp.network.models.Attempt;
import org.unioeste.ilp.network.models.Sample;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * DAO for the entity Sample on the database.
 * 
 * Uses ORMLite {http://ormlite.com/} for mapping objects on the 
 * database and building queries.
 *  
 * @author Lucas André de Alencar
 *
 */
public class SampleDao {

	private Dao<Sample, Integer> sampleDao;
	private AttemptDao attemptDao;
	
	public SampleDao(ConnectionSource connection) throws SQLException {
		sampleDao = DaoManager.createDao(connection, Sample.class);
		attemptDao = new AttemptDao(connection);
	}
	
	public Dao<Sample, Integer> getDao() {
		return sampleDao;
	}
	
	/**
	 * Performs the query and returns the resulted list.
	 * 
	 * @param query Query
	 * @return List of samples
	 * @throws SQLException
	 */
	public List<Sample> query(QueryBuilder<Sample, Integer> query) throws SQLException {
		return sampleDao.query(query.prepare());
	}
	
	public int count(int attemptId) throws SQLException {
		QueryBuilder<Sample, Integer> query = sampleDao.queryBuilder().setCountOf(true);
		return (int) sampleDao.countOf(query.where().eq("attempt_id", attemptId).prepare());
	}
	
	/**
	 * Counts the number of samples a user has with pattern.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return Number of samples
	 * @throws SQLException
	 */
	public int count(int userId, int patternId) throws SQLException {
		List<Attempt> attempts = attemptDao.getRandomAttempt(userId, patternId, 1);
		return count(attempts.get(0).getId());
	}
	
	/**
	 * Gets all the samples from the attempts.
	 * 
	 * @param attempts List of attempts
	 * @return List of samples
	 * @throws SQLException
	 */
	public List<Sample> getSamplesFromAttempts(List<Attempt> attempts) throws SQLException {
		List<Sample> samples = new ArrayList<Sample>();
		for (int i = 0; i < attempts.size(); i++) {
			samples.addAll(sampleDao.queryForEq("attempt_id", attempts.get(i).getId()));
		}
		return samples;
	}
	
	/**
	 * Gets all the samples from a user with the pattern specified.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return List of samples
	 * @throws SQLException
	 */
	public List<Sample> getUserSamples(int userId, int patternId) throws SQLException {
		QueryBuilder<Attempt, Integer> attemptQuery = attemptDao.joinedExperience(userId, patternId);
		return query(joinedAttempt(attemptQuery));
	}
	
	/**
	 * Builds a joined query between the tables samples and attempts.
	 * 
	 * @param attemptQuery Attempt query
	 * @return Samples query
	 * @throws SQLException
	 */
	public QueryBuilder<Sample, Integer> joinedAttempt(QueryBuilder<Attempt, Integer> attemptQuery) throws SQLException {
		QueryBuilder<Sample, Integer> sampleQuery = sampleDao.queryBuilder();
		sampleQuery.join(attemptQuery);
		return sampleQuery;
	}
}

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
import java.util.List;

import org.unioeste.ilp.network.models.Attempt;
import org.unioeste.ilp.network.models.Experience;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * DAO for the entity Attempt on the database.
 * 
 * Uses ORMLite {http://ormlite.com/} for mapping objects on the 
 * database and building queries.
 * 
 * @author Lucas André de Alencar
 *
 */
public class AttemptDao {

	private Dao<Attempt, Integer> attemptDao;
	private ExperienceDao experienceDao;
	
	public AttemptDao(ConnectionSource connection) throws SQLException {
		attemptDao = DaoManager.createDao(connection, Attempt.class);
		experienceDao = new ExperienceDao(connection);
	}
	
	public Dao<Attempt, Integer> getDao() {
		return attemptDao;
	}
	
	/**
	 * Counts the number of attempts a user has with pattern specified.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return Number of attempts
	 * @throws SQLException
	 */
	public int count(int userId, int patternId) throws SQLException {
		QueryBuilder<Attempt, Integer> query = joinedExperience(userId, patternId).setCountOf(true);
		return (int) attemptDao.countOf(query.prepare());
	}
	
	/**
	 * Makes a query where the attempt table is joined with the experience table.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return Joined query with attempt and experience
	 * @throws SQLException
	 */
	public QueryBuilder<Attempt, Integer> joinedExperience(int userId, int patternId) throws SQLException {
		QueryBuilder<Experience, Integer> expQuery = experienceDao.queryWithSameUserId(userId, patternId);
		QueryBuilder<Attempt, Integer> attemptQuery = attemptDao.queryBuilder();
		return attemptQuery.join(expQuery);
	}
	
	/**
	 * Gets random attempts from the user associated with the pattern specified.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max Attempts
	 * @return List of random attempts
	 * @throws SQLException
	 */
	public List<Attempt> getRandomAttempt(int userId, int patternId, int maxAttempts) throws SQLException {
		QueryBuilder<Attempt, Integer> attemptQuery = joinedExperience(userId, patternId);
		return attemptDao.query(attemptQuery.limit((long) maxAttempts).prepare());
	}
	
	/**
	 * Gets a random list of attempts negatives, that the user is different but the pattern is the same.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max Attempts
	 * @return List of random attempts
	 * @throws SQLException
	 */
	public List<Attempt> getNegativeRandomAttempts(int userId, int patternId, int maxAttempts) throws SQLException {
		QueryBuilder<Experience, Integer> expQuery = experienceDao.queryWithDiffUserId(userId, patternId);
		QueryBuilder<Attempt, Integer> attemptQuery = attemptDao.queryBuilder();
		List<Attempt> attempts = attemptDao.query(attemptQuery.join(expQuery).orderByRaw("random()").limit((long) maxAttempts).prepare());
		return attempts;
	}
	
	/**
	 * Gets the attempts related with user and pattern until reach the max specified.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @param maxAttempts Max Attempts
	 * @return List of attempts
	 * @throws SQLException
	 */
	public List<Attempt> getPositiveAttempts(int userId, int patternId, int maxAttempts) throws SQLException {
		QueryBuilder<Experience, Integer> expQuery = experienceDao.queryWithSameUserId(userId, patternId);
		QueryBuilder<Attempt, Integer> attemptQuery = attemptDao.queryBuilder();
		List<Attempt> attempts = attemptDao.query(attemptQuery.join(expQuery).limit((long) maxAttempts).prepare());
		return attempts;
	}
}

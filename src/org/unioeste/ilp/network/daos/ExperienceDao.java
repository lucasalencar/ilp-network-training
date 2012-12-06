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

import org.unioeste.ilp.network.models.Experience;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * DAO for the entity Experience on the database.
 * 
 * Uses ORMLite {http://ormlite.com/} for mapping objects on the 
 * database and building queries.
 * 
 * @author Lucas André de Alencar
 *
 */
public class ExperienceDao {

	private Dao<Experience, Integer> experienceDao;
	
	public ExperienceDao(ConnectionSource connection) throws SQLException {
		experienceDao = DaoManager.createDao(connection, Experience.class);
	}
	
	public Dao<Experience, Integer> getDao() {
		return experienceDao;
	}
	
	public QueryBuilder<Experience, Integer> queryBuilder() {
		return experienceDao.queryBuilder();
	}
	
	/**
	 * Builds a query where the experiences retrieved must have the same user and pattern specified.
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return Experience query
	 * @throws SQLException
	 */
	public QueryBuilder<Experience, Integer> queryWithSameUserId(int userId, int patternId) throws SQLException {
		QueryBuilder<Experience, Integer> expQuery = experienceDao.queryBuilder();
		expQuery.where().eq("user_id", userId).and().eq("pattern_id", patternId);
		return expQuery;
	}
	
	/**
	 * Bulds a query where the experiences retrieved must have the same pattern but different user. 
	 * 
	 * @param userId User
	 * @param patternId Pattern
	 * @return Experience query
	 * @throws SQLException
	 */
	public QueryBuilder<Experience, Integer> queryWithDiffUserId(int userId, int patternId) throws SQLException {
		QueryBuilder<Experience, Integer> expQuery = experienceDao.queryBuilder();
		expQuery.where().ne("user_id", userId).and().eq("pattern_id", patternId).and().ne("user_id", 1);
		return expQuery;
	}
}

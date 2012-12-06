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

import org.unioeste.ilp.network.models.User;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

/**
 * DAO for the entity User on the database.
 * 
 * Uses ORMLite {http://ormlite.com/} for mapping objects on the 
 * database and building queries.
 * 
 * @author Lucas André de Alencar
 *
 */
public class UserDao {

	Dao<User, Integer> userDao;
	
	public UserDao(ConnectionSource connection) throws SQLException {
		userDao = DaoManager.createDao(connection, User.class);
	}
	
	/**
	 * Counts the number of users on database.
	 * 
	 * @return Number of users
	 * @throws SQLException
	 */
	public int count() throws SQLException {
		return (int) userDao.countOf();
	}
	
	/**
	 * Gets a random user from database.
	 * 
	 * @return Random user id
	 * @throws SQLException
	 */
	public int getRandomUserId() throws SQLException {
		QueryBuilder<User, Integer> query = userDao.queryBuilder();
		query.where().ne("_id", 1);
		query.orderByRaw("random()").limit((long) 1);
		return userDao.query(query.prepare()).get(0).getId();
	}
	
	/**
	 * Gets all the users on the database except the listed on the array.
	 * 
	 * @param usersId Users exception
	 * @return List of users
	 * @throws SQLException
	 */
	public List<User> getUsersExcept(int [] usersId) throws SQLException {
		QueryBuilder<User, Integer> query = userDao.queryBuilder();
		Where<User, Integer> where = query.where();
		for (int i = 0; i < usersId.length; i++) {
			if (i == 0)
				where.ne("_id", usersId[i]);
			else
				where.and().ne("_id", usersId[i]);
		}
		return userDao.query(query.prepare());
	}
}

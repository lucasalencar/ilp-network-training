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

import org.unioeste.ilp.network.daos.AttemptDao;
import org.unioeste.ilp.network.daos.ExperienceDao;
import org.unioeste.ilp.network.daos.SampleDao;
import org.unioeste.ilp.network.db.SQLiteConnectionSource;

/**
 * Abstract class responsible for encapsulate training
 * set handler commom methods.
 * 
 * Just creates all the DAOs needed and initialize 
 * connection with the database.
 * 
 * @author Lucas André de Alencar
 *
 */
public abstract class GeneralTrainingSetHandler {

	private SQLiteConnectionSource connection;
	
	protected ExperienceDao experienceDao;
	protected AttemptDao attemptDao;
	protected SampleDao sampleDao;
	
	public GeneralTrainingSetHandler(String dbPath) throws SQLException, ClassNotFoundException {
		connection = new SQLiteConnectionSource(dbPath);
	}
	
	protected void createExperienceDao() throws SQLException {
		if (experienceDao == null)
			experienceDao = new ExperienceDao(connection.getConnectionSource());
	}
	
	protected void createAttemptDao() throws SQLException {
		if (attemptDao == null)
			attemptDao = new AttemptDao(connection.getConnectionSource());
	}
	
	protected void createSampleDao() throws SQLException {
		if (sampleDao == null)
			sampleDao = new SampleDao(connection.getConnectionSource());
	}
	
	public void closeConnection() throws SQLException {
		connection.closeConnection();
	}
	
}

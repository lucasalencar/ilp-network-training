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

package org.unioeste.ilp.network;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.unioeste.ilp.network.daos.UserDao;
import org.unioeste.ilp.network.db.SQLiteConnectionSource;
import org.unioeste.ilp.network.lab.ReplicatorScientist;
import org.unioeste.ilp.network.lab.Scientist;
import org.unioeste.ilp.network.models.User;

import com.j256.ormlite.logger.LocalLog;

public class Main {
	
	private static final String VARIANT_MAX_ERROR = "variantMaxError";
	private static final String VARIANT_UNITS = "variantUnits";
	private static final String VARIANT_NUM_EXAMPLES = "variantNumExamples";
	private static final String ALL_VARIANTS = "all";
	
	public static int getRandomUserId() throws SQLException, ClassNotFoundException {
		SQLiteConnectionSource connection = new SQLiteConnectionSource(SQLiteConnectionSource.DEFAULT_DB_PATH);
		UserDao userDao = new UserDao(connection.getConnectionSource());
		return userDao.getRandomUserId();
	}
	
	/**
	 * Selects a random user from the database and executes experiments
	 * with all the patterns in the database.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void startExperiments() throws ClassNotFoundException, SQLException, IOException {
		int userId = getRandomUserId();
		System.out.println("Usuário selecionado: " + userId);
		for (int i = 1; i <= 3; i++) {
			Scientist.pTraining(userId, i);
			Scientist.pnTraining(userId, i);
		}
	}
	
	/**
	 * Makes the experiments specified on the option string.
	 * 
	 * @param option
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void replicatorExperiments(String option) throws ClassNotFoundException, SQLException, IOException {
		SQLiteConnectionSource connection = new SQLiteConnectionSource(SQLiteConnectionSource.DEFAULT_DB_PATH);
		UserDao userDao = new UserDao(connection.getConnectionSource());
		int [] exceptions = {1};
		List<User> users = userDao.getUsersExcept(exceptions);
		
		for (int i = 0; i < users.size(); i++) {
			int userId = users.get(i).getId();
			System.out.println("Usuário atual: " + userId);
			
			for (int patternId = 1; patternId <= 3; patternId++) {
				System.out.println("Padrão atual: " + patternId);
				if (option.equals(VARIANT_MAX_ERROR))
					ReplicatorScientist.replicatorVariantMaxError(userId, patternId);
				
				if (option.equals(VARIANT_UNITS))
					ReplicatorScientist.replicatorVariantUnitsTraining(userId, patternId);
				
				if (option.equals(VARIANT_NUM_EXAMPLES))
					ReplicatorScientist.replicatorVariantNumExamples(userId, patternId);
				
				if (option.equals(ALL_VARIANTS)) {
					ReplicatorScientist.replicatorVariantMaxError(userId, patternId);
					ReplicatorScientist.replicatorVariantUnitsTraining(userId, patternId);
					ReplicatorScientist.replicatorVariantNumExamples(userId, patternId);
				}	
			}
		}
	}
	
	/**
	 * Makes all the experiments with all the users and all the patterns on the DB.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void replicatorExperiments() throws ClassNotFoundException, SQLException, IOException {
		SQLiteConnectionSource connection = new SQLiteConnectionSource(SQLiteConnectionSource.DEFAULT_DB_PATH);
		UserDao userDao = new UserDao(connection.getConnectionSource());
		int [] exceptions = {1};
		List<User> users = userDao.getUsersExcept(exceptions);
		
		for (int i = 0; i < users.size(); i++) {
			int userId = users.get(i).getId();
			System.out.println("Usuário atual: " + userId);
			
			for (int patternId = 1; patternId <= 3; patternId++) {
				System.out.println("Padrão atual: " + patternId);
				ReplicatorScientist.replicatorVariantMaxError(userId, patternId);
				ReplicatorScientist.replicatorVariantUnitsTraining(userId, patternId);
				ReplicatorScientist.replicatorVariantNumExamples(userId, patternId);
				ReplicatorScientist.replicatorVariantCentralUnitsTraining(userId, patternId);
				ReplicatorScientist.ultimateTest(patternId);
			}
		}
	}
	
	/**
	 * Makes the ultimate test where the user 15 (with more samples) is used
	 * on the training of the networks.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void ultimateExperiment() throws SQLException, ClassNotFoundException, IOException {
		for (int patternId = 1; patternId <= 3; patternId++) {
			System.out.println("Padrão atual: " + patternId);
			ReplicatorScientist.ultimateTest(patternId);
		}
	}
	
	public static void main(String[] args) {
		System.setProperty(LocalLog.LOCAL_LOG_FILE_PROPERTY, "queries.log"); // Sets the log file name.
		try {
//			startExperiments();
			replicatorExperiments(args[0]);
//			ultimateExperiment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

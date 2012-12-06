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

package org.unioeste.ilp.network.db;

import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Class that holds the database connection.
 * Connects to SQLite on the file on the directory db named ilp.db.
 * 
 * The ConnectionSource is used by the ORMLite to make the queries on the database.
 * 
 * @author Lucas André de Alencar
 *
 */
public class SQLiteConnectionSource {

	private static final String driverName = "org.sqlite.JDBC";
	
	public static final String DEFAULT_DB_PATH = "db/ilp.db";
	
	private ConnectionSource connection;
	
	public SQLiteConnectionSource(String path) throws SQLException, ClassNotFoundException {
		connection = createConnection(path);
	}
	
	public static ConnectionSource createConnectionSource(String path) throws ClassNotFoundException, SQLException {
		return createConnection(path);
	}
	
	private static ConnectionSource createConnection(String path) throws ClassNotFoundException, SQLException {
		Class.forName(driverName);
		String url = "jdbc:sqlite:" + path.trim();
		ConnectionSource connection = new JdbcConnectionSource(url);
		return connection;
	}
	
	public ConnectionSource getConnectionSource() {
		return connection;
	}
	
	public void closeConnection() throws SQLException {
		connection.close();
	}
}

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

package org.unioeste.ilp.network.models;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "experiences")
public class Experience {

	@DatabaseField(id = true)
	private int _id;
	
	@DatabaseField
	private boolean done;
	
	@DatabaseField(foreign = true)
	private User user;
	
	@DatabaseField(foreign = true)
	private Pattern pattern;
	
	@ForeignCollectionField
	private ForeignCollection<Attempt> attempts;
	
	public Experience() {}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getId() {
		return _id;
	}

	public User getUser(Dao<User, Integer> userDao) throws SQLException {
		userDao.refresh(user);
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Pattern getPattern(Dao<Pattern, Integer> patternDao) throws SQLException {
		patternDao.refresh(pattern);
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public ForeignCollection<Attempt> getAttempts() {
		return attempts;
	}
}

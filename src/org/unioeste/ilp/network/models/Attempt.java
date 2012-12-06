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

@DatabaseTable(tableName = "attempts")
public class Attempt {

	@DatabaseField(id = true)
	private int _id;
	
	@DatabaseField(foreign = true)
	private Experience experience;
	
	@ForeignCollectionField
	private ForeignCollection<Sample> samples;
	
	public Attempt() {}

	public int getId() {
		return _id;
	}

	public Experience getExperience(Dao<Experience, Integer> experienceDao) throws SQLException {
		experienceDao.refresh(experience);
		return experience;
	}

	public void setExperience(Experience experience) {
		this.experience = experience;
	}

	public ForeignCollection<Sample> getSamples() {
		return samples;
	}
}

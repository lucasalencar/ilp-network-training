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

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "patterns")
public class Pattern {

	@DatabaseField(id = true)
	private int _id;
	
	@DatabaseField(canBeNull = false, unique = true)
	private String pattern_sha1;
	
	@DatabaseField(canBeNull = false)
	private String pattern_string;
	
	@ForeignCollectionField
	private ForeignCollection<Experience> experiences;
	
	public Pattern() {}

	public int getId() {
		return _id;
	}
	
	public String getPattern_sha1() {
		return pattern_sha1;
	}

	public void setPattern_sha1(String pattern_sha1) {
		this.pattern_sha1 = pattern_sha1;
	}

	public String getPattern_string() {
		return pattern_string;
	}

	public void setPattern_string(String pattern_string) {
		this.pattern_string = pattern_string;
	}

	public ForeignCollection<Experience> getExperiences() {
		return experiences;
	}
}

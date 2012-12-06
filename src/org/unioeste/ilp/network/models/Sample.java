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
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "samples")
public class Sample {

	@DatabaseField(id = true)
	private int _id;
	
	@DatabaseField(canBeNull = false)
	private double event_time;
	
	@DatabaseField(canBeNull = false)
	private double pressure;
	
	@DatabaseField(canBeNull = false)
	private double pressure_area;
	
	@DatabaseField(foreign = true)
	private Attempt attempt;
	
	public Sample() {}

	public double getEvent_time() {
		return event_time;
	}

	public void setEvent_time(double event_time) {
		this.event_time = event_time;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getPressure_area() {
		return pressure_area;
	}

	public void setPressure_area(double pressure_area) {
		this.pressure_area = pressure_area;
	}

	public int getId() {
		return _id;
	}

	public Attempt getAttempt(Dao<Attempt, Integer> attemptDao) throws SQLException {
		attemptDao.refresh(attempt);
		return attempt;
	}

	public void setAttempt(Attempt attempt) {
		this.attempt = attempt;
	}
}

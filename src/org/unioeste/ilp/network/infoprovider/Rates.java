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

package org.unioeste.ilp.network.infoprovider;

/**
 * Class that encapsulates the rates represented on the results of the tests.
 * 
 * @author Lucas André de Alencar
 *
 */
public class Rates {

	private String id;
	private double far = -1;
	private double frr = -1;
	
	private double farStdDev;
	private double frrStdDev;
	
	public Rates(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public double getFAR() {
		return far;
	}

	public void setFAR(double far) {
		this.far = far;
	}

	public double getFRR() {
		return frr;
	}

	public void setFRR(double frr) {
		this.frr = frr;
	}
	
	public double getTAR() {
		return 1 - frr;
	}
	
	public double getTRR() {
		return 1 - far;
	}
	
	public boolean isValid() {
		return id != null && far != -1 && frr != -1;
	}

	public double getFARStdDev() {
		return farStdDev;
	}

	public void setFARStdDev(double farStdDev) {
		this.farStdDev = farStdDev;
	}

	public double getFRRStdDev() {
		return frrStdDev;
	}

	public void setFRRStdDev(double frrStdDev) {
		this.frrStdDev = frrStdDev;
	}
}

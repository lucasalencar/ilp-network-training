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
 * Encapsulates basic operations with data.
 * 
 * @author Lucas André de Alencar
 *
 */
public class Calculator {

	public static double standardDeviation(double [] array, double mean) {
		double sqDiffs = 0;
        for(int i = 0; i < array.length; i++){
        	sqDiffs += (array[i] - mean) * (array[i] - mean);
        }
        return Math.sqrt(sqDiffs / array.length);
	}
	
	public static double standardDeviation(double [] array) {
		return standardDeviation(array, mean(array));
	}
	
	public static double mean(double [] array) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum / array.length;
	}
}

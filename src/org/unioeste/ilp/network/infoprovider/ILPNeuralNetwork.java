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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.unioeste.ilp.network.lab.Scientist;

/**
 * Class responsible for parsing the results files resulted from the tests
 * and organizing it to external usage.
 * 
 * Provides methods that gather all the information generated from the tests,
 * calculates the mean from all the networks tested and organizes it.
 * 
 * @author Lucas André de Alencar
 *
 */
public class ILPNeuralNetwork {
	
	public static final String VARIANT_MAX_ERROR = "replicatorVariantMaxError";
	public static final String VARIANT_NUM_EXAMPLES = "replicatorVariantNumExamples";
	public static final String VARIANT_UNITS = "replicatorVariantUnits";
	public static final String VARIANT_CENTRAL_UNITS = "replicatorVariantCentralUnits";

	private static Scanner newScanner(String training, int userId, int patternId) throws FileNotFoundException {
		String path = Scientist.initDirs(training, userId, patternId);
		Scanner scanner = new Scanner(new File(path + "/" + Scientist.TRAINING_INFO_FILE));
		return scanner;
	}
	
	/**
	 * Gets the list rates from the user, pattern and training specified.
	 * 
	 * @param training Training done
	 * @param userId User
	 * @param patternId Pattern
	 * @return List of rates
	 * @throws FileNotFoundException
	 */
	public static List<Rates> getReplicatorRates(String training, int userId, int patternId) throws FileNotFoundException {
		Scanner file = newScanner(training, userId, patternId);
		List<Rates> ratesList = new ArrayList<Rates>();
		while (file.hasNext()) {
			Rates rates = new Rates(file.next());
			String value = file.next();
			rates.setFAR(new Double(value));
			rates.setFRR(new Double(file.next()));
			ratesList.add(rates);
			System.out.println(value);
		}
		return ratesList;
	}
	
	/**
	 * Gets all the rates from all the users related with the pattern and training specified.
	 * Each element on the list is a list of rates from 1 user.
	 * 
	 * @param training Training done
	 * @param patternId Pattern
	 * @return List of lists of rates
	 * @throws FileNotFoundException
	 */
	public static List<List<Rates>> allUsersRates(String training, int patternId) throws FileNotFoundException {
		List<List<Rates>> rates = new ArrayList<List<Rates>>();
		for (int i = 2; i <= 15; i++) {
			List<Rates> rate = getReplicatorRates(training, i, patternId);
			rates.add(rate);
		}
		return rates;
	}
	
	private static Rates ratesStatistics(String id, double [] fars, double [] frrs) {
		Rates rate = new Rates(id);
		rate.setFAR(Calculator.mean(fars));
		rate.setFRR(Calculator.mean(frrs));
		rate.setFARStdDev(Calculator.standardDeviation(fars, rate.getFAR()));
		rate.setFRRStdDev(Calculator.standardDeviation(frrs, rate.getFRR()));
		return rate;
	}
	
	/**
	 * Calculates the mean and standard deviation of the FAR and FRR on the list of lists of rates.
	 * The list returned contains the mean and standard deviation of each user on the previous list.
	 * 
	 * @param rates List of lists of rates
	 * @return List of rates
	 */
	public static List<Rates> calcStatisticsFromListRates(List<List<Rates>> rates) {
		List<Rates> statistics = new ArrayList<Rates>();
		for (int i = 0; i < rates.get(0).size(); i++) {
			double [] fars = new double[rates.size()];
			double [] frrs = new double[rates.size()];
			for (int j = 0; j < rates.size(); j++) {
				fars[j] = rates.get(j).get(i).getFAR();
				frrs[j] = rates.get(j).get(i).getFRR();
			}
			Rates rate = ratesStatistics(rates.get(0).get(i).getId(), fars, frrs);
			statistics.add(rate);
		}
		return statistics;
	}
	
	/**
	 * Gets the mean rates of each user related with the pattern and the training done.
	 * 
	 * @param training Training done
	 * @param patternId Pattern
	 * @return List of rates
	 * @throws FileNotFoundException
	 */
	public static List<Rates> meanRatesThroughPattern(String training, int patternId) throws FileNotFoundException {
		return calcStatisticsFromListRates(allUsersRates(training, patternId));
	}
	
	/**
	 * Gets the mean rates with all the users and patterns related with the training.
	 * Each element on the list is a pattern where has a list of users with their mean rates.
	 * 
	 * @param training Training
	 * @return List of lists of rates
	 * @throws FileNotFoundException
	 */
	public static List<List<Rates>> allPatternsMeanRates(String training) throws FileNotFoundException {
		List<List<Rates>> rates = new ArrayList<List<Rates>>();
		for (int i = 1; i <= 3; i++) {
			List<Rates> rate = meanRatesThroughPattern(training, i);
			rates.add(rate);
		}
		return rates;
	}
	
	/**
	 * Gets the mean rates from each pattern related with the training.
	 * Each element on the list is a pattern that has the mean rates
	 * from each user related with the pattern.
	 * 
	 * @param training Training
	 * @return List of rates
	 * @throws FileNotFoundException
	 */
	public static List<Rates> meanRatesAllPatterns(String training) throws FileNotFoundException {
		return calcStatisticsFromListRates(allPatternsMeanRates(training));
	}
	
	/**
	 * Shows the rates on the list.
	 * 
	 * @param ratesList List of rates
	 */
	public static void showList(List<Rates> ratesList) {
		for (int i = 0; i < ratesList.size(); i++) {
			System.out.println(ratesList.get(i).getId() + " " + ratesList.get(i).getFAR() + " " + ratesList.get(i).getFRR() + " " + ratesList.get(i).getFARStdDev() + " " + ratesList.get(i).getFRRStdDev());
		}
	}
}

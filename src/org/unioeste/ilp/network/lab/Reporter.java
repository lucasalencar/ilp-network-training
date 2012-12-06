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

package org.unioeste.ilp.network.lab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.unioeste.ilp.network.util.OutputHandler;

/**
 * Class that holds all the information obtained on the tests
 * to save later on a specified file.
 * 
 * All the information is specific to the trainings, calculating
 * the FAR and FRR of each test made. Each row on the report
 * is a test made with a specific network.
 * 
 * This class is managed on the classes of experiments.
 * 
 * @author Lucas André de Alencar
 *
 */
public class Reporter {

	private List<String> report;
	
	private double far = -1;
	private double frr = -1;
	private String network = null;
	
	public Reporter() {
		report = new ArrayList<String>();
	}
	
	public boolean hasPreviousReport() {
		return far >= 0 && frr >= 0 && network != null;
	}
	
	public boolean isActualReportIncomplete() {
		return far < 0 || frr < 0 || network == null;
	}
	
	public boolean isActualReportComplete() {
		return !isActualReportIncomplete();
	}
	
	public void commitReport() {
		if (isActualReportComplete()) {
			report.add(network + "\t" + String.format(Locale.US, "%.20f", far) + "\t" + String.format(Locale.US, "%.20f", frr));
			clearActualReport();
		} else
			throw new IllegalStateException("Your actual report is incomplete. Missing" + getMissing() + ".");
	}
	
	public void clearActualReport() {
		network = null;
		far = -1;
		frr = -1;
	}
	
	private String getMissing() {
		String missing = "";
		if (far < 0) missing = missing.concat(" far");
		if (frr < 0) missing = missing.concat(" frr");
		if (network == null) missing = missing.concat(" network");
		return missing;
	}
	
	public void report(String network, int fa, int fr, int size) {
		if (hasPreviousReport()) commitReport();
		this.network = network;
		far = calculateFAR(fa, size);
		frr = calculateFRR(fr, size);
	}
	
	public void report(int fa, int negativeSize, int fr, int positiveSize) {
		if (hasPreviousReport()) commitReport();
		far = calculateFAR(fa, negativeSize);
		frr = calculateFRR(fr, positiveSize);
	}
	
	public void report(String network) {
		if (hasPreviousReport()) commitReport();
		this.network = network;
	}
	
	public double calculateFAR(int fa, int size) {
		return (double)(fa) / (double)(size);
	}
	
	public double calculateFRR(int fr, int size) {
		return (double)(fr) / (double)(size);
	}
	
	public void writeReport(String path) throws IOException {
		if (!hasPreviousReport()) {
//			int reportSize = report.size();
//			report.add("");
//			report.add("Report line: " + reportSize);
			OutputHandler.writeOutput(path, report);
		} else throw new IllegalStateException("There's reports to commit. Before writing report, commit last changes.");
	}
}

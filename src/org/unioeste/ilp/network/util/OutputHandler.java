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

package org.unioeste.ilp.network.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataPair;

/**
 * Encapsulates commom methods used on manipulations of files.
 * 
 * @author Lucas André de Alencar
 *
 */
public class OutputHandler {

	/**
	 * Writes the list of outputs made by the training on a specified file.
	 * 
	 * @param outputFile File
	 * @param numInputs Number of inputs
	 * @param numOutputs Number of outputs
	 * @param hiddenLayer Hidden Layers
	 * @param trainOutputs List of strings
	 * @throws IOException
	 */
	public static void writeOutput(File outputFile, int numInputs, int numOutputs, int [] hiddenLayer, List<String> trainOutputs) throws IOException {
		List<String> output = new ArrayList<String>();
		output.add(networkToString(numInputs, hiddenLayer, numOutputs));
		output.addAll(trainOutputs);
		writeFile(outputFile, output);
	}
	
	/**
	 * Writes the list of outputs made by the training on a specified file.
	 * 
	 * @param outputFile File
	 * @param lines List of strings
	 * @throws IOException
	 */
	public static void writeOutput(File outputFile, List<String> lines) throws IOException {
		writeFile(outputFile, lines);
	}
	
	/**
	 * Writes the list of strings on a file named as passed on parameter.
	 * 
	 * @param fileName File name
	 * @param trainOutputs List of strings
	 * @throws IOException
	 */
	public static void writeOutput(String fileName, List<String> trainOutputs) throws IOException {
		writeFile(new File(fileName), trainOutputs);
	}
	
	/**
	 * Creates and opens the file specified and writes the list of strings output.
	 * 
	 * @param outputFile File
	 * @param output List of strings
	 * @throws IOException
	 */
	public static void writeFile(File outputFile, List<String> output) throws IOException {
		FileWriter file = new FileWriter(outputFile);
		for (int j = 0; j < output.size(); j++) {
			file.write(output.get(j) + "\n");
		}
		file.close();
	}
	
	/**
	 * Converts a network structure on a string representation.
	 * 
	 * @param numInputs Number of inputs
	 * @param hiddenLayer Hidden layers
	 * @param numOutputs Number of outputs
	 * @return String
	 */
	public static String networkToString(int numInputs, int [] hiddenLayer, int numOutputs) {
		return numInputs + "-" + hiddenLayerToString(hiddenLayer) + "-" + numOutputs;
	}
	
	/**
	 * Converts the hidden layers array representation toa string representation.
	 * 
	 * @param hiddenLayer Array of layers.
	 * @return String
	 */
	public static String hiddenLayerToString(int [] hiddenLayer) {
		String s = "";
		for (int i = 0; i < hiddenLayer.length - 1; i++) {
			s = s.concat(hiddenLayer[i] + "-");
		}
		return s + hiddenLayer[hiddenLayer.length - 1];
	}
	
	/**
	 * Converts the samples on a dataset to a list of strings.
	 * 
	 * @param dataset MLDataSet
	 * @return List of strings
	 */
	public static List<String> outputSample(MLDataSet dataset) {
		MLDataPair pair = BasicMLDataPair.createPair(dataset.getInputSize(), dataset.getIdealSize());
		List<String> samples = new ArrayList<String>();
		for (int i = 0; i < dataset.getRecordCount(); i++) {
			dataset.getRecord(i, pair);
			samples.add(createInputSampleLine(pair.getInputArray()));
			System.out.println(samples.get(i));
		}
		return samples;
	}
	
	/**
	 * Writes the samples on a dataset to a file and returns what was written.
	 * 
	 * @param dataset MLDataSet
	 * @param outputFile File
	 * @return List of strings
	 * @throws IOException
	 */
	public static List<String> outputSample(MLDataSet dataset, File outputFile) throws IOException {
		List<String> samples = outputSample(dataset);
		writeOutput(outputFile, samples);
		return samples;
	}
	
	private static String createInputSampleLine(double[] inputArray) {
		String line = "" + inputArray[0];
		for (int i = 1; i < inputArray.length; i++) {
			line = line.concat("\t" + inputArray[i]);
		}
		return line;
	}
	
	/**
	 * Create all the directories that doesn't exist specified on the string.
	 * 
	 * @param dir String
	 * @return Ok?
	 */
	public static boolean createDir(String dir) {
		return (new File(dir)).mkdirs();
	}
	
	/**
	 * Builds the string with the directories on the array and creates them on filesystem.
	 * 
	 * @param dirs Array of strings
	 * @return String of path
	 */
	public static String mountDir(String[] dirs) {
		String path = "./" + dirs[0];
		for (int i = 1; i < dirs.length; i++) {
			path = path.concat("/" + dirs[i]);
		}
		createDir(path);
		return path;
	}
	
	/**
	 * Separates the path on an array of strings.
	 * 
	 * @param dirs String path
	 * @return Array of strings.
	 */
	public static String[] separateDirs(String dirs) {
		return dirs.split("/");
	}
	
}

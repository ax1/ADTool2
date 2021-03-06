package ax1.simulators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import ax1.variables.Complex;
import lu.uni.adtool.ADToolMain;
import lu.uni.adtool.domains.ValuationDomain;
import lu.uni.adtool.domains.ValueAssignement;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tree.ADTNode;
import lu.uni.adtool.ui.canvas.AbstractDomainCanvas;

/**
 * Simulate changes in values for attack trees and perform calculations up to
 * the root node. The simulation is performed in front-end to allow all the
 * existing events to be trigered.s
 * 
 * Usage: select an attack tree and then go to menu/file/SIMULATE5
 * 
 * Note: the tree must be filled before simulating
 * 
 * TYPES: - default: calculates the root risk depending on countermeasures. - p,
 * i, or c, add that char on each node to be simulated (p=prob, i=impact,
 * c=cost)
 */
public class Simulator {

	public static ADToolMain main;

	public static void simulate() {
		Map<String, Complex> candidates = new HashMap<>();
		Map<Complex, String> backup = new HashMap<>();
		AbstractDomainCanvas<Ring> canvas = null;
		List<String> results = new ArrayList<>();
		try {
			log("-----SIMULATION STARTED!-----");
			// ---------------- PREPARE SIMULATION (variables)----------------
			canvas = (AbstractDomainCanvas<Ring>) main.getController().getLastFocusedTree();
			ValuationDomain valuation = canvas.getValues();
			ValueAssignement<Ring> map = valuation.getValueMap();

			ADTNode root = (ADTNode) canvas.getTree().getRoot(true);
			Complex complexRoot = (Complex) valuation.getTermValue(root);
			if (complexRoot == null)
				throw new Exception("Tree is still incomplete, root node must have values");

			Set<SimulationType> types = new TreeSet<>(new MyComparator());
			putNodes(true, map, candidates, types, backup); // find attack nodes
			putNodes(false, map, candidates, types, backup); // find countermeasures nodes
			if (types.size() != 1) {
				// throw new Exception("The simulation algorithm must have EXACTLY ONE type, and
				// found " + types.size());
				log("No simulation type found. Using the default simulation (countermeasures activation)");
				simulateCountermeasures(canvas, map, candidates, results, backup, root);
				if (results.size() > 100) {
					log("results size is  big to display. Created a file 'simulation_results.txt' in the project root with the content instead.");
					saveToFile(results);
				} else {
					for (String s : results) {
						log(s);
						System.out.println(s);
					}
				}
				log("-----SIMULATION FINISHED------");
				return;
			}

			// ----------------START SIMULATION-----------------------------------------
			SimulationType type = types.iterator().next();
			results.add(type.description);

			String header = "";
			for (String key : candidates.keySet())
				header = header + key + "|";
			header = header + root.getName();
			header = header.replace(" ", "_").replace("|", " ");
			results.add(header);
			log(header);

			double value = 0;
			for (int r = 0; r <= 20; r++) {
				String line = "";

				for (String key : candidates.keySet()) {
					Complex complex = candidates.get(key);
					if (complex.toString().split(" ").length < 3)
						continue;// this node cannot be calculated (either empty or bad data)
					double[] v = complex.toVector();
					v[type.index] = value;
					Complex temp = new Complex(Complex.f(v[0]) + " " + Complex.f(v[1]) + " " + Complex.f(v[2]));
					complex.updateFromString(temp.toString());
					line = line + Complex.f(value) + " ";
					canvas.valuesUpdated(false); // This is better than valuation.refreshAllValues(root)
				}

				complexRoot = (Complex) valuation.getTermValue(root);
				line = line + complexRoot.toString();
				results.add(line);
				log(line);
				value = value + type.tick;
			}
			log("-----SIMULATION FINISHED------");
		} catch (Exception e) {
			logError(e.getMessage());
			e.printStackTrace();
			log("-----SIMULATION FAILED!-----");
		} finally {
			// ----------------RESTATE TREE-----------------------------------------
			if (canvas != null) {
				for (Entry<Complex, String> entry : backup.entrySet()) {
					entry.getKey().updateFromString(entry.getValue());
				}
				canvas.valuesUpdated(false);
			}
		}
	}

	/**
	 * Add relevant nodes for simulation
	 */
	private static void putNodes(boolean isProponent, ValueAssignement<Ring> map, Map<String, Complex> candidates,
			Set<SimulationType> types, Map<Complex, String> backup) {
		Set<String> keys = map.keySet(isProponent);
		for (String key : keys) {
			Complex complex = (Complex) map.get(isProponent, key);
			SimulationType type = new SimulationType(complex.toString().toLowerCase());
			if (type.type != null) {
				types.add(type);
				candidates.put(key, complex);
				backup.put(complex, complex.toString());
			}
		}
	}

	private static String getSimulationType(Complex complex) {
		String text = complex.toString().toLowerCase();
		return text.contains("p") ? "p" : text.contains("i") ? "i" : text.contains("c") ? "c" : null;
	}

	public static void log(String message) {
		main.getController().report(message);
	}

	public static void logError(String message) {
		main.getController().reportError(message);
	}

	/**
	 * Get all CM and simulate the risk at ROOT when enabling and disabling some of
	 * them
	 */
	private static void simulateCountermeasures(AbstractDomainCanvas<Ring> canvas, ValueAssignement<Ring> map,
			Map<String, Complex> candidates, List<String> results, Map<Complex, String> backup, ADTNode root) {
		// get all editables CMs
		boolean isProponent = false;
		Set<String> keys = map.keySet(isProponent);
		List<Complex> values = new ArrayList<>();
		List<Double> backupProbabilities = new ArrayList<>();

		// get header line
		String header = "";
		for (String key : keys)
			header = header + key + "|";
		header = header + root.getName();
		header = header.replace(" ", "_").replace("|", " ");
		results.add(header);

		// add all editable CM as candidates
		for (String key : keys) {
			Complex complex = (Complex) map.get(isProponent, key);
			candidates.put(key, complex);
			values.add(complex);
			backup.put(complex, complex.toString());
			String original = complex.toString().contentEquals("") ? "0 0 0" : complex.toString();
			double[] v = Complex.toVector(original);
			backupProbabilities.add(v[0]);
		}

		// the list of branches equals from 0 - 2^n
		int TOTAL = 1 << keys.size();

		// foreach iteration
		for (int r = 0; r < TOTAL; r++) {
			// get array [00110011...]
			String temp = "";
			String b = Integer.toBinaryString(r);
			for (int s = 0; s < keys.size() - b.length(); s++)
				temp = temp + "0";
			temp = temp + b;
			String[] sarr = temp.split("");
			String line = "";
			for (int s = 0; s < sarr.length; s++) {
				// activate or deactivate the countermeasure
				boolean activate = sarr[s].equals("1") ? true : false;
				line = line + sarr[s] + " ";
				// toggle the countermeasure value and refresh tree sequentially
				Complex c = values.get(s);
				double[] v = new double[] { 0, 10, 10 }; // and not 0,0,0 because we want 0,10,10(inactive) or
															// 1,10,10(active full)
				if (c.toString().equals("") == false)
					v = c.toVector();
				v[0] = activate ? backupProbabilities.get(s) : 0; // deactivated if p=0, activated if p=p_original
				Complex tempC = new Complex(Complex.f(v[0]) + " " + Complex.f(v[1]) + " " + Complex.f(v[2]));
				c.updateFromString(tempC.toString());
				canvas.valuesUpdated(false);
			}
			Complex complexRoot = (Complex) canvas.getValues().getTermValue(root);
			line = line + complexRoot.toString();
			results.add(line);
		}

	}

//    private static void saveToFile(List<String> results) {
//        try {
//        	for(String line:results)
//  
//            Files.write(Paths.get("simulation.txt"), line.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

	private static void saveToFile(List<String> results) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File("simulation_results.txt"));
			for (String line : results) {
				line = line + "\n";
				os.write(line.getBytes(), 0, line.length());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class MyComparator implements Comparator<SimulationType> {

		@Override
		public int compare(SimulationType t1, SimulationType t2) {

			return t1.type.compareTo(t2.type);
		}
	}

	public static class SimulationType {
		public final String type; // probability, cost or impact
		public final int index; // index for retrieving the value in the complex vector
		public final double tick; // what is the increment value to be applied
		public final String description;

		public SimulationType(String text) {
			type = text.contains("p") ? "p" : text.contains("i") ? "i" : text.contains("c") ? "c" : null;
			index = text.contains("p") ? 0 : text.contains("i") ? 1 : text.contains("c") ? 2 : -1;
			tick = (type != null && type.equals("p")) ? 0.05 : 0.5; // 1/100th to get100 points for graphs
			description = "Simulation type: " + (text.contains("p") ? "probability"
					: text.contains("i") ? "impact" : text.contains("c") ? "cost" : "<undefined>");
		}
	}
}
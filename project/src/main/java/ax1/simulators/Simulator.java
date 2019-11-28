package ax1.simulators;

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
 * existing events to be trigered.
 * 
 * Usage: select an attack tree and then go to menu/file/SIMULATE
 */
public class Simulator {

	public static ADToolMain main;

	public static void simulate() {
		Map<String, Complex> candidates = new HashMap<>();
		Map<Complex, String> backup = new HashMap<>();
		AbstractDomainCanvas<Ring> canvas = null;
		try {
			log("-----SIMULATION STARTED!-----");
			// ---------------- PREPARE SIMULATION (variables)----------------
			canvas = (AbstractDomainCanvas<Ring>) main.getController().getLastFocusedTree();
			ValuationDomain valuation = canvas.getValues();
			ValueAssignement<Ring> map = valuation.getValueMap();

			Set<SimulationType> types = new TreeSet<>(new MyComparator());
			putNodes(true, map, candidates, types, backup); // find attack nodes
			putNodes(false, map, candidates, types, backup); // find countermeasures nodes
			if (types.size() != 1) {
				throw new Exception("The simulation algorithm must have EXACTLY ONE type, and found " + types.size());
			}

			// ----------------START SIMULATION-----------------------------------------
			List<String> results = new ArrayList<>();
			ADTNode root = (ADTNode) canvas.getTree().getRoot(true);
			Complex complexRoot = (Complex) valuation.getTermValue(root);
			if (complexRoot == null)
				throw new Exception("Tree is still incomplete, root node must have values");

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
			for (int r = 0; r <= 10; r++) {
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
			tick = (type != null && type.equals("p")) ? 0.1 : 1;
			description = "Simulation type: " + (text.contains("p") ? "probability"
					: text.contains("i") ? "impact" : text.contains("c") ? "cost" : "<undefined>");
		}
	}
}
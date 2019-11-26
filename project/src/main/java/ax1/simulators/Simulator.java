package ax1.simulators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ax1.variables.Complex;
import lu.uni.adtool.ADToolMain;
import lu.uni.adtool.domains.ValuationDomain;
import lu.uni.adtool.domains.ValueAssignement;
import lu.uni.adtool.domains.rings.Ring;
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
		log("-----SIMULATION STARTED!-----");
		Map<String, Complex> candidates = new HashMap<>();
		// ---------------- PREPARE SIMULATION (variables)----------------
		AbstractDomainCanvas<Ring> canvas = (AbstractDomainCanvas<Ring>) main.getController().getLastFocusedTree();
		ValuationDomain valuation = canvas.getValues();
		ValueAssignement<Ring> map = canvas.getValues().getValueMap();
		Set<String> opponents = map.keySet(false);
		for (String key : opponents) {
			Complex complex = (Complex) map.get(false, key);
			if (complex.toString().contains("*")) {
				candidates.put(key, complex);
				log("candidate: " + key);
			}
		}

		// ----------------START SIMULATION-----------------------------------------
		// Node root = canvas.getTree().getRoot(true);
		for (String key : candidates.keySet()) {
			// do whatever
			// complex.updateFromString("0.1 1 1 *"); // DEMO, change a value in background
			// recalculate tre
			canvas.valuesUpdated(false); // This is better than valuation.refreshAllValues(root)
		}

		log("-----SIMULATION FINISHED------");
	}

	public static void log(String message) {
		main.getController().report(message);
	}

}

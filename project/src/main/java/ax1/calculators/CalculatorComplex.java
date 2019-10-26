package ax1.calculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ax1.variables.Complex;
import lu.uni.adtool.domains.ValueAssignement;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tree.ADTNode;
import lu.uni.adtool.tree.Node;

public abstract class CalculatorComplex implements ICalculatorComplex {

	public Complex calc(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map) {
		try {
			ADTNode.Type type = node.getType();
			switch (type) {
			case OR_OPP:
				return or_opp(a, b, node, values, map);
			case AND_OPP:
				return and_opp(a, b, node, values, map);
			case OR_PRO:
				return or_pro(a, b, node, values, map);
			case AND_PRO:
				return and_pro(a, b, node, values, map);
			default:
				throw new Exception("@@@@DEVELOPER ERROR@@@@:  not implemented");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return new Complex("");
		}
	}

	/**
	 * Get complex value as array[a,b,c...] for all child nodesthat are not
	 * countermeasures
	 * 
	 * @param node        the node to get childs data
	 * @param values      the values at this stage of some editable nodes,
	 *                    categorized in proponent or opponent types
	 * @param map         the list of values for all the nodes (editable or
	 *                    calculated) that have been processed for now
	 * @param isProponent if node is proponent or opponent
	 * @return list of child values that are same type of parent
	 *         (isProponent?proponentChilds:opponentChilds)
	 */
	private static List<double[]> getVectors(ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map,
			boolean isProponent) {
		List<double[]> vectors = new ArrayList<>();
		List<Node> childs = node.getNotNullChildren();
		for (int r = 0; r < childs.size(); r++) {
			ADTNode child = (ADTNode) childs.get(r);
			if (!child.isCountered()) {
				Complex complex = (Complex) map.get(child);
				vectors.add(complex.toVector());
			}
		}
		return vectors;
	}

}

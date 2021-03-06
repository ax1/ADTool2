package ax1.calculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ax1.variables.Complex;
import lu.uni.adtool.domains.ValueAssignement;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tree.ADTNode;
import lu.uni.adtool.tree.ADTNode.Role;
import lu.uni.adtool.tree.Node;

public class CalculatorWorstCase extends CalculatorComplex {

	public Complex and_pro(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map) {
		List<double[]> vectors = getVectors(node, values, true, map);
		return calcAND(vectors);
	}

	public Complex or_pro(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map) {
		return calcOR(a, b);
	}

	public Complex and_opp(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map) {
		List<double[]> vectors = getVectors(node, values, false, map);
		return calcAND(vectors);
	}

	public Complex or_opp(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map) {
		return calcOR(a, b);
	}

	public Complex counter_opp(Complex a, Complex b) {
		try {
			double[] v1 = a.toVector();
			double[] v2;
			try {
				v2 = b.toVector();
			} catch (Exception e) {
				v2 = new double[3];
			}
			// if counter probability = 0 the propagated complex should be the node itself
			if (v2[0] == 0) {
				return new Complex(
						Double.toString(v1[0]) + " " + Double.toString(v1[1]) + " " + Double.toString(v1[2]));
			} else {
				double prob = v1[0] * (1 - v2[0]);
				double impact = v1[1] * v2[1] / 10d;
				double cost = v1[2]; // the cost of the box should not vary
				return new Complex(Double.toString(prob) + " " + Double.toString(impact) + " " + Double.toString(cost));
			}
		} catch (Exception e) {
			return new Complex("");
		}
	}

	public Complex counter_pro(Complex a, Complex b) {
		try {
			double[] v1 = a.toVector();
			double[] v2;
			try {
				v2 = b.toVector();
			} catch (Exception e) {
				v2 = new double[3];
			}
			// if counter probability = 0 the propagated complex should be the node itself
			if (v2[0] == 0) {
				return new Complex(
						Double.toString(v1[0]) + " " + Double.toString(v1[1]) + " " + Double.toString(v1[2]));
			} else {
				double prob = v1[0] * (1 - v2[0]);
				double impact = v1[1] * v2[1] / 10d;
				double cost = v1[2]; // The resulting cost is always from the viewer point of view (attacker
										// view->cost of attacker)
				return new Complex(Double.toString(prob) + " " + Double.toString(impact) + " " + Double.toString(cost));
			}
		} catch (Exception e) {
			return new Complex("");
		}
	}

	/**
	 * Return [probs,impacts,costs] as Complex variable
	 **/
	private static Complex calcAND(List<double[]> vectors) {
		double A = A(vectors);
		double B = B(vectors);
		double C = C(vectors);
		String text = Double.toString(A) + " " + Double.toString(B) + " " + Double.toString(C);
		return new Complex(text);
	}

//	/**
//	 * Return the Complex one with the higher risk
//	 */
//	private static Complex calcOR(Complex a, Complex b) {
//		double[] v1 = a.toVector();
//		double[] v2 = b.toVector();
//		double risk1 = v1[0] * v1[1] / v1[2];
//		double risk2 = v2[0] * v2[1] / v2[2];
//		return risk1 > risk2 ? (Complex) a.clone() : (Complex) b.clone();
//	}

	/*
	 * Return the Complex one with the higher risk, if risks are equal, return the
	 * Complex one with higher A=probability, and if A are equal too, return the one
	 * with higher B=impact. and if A and B are equal too, C= cost is equal too,
	 * return the first Complex one.
	 */
	private static Complex calcOR(Complex a, Complex b) {
		double[] v1 = a.toVector();
		double[] v2 = b.toVector();
		double risk1 = v1[0] * v1[1] / v1[2];
		double risk2 = v2[0] * v2[1] / v2[2];
		if (risk1 == risk2) {
			if (v1[0] == v2[0]) {
				return v1[1] > v2[1] ? (Complex) a.clone() : (Complex) b.clone();
			} // return the one with higher B
			return v1[0] > v2[0] ? (Complex) a.clone() : (Complex) b.clone(); // return the one with higher A
		}
		return risk1 > risk2 ? (Complex) a.clone() : (Complex) b.clone();
	}

	private static double A(List<double[]> vectors) {
		double res = 1;
		for (double[] vector : vectors)
			res = res * vector[0];
		return res;
	}

	private static double B(List<double[]> vectors) {
		int n = vectors.size();
		double res = 1;
		for (double[] vector : vectors)
			res = res * (10 - vector[1]);
		res = (Math.pow(10, n) - res) / Math.pow(10, n - 1);
		return res;
	}

	private static double C(List<double[]> vectors) {
		double res = 0;
		for (double[] vector : vectors)
			res = res + vector[2];
		return res;
	}

	private static List<double[]> getVectors(ADTNode node, ValueAssignement<Ring> values, boolean isProponent,
			HashMap<Node, Ring> map) {
		List<double[]> vectors = new ArrayList<>();
		List<Node> childs = node.getNotNullChildren();
		// remove childs with different role than parent because they will be calculated
		// as counter_opp or counter_prop later
		List<ADTNode> realChilds = new ArrayList<>();
		Role role = isProponent ? Role.PROPONENT : Role.OPPONENT;
		for (Node n : childs) {
			ADTNode child = (ADTNode) n;
			if (child.getRole().equals(role))
				realChilds.add(child);
		}

		// get vector for all valid child nodes
		for (ADTNode child : realChilds) {
			Complex complex = getDerivedComplex(child, values, isProponent, map);
			vectors.add(complex.toVector());
		}
		return vectors;
	}

	/**
	 * Get the complex value given a node. This can be in different places depending
	 * if related nodes are filled, the type, etc Please note that some nodes can
	 * have two complex(nodes with countermeasures. In this case, the merged complex
	 * is retrieved
	 * 
	 * @param node
	 * @param values
	 * @param isProponent
	 * @param map
	 */
	private static Complex getDerivedComplex(ADTNode node, ValueAssignement<Ring> values, boolean isProponent,
			HashMap<Node, Ring> map) {
		Complex complex = (Complex) map.get(node);
		if (complex != null && complex.toString().equals("") == false) {
			return complex;
		} else {
			complex = (Complex) values.get(isProponent, node.getName());
			if (complex == null)
				complex = new Complex();
		}
		return complex;
	}

}

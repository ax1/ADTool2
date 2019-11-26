package ax1.calculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ax1.variables.Complex;
import lu.uni.adtool.domains.ValueAssignement;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tree.ADTNode;
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
			double[] v2 = b.toVector();
			double prob = v1[0] * (1 - v2[0]);
			double impact = v1[1] * v2[1] / 10d;
			double cost = v1[2] < v2[2] ? v1[2] : v2[2];
			String text = Double.toString(prob) + " " + Double.toString(impact) + " " + Double.toString(cost);
			return new Complex(text);
		} catch (Exception e) {
			return new Complex("");
		}

	}

	public Complex counter_pro(Complex a, Complex b) {
		try {
			double[] v1 = a.toVector();
			double[] v2 = b.toVector();
			double prob = v1[0] * (1 - v2[0]);
			double impact = v1[1] * v2[1] / 10d;
			double cost = v1[2];
			String text = Double.toString(prob) + " " + Double.toString(impact) + " " + Double.toString(cost);
			return new Complex(text);
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

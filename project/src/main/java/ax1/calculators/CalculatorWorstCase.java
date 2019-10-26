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
		// TODO ARF: if countermeasure!="" the oponent blocked the proponent so result
		// is default value
		return new Complex("");
	}

	public Complex counter_pro(Complex a, Complex b) {
		// TODO ARF: if countermeasure!="" the proponent anulates the opponent so result
		// is proponent values
		// TODO ARF: esto hay que mirarlo porque el otro puede ser logico pero este no
		// tanto porque cual de a b es la contramedida?
		return (Complex) a.clone();
	}

	/**
	 * Return [probs, impacts,costs] as complex variable
	 **/
	private static Complex calcAND(List<double[]> vectors) {
		double A = A(vectors);
		double B = B(vectors);
		double C = C(vectors);
		String text = Double.toString(A) + ", " + Double.toString(B) + ", " + Double.toString(C);
		return new Complex(text);
	}

	/**
	 * Return the Complex one with the higher risk
	 */
	private static Complex calcOR(Complex a, Complex b) {
		double[] v1 = a.toVector();
		double[] v2 = b.toVector();
		double risk1 = v1[0] * v1[1] / v1[2];
		double risk2 = v2[0] * v2[1] / v2[2];
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
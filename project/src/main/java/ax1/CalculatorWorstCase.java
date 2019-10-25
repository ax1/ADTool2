package ax1;

import java.util.ArrayList;
import java.util.List;

import lu.uni.adtool.domains.ValueAssignement;
import lu.uni.adtool.domains.rings.Complex;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tree.ADTNode;
import lu.uni.adtool.tree.Node;

public class CalculatorWorstCase {

	public static Complex calc(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values) {
		try {
			ADTNode.Type type = node.getType();
			if (type == type.AND_OPP || type == type.AND_PRO) {
				boolean isProponent = type == type.AND_PRO ? true : false;
				return calcAND(a, b, node, values, isProponent);
			} else {
				return calcOR(a, b, node);
			}
		} catch (Exception e) {
			return new Complex("");
		}
	}

	private static Complex calcAND(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values,
			boolean isProponent) {
		List<double[]> vectors = getVectors(node, values, isProponent);
		double A = A(vectors);
		double B = B(vectors);
		double C = C(vectors);
		String text = Double.toString(A) + ", " + Double.toString(B) + ", " + Double.toString(C);
		return new Complex(text);
	}

	/**
	 * Return the one with the higher risk
	 * 
	 * @param a
	 * @param b
	 * @param node
	 * @return
	 */
	private static Complex calcOR(Complex a, Complex b, ADTNode node) {
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

	private static List<double[]> getVectors(ADTNode node, ValueAssignement<Ring> values, boolean isProponent) {
		List<double[]> vectors = new ArrayList<>();
		List<Node> childs = node.getNotNullChildren();
		for (int r = 0; r < childs.size(); r++) {
			ADTNode child = (ADTNode) childs.get(r);
			Complex complex = (Complex) values.get(isProponent, child.getName());
			vectors.add(complex.toVector());
		}
		return vectors;
	}

}

package ax1;

import lu.uni.adtool.domains.rings.Complex;
import lu.uni.adtool.tree.ADTNode;

public class CalculatorWorstCase {

	public static Complex calc(Complex a, Complex b, ADTNode node) {
		try {
			ADTNode.Type type = node.getType();
			if (type == type.AND_OPP || type == type.AND_PRO) {
				return calcAND(a, b, node);
			} else {
				return calcOR(a, b, node);
			}
		} catch (Exception e) {
			return new Complex("");
		}
	}

	private static Complex calcAND(Complex a, Complex b, ADTNode node) {
		return (Complex) a.clone();
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

}

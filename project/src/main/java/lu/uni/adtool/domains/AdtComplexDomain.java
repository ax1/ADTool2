package lu.uni.adtool.domains;

import lu.uni.adtool.domains.rings.Complex;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tree.ADTNode;

public interface AdtComplexDomain<Type extends Ring> {
	/**
	 * Allow calculations by using information from node structure, related, etc.
	 * This allow for multipurpose calculations.
	 * 
	 * Note, the node parameter should be treated as read-only exclusively. To
	 * modify node data use the existing visual features.
	 * 
	 * @param a
	 * @param b
	 * @param type
	 * @return
	 */
	public Complex calc(Complex a, Complex b, ADTNode node, ValueAssignement<Type> values);
}

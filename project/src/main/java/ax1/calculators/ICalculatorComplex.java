package ax1.calculators;

import java.util.HashMap;

import ax1.variables.Complex;
import lu.uni.adtool.domains.ValueAssignement;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tree.ADTNode;
import lu.uni.adtool.tree.Node;

public interface ICalculatorComplex {

	public Complex calc(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map);

	public Complex and_pro(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map);

	public Complex or_pro(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map);

	public Complex and_opp(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map);

	public Complex or_opp(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map);

	public Complex counter_opp(Complex a, Complex b);

	public Complex counter_pro(Complex a, Complex b);

}

package lu.uni.adtool.domains.adtpredefined;

import java.util.HashMap;

import ax1.calculators.CalculatorComplex;
import ax1.calculators.CalculatorComplexFactory;
import ax1.variables.Complex;
import lu.uni.adtool.domains.AdtComplexDomain;
import lu.uni.adtool.domains.AdtDomain;
import lu.uni.adtool.domains.ValueAssignement;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tools.Options;
import lu.uni.adtool.tree.ADTNode;
import lu.uni.adtool.tree.Node;

public class DomainComplex implements AdtDomain<Complex>, AdtComplexDomain<Ring> {

	private CalculatorComplex calculator;

	private static final long serialVersionUID = 1L;

	public DomainComplex() {
		calculator = CalculatorComplexFactory.create("worstCase"); // TODO the string param can be get from setting environment
																	// variable, or using the class name or whatever
																	// other options
	}

	public final boolean isValueModifiable(boolean isProponent) {
		return true;
	}

	public final String getName() {
		return Options.getMsg("adtdomain.complex.name");
	}

	public final String getDescription() {
		final String name = Options.getMsg("adtdomain.complex.description");
		final String vd = "prob [0,1], impact [0,10], cost [0,\u221E]";
		final String[] operators = { "<i>x</i>&nbsp;+&nbsp;<i>y</i>", "<i>x</i><i>y</i>",
				"<i>x</i>&nbsp;+&nbsp;<i>y</i>", "<i>x</i><i>y</i>", "<i>x</i>(1&nbsp;-&nbsp;<i>y</i>)",
				"<i>x</i>(1&nbsp;-&nbsp;<i>y</i>)" };
		return DescriptionGenerator.generateDescription(this, name, vd, operators);
	}

	@Override
	public Complex getDefaultValue(Node node) {
		return new Complex();
	}

	int counter = 0;

	@Override
	public Complex calc(Complex a, Complex b, ADTNode.Type type) {
		System.out.println(
				"@@@@DEVELOPER ERROR@@@@: calc(a,b,type) should not be called on complex domains, Use calc(a,b,node) instead");
		return new Complex(String.valueOf(counter++));
	}

	@Override
	public Complex calc(Complex a, Complex b, ADTNode node, ValueAssignement<Ring> values, HashMap<Node, Ring> map) {
		return calculator.calc(a, b, node, values, map);
	}

	@Override
	public Complex cp(Complex a, Complex b) {
		return calculator.counter_pro(a, b);
	}

	@Override
	public Complex co(Complex a, Complex b) {
		return calculator.counter_opp(a, b);
	}

}
package lu.uni.adtool.domains.adtpredefined;

import lu.uni.adtool.domains.AdtDomain;
import lu.uni.adtool.domains.rings.Complex;
import lu.uni.adtool.tools.Options;
import lu.uni.adtool.tree.ADTNode.Type;
import lu.uni.adtool.tree.Node;

public class ProbSucc2 implements AdtDomain<Complex> {

	public ProbSucc2() {
	}

	public final boolean isValueModifiable(boolean isProponent) {
		return true;
	}

	public final String getName() {
		return Options.getMsg("adtdomain.probsucc2.name");
	}

	public final String getDescription() {
		final String name = Options.getMsg("adtdomain.probsucc2.description");
		final String vd = "[0,1]";
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
	public Complex calc(Complex a, Complex b, Type type) {
		return new Complex(String.valueOf(counter++));
	}

	@Override
	public Complex cp(Complex a, Complex b) {
		return new Complex();
	}

	@Override
	public Complex co(Complex a, Complex b) {
		return new Complex();
	}
}
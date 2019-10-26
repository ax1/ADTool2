package ax1;

public class CalculatorComplexFactory {
	public static CalculatorComplex create(String name) {
		if (name == "worstCase")
			return new CalculatorWorstCase();
		else
			return null;
	}
}

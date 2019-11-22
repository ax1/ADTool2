package ax1.variables;

import java.util.Locale;

import lu.uni.adtool.domains.rings.Ring;

public class Complex implements Ring {

	private String value;

	public Complex() {
		this.value = "";
	}

	/**
	 * N dimension complex value
	 * 
	 * @param value format: "r1 r2 r3..." or "r1 r2 r3..." where r1 r2 r3... are
	 *              double numbers
	 */
	public Complex(String value) {
		this.value = normalize(value);
	}

	public final String toString() {
		try {
			if (value.equals(""))
				return value;
			double[] v = toVector();
			double res = v[0] * v[1] / v[2];
			return f(v[0]) + " " + f(v[1]) + " " + f(v[2]) + " " + f(res);
		} catch (Exception e) {
			return value;
		}
	}

	@Override
	public String toUnicode() {
		return value;
	}

	@Override
	public boolean updateFromString(String s) {
		value = MIGRATE(s);
		return true;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object clone() {
		return new Complex(value);
	}

	public double[] toVector() {
		return toVector(this.value);
	}

	private static double[] toVector(String value) {
		String[] data = value.split(" ");
		double[] arr = new double[data.length];
		for (int r = 0; r < data.length; r++)
			arr[r] = Double.valueOf(data[r]);
		return arr;
	}

	/**
	 * Transform a complex 3D value into a 4D value (and shrink to 2 decimals)
	 * 
	 * @param value
	 * @return
	 */
	private static final String normalize(String value) {
		try {
			if (value.equals(""))
				return value;
			value = MIGRATE(value);
			double[] v = toVector(value);
			double res = v[0] * v[1] / v[2];
			return f(v[0]) + " " + f(v[1]) + " " + f(v[2]) + " " + f(res);
		} catch (Exception e) {
			return value;
		}
	}

	private static String MIGRATE(String value) {
		value = value.replace(",", " ");// TODO, ONLY FOR MIGRATION to new format
		value = value.replace("  ", " "); // TODO, this can be removed after migration of OLD complex trees
		return value.toString();
	}

	private static String f(double val) {
		return String.format(Locale.US, "%.2f", val);
	}

}

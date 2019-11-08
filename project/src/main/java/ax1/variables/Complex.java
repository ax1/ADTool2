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
	 * @param value format: "r1,r2,r3..." or "r1, r2, r3..." where r1 r2 r3... are
	 *              double numbers
	 */
	public Complex(String value) {
		this.value = value;
	}

	public final String toString() {
		try {
			if (value.equals(""))
				return value;
			double[] v = toVector();
			double res = v[0] * v[1] / v[2];
			return v[0] + ", " + v[1] + ", " + v[2] + ", " + String.format(Locale.US, "%.2f", res);
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
		value = s;
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
		String[] data = value.replace(" ", "").split(",");
		double[] arr = new double[data.length];
		for (int r = 0; r < data.length; r++)
			arr[r] = Double.valueOf(data[r]);
		return arr;
	}
}

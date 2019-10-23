package lu.uni.adtool.domains.rings;

public class Complex implements Ring {

	private String value;

	public Complex() {
		this.value = "";
	}

	public Complex(String value) {
		this.value = value;
	}

	public final String toString() {
		return value;
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
}

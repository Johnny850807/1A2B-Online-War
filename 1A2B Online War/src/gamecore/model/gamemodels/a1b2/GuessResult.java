package gamecore.model.gamemodels.a1b2;

public class GuessResult {
	private int a;
	private int b;
	
	public GuessResult(int a, int b) {
		this.a = a;
		this.b = b;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	@Override
	public int hashCode() {
		return a * 10 + b;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuessResult other = (GuessResult) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		return true;
	}
}

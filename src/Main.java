package friendly_prng;

import java.util.List;

public class Main {

	public static void main(String[] args) {

		AdjustedRandom gen = new AdjustedRandom(new BranchedAdjuster());
		gen.diagnostic(0.60, 100000);
		
		String s = new String("a");
		List l;

	}

}

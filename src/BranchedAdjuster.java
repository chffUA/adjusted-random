package friendly_prng;

/**
 * 
 * See {@link Adjuster Adjuster}.
 *
 */
public class BranchedAdjuster implements Adjuster {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double adjust(double chance) {
		if (chance<=0.01 || chance>=0.99) return 0;
		if (chance<=0.1 || chance>=0.9) return 0.001;
		if (chance<=0.25 || chance>=0.75) return 0.01;
		if (chance<=0.40 || chance>=0.60) return 0.05;
		return 0.1;
	}

}

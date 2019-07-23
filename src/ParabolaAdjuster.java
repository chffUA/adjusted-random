package friendly_prng;

/**
 * 
 * See {@link Adjuster Adjuster}.
 *
 */
public class ParabolaAdjuster implements Adjuster {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double adjust(double chance) {
		double r = -Math.pow(chance-0.5,2) + 0.25;
		return (-Math.abs(chance-0.5)+0.5) * r;
	}

}

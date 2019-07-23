package friendly_prng;

/**
 * Implementations of the <code>Adjuster</code> interface are responsible
 * for the modification of a probability value after each roll.
 * <p>
 * This interface requires a single method, <b><i>adjust</b></i>.
 *
 */
public interface Adjuster {
	
	/**
	 * <blockquote><i><b>adjust</b></i>
	 * <p>
	 * <code>&nbsp;public double adjust(double chance)</code>
	 * <p>
	 * This method dictates the exact adjustment (modification) made to
	 * a probability value after calling the <code>next(double chance)</code>
	 * method of the {@link AdjustedRandom AdjustedRandom} class.
	 * <p>
	 * The actual probability value of a successful roll is equal to the
	 * <b>successive application</b> of this method an amount of times equal to how
	 * large the current success or failure streak is. In each iteration, the obtained
	 * result is <b>added</b> to the current chance (<b>chance</b> is always within [0, 1]) 
	 * if it's a <b>failure streak</b>, and is <b>subtracted</b> if it's a <b>success streak</b>.
	 * <p>
	 * This can be seen as a plot where the x-axis is the <b>chance</b> parameter, which ranges
	 * from 0 to 1, and the result of <b><i>adjust</i></b> is the y-axis.
	 * <p>
	 * In order to shorten streaks, the returned value should be positive. To lengthen
	 * streaks, the returned value should be negative.
	 * <p>
	 * See example for an <b><i>adjust</b></i> method that always returns 0.10 * chance:
	 * <p>
	 * <blockquote><code>
	 * AdjustedRandom.next(0.4) // initial chance is 40%
	 * <p>
	 * With a success streak of 2:<br>
	 * Chance = 0.4 - (0.10 * 0.4) = 0.36<br>
	 * Chance = 0.36 - (0.10 * 0.36) = 0.324<br>
	 * 32.4% is now the actual success chance
	 * <p>
	 * With a failure streak of 2:<br>
	 * Chance = 0.4 + (0.10 * 0.4) = 0.44<br>
	 * Chance = 0.44 + (0.10 * 0.44) = 0.484<br>
	 * 48.4% is now the actual success chance
	 * </code></blockquote>
	 * 
	 * @param chance - probability value for the current iteration
	 * @return the amount to add or subtract to the current <b>chance</b>
	 */
	public double adjust(double chance);
}

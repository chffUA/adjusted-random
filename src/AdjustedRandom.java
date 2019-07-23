package friendly_prng;

import java.util.Random;

/**
 * The <code>AdjustedRandom</code> class provides a way to
 * generate successful and unsuccessful outcomes in a way
 * that attempts to preserve a <b>probability value</b> chosen 
 * by the user, but has a large impact on the length of <b>success 
 * and failure streaks</b>.
 * <p>
 * This class can be used to generate sequences that are less
 * random (but still random) by modifying the chance of success
 * based on the outcome of previous rolls.
 */
public class AdjustedRandom {
	
	private int streak; //<0 = fail streak, >0 = success streak
	private Adjuster adjuster;
	private Random random;
	
	/**
	 * <blockquote><i><b>AdjustedRandom</b></i>
	 * <p>
	 * <code>&nbsp;public AdjustedRandom()</code>
	 * <p>
	 * Creates an instance of this class and defaults to 
	 * a {@link BranchedAdjuster BranchedAdjuster} as its
	 * probability modifier.
	 */
	public AdjustedRandom() {
		setAdjuster(new BranchedAdjuster());
		random = new Random();
	}
	
	/**
	 * <blockquote><i><b>AdjustedRandom</b></i>
	 * <p>
	 * <code>&nbsp;public AdjustedRandom(Adjuster adjuster)</code>
	 * <p>
	 * Creates an instance of this class and uses the selected 
	 * {@link Adjuster Adjuster} implementation as its
	 * probability modifier.
	 * 
	 * @param adjuster - the Adjuster implementation
	 */
	public AdjustedRandom(Adjuster adjuster) {
		setAdjuster(adjuster);
		random = new Random();
	}
	
	/**
	 * <blockquote><i><b>setAdjuster</b></i>
	 * <p>
	 * <code>&nbsp;public AdjustedRandom setAdjuster(Adjuster adjuster)</code>
	 * <p>
	 * Sets the given {@link Adjuster Adjuster} implementation as the
	 * probability modifier of this instance, without clearing its memory 
	 * of previous outcomes.
	 * 
	 * @param adjuster - the Adjuster implementation
	 * @return this instance (useful for method chaining)
	 */
	public AdjustedRandom setAdjuster(Adjuster adjuster) {
		this.adjuster = adjuster;
		return this;
	}
	
	/**
	 * <blockquote><i><b>next</b></i>
	 * <p>
	 * <code>&nbsp;public boolean next(double chance)</code>
	 * <p>
	 * Generates a new random outcome based on the given chance
	 * and the previous history of outcomes for this instance, if any.
	 * 
	 * @throws IllegalArgumentException if <b>chance</b> is not in the interval [0, 1]
	 * @param chance - the intended chance of obtaining a success, between 0 (inclusive) and 1 (inclusive)
	 * @return a success or failure in the form of a boolean
	 */
	public boolean next(double chance) {
		if (chance<0 || chance>1) throw new IllegalArgumentException("Chance must be in the interval [0, 1]");
		return rollAndUpdateStreak(chance);
	}
	
	private boolean rollAndUpdateStreak(double chance) {
		
		if (random.nextDouble()<=modifiedChance(chance)) { //success based on modified chance, update streak
			streak = (streak>=0 ? streak+1 : 0);
			return true;
		}
		
		else { //fail based on modified chance, update streak
			streak = (streak<=0 ? streak-1 : 0);
			return false;
		}

	}
	
	private double modifiedChance(double chance) {
		if (streak<0) for (int i=0; i>streak; i--) chance += adjuster.adjust(chance);
		else if (streak>0) for (int i=0; i<streak; i++) chance -= adjuster.adjust(chance);
		
		if (chance<0) chance = 0; if (chance>1) chance = 1;
		return chance;
	}
	
	/**
	 * <blockquote><i><b>reset</b></i>
	 * <p>
	 * <code>&nbsp;public void reset()</code>
	 * <p>
	 * Clears any memory of the previous history of outcomes for this instance.
	 * Guarantees that the following <code>next(double chance)</code> call will have a
	 * probability of success exactly equal to <b>chance</b>.
	 */
	public void reset() {
		streak = 0;
	}
	
	/**
	 * <blockquote><i><b>diagnostic</b></i>
	 * <p>
	 * <code>&nbsp;public void diagnostic(double chance, int runs)</code>
	 * <p>
	 * Creates a new instance of the <code>AdjustedRandom</code> class that inherits the 
	 * {@link Adjuster Adjuster} implementation of its parent class (that is, the class
	 * <b><i>diagnostic</b></i> was called on).
	 * <br>
	 * Within that new instance, runs the <code>next(double chance)</code> method an amount of times equal
	 * to <b>runs</b> and with a probability equal to <b>chance</b>, always modifying probability based on 
	 * previous outcomes.
	 * <br>
	 * Once done, it prints to the Console a short report of the obtained results, including:
	 * <p>
	 * - Example probability adjustment values given by the Adjuster in use
	 * <br>
	 * - Total and relative amounts and of successes and failures
	 * <br>
	 * - Largest success and failure streaks
	 * <br>
	 * - Largest success and failure streaks of an unbiased random number generator, for comparison
	 * 
	 * @param chance - the intended chance of obtaining a success, between 0 (inclusive) and 1 (inclusive)
	 * @param runs - the amount of rolls
	 */
	public void diagnostic(double chance, int runs) {
		int itrue = 0, itst = 0, maxitst = 0, ifalse = 0, ifst = 0, maxifst = 0;
		int rtst = 0, maxrtst = 0, rfst = 0, maxrfst = 0;
		AdjustedRandom prng = new AdjustedRandom(adjuster);
		Random rprng = new Random();
		
		for (int i=0; i<runs; i++) {
			if (prng.next(chance)) { itrue++; itst++; if (ifst>maxifst) maxifst = ifst; ifst = 0; }
			else { ifalse++; ifst++; if (itst>maxitst) maxitst = itst; itst = 0; }
			
			if (rprng.nextDouble()<=chance) { rtst++; if (rtst>maxrtst) maxrtst = rtst; rfst = 0; }
			else { rfst++; if (rfst>maxrfst) maxrfst = rfst; rtst = 0; }
		}
		if (ifst>maxifst) maxifst = ifst; if (itst>maxitst) maxitst = itst;
		if (rfst>maxrfst) maxrfst = rfst; if (rtst>maxrtst) maxrtst = rtst;
		
		System.out.printf("---- Diagnostic for %d runs ----\n", runs);
		System.out.printf("Adjustment value for chance = %.1f%%: ±%.2f\n", chance*100d, adjuster.adjust(chance)*100d);
		System.out.printf("%s: ±%.2f  //  %s: ±%.2f  //  %s: ±%.2f  //  %s: ±%.2f  //  %s: ±%.2f\n",
				"10%", adjuster.adjust(0.1)*100d, "25%", adjuster.adjust(0.25)*100d, "50%", adjuster.adjust(0.5)*100d,
				"75%", adjuster.adjust(0.75)*100d, "90%", adjuster.adjust(0.9)*100d);
		System.out.printf("Expected successes: %d (%.1f%%)\n\n", Math.round(chance*runs), chance*100d);
		System.out.printf("Successes: %d (%.1f%%) Longest streak: %d ", itrue, 1d*itrue/runs*100d, maxitst);
		System.out.printf("(%d without adjustments)\n", maxrtst);
		System.out.printf("Failures: %d (%.1f%%) Longest streak: %d ", ifalse, 1d*ifalse/runs*100d, maxifst);
		System.out.printf("(%d without adjustments)\n", maxrfst);
	}
	
}

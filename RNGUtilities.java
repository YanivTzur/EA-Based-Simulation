package ex12Q2;

import java.util.Random;

/**
 * Used for generating random numbers for stochastic processes.
 * @author Yaniv Tzur
 */
public class RNGUtilities 
{
	private static Random _randomNumberGenerator; // Used for generating random (or rather mainly
												  // pseudo-random) numbers.
	
	/**
	 * Generates a random integer in the interval [start, end] (interval includes both start and end)
	 * and returns it.
	 * @param start The smallest possible number that can be generated.
	 * @param end The largest possible number that can be generated.
	 * @return A randomly generated integer.
	 */
	public static int generateRandomInteger(int start, int end)
	{
		int range = end - start;
		if (_randomNumberGenerator == null)
			_randomNumberGenerator = new Random();
		if (range > 0)
			return _randomNumberGenerator.nextInt(range) + start;
		return start;
	}
	
	/**
	 * Returns a randomly generated number in the interval [0,1].
	 * @return A randomly generated number in the interval [0,1].
	 */
	public static double generateRandomDouble()
	{
		return _randomNumberGenerator.nextDouble();
	}
}

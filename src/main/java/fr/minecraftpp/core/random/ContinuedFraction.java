/*
 * Modified for simplicity. From : http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/index.html
 * in apache.commons.math3.util
 */

package fr.minecraftpp.core.random;

public abstract class ContinuedFraction
{

	private static final double DEFAULT_EPSILON = 10e-9;

	protected ContinuedFraction()
	{
		super();
	}

	protected abstract double getA(int n, double x);

	protected abstract double getB(int n, double x);

	public double evaluate(double x) throws Exception
	{
		return evaluate(x, DEFAULT_EPSILON, Integer.MAX_VALUE);
	}

	public double evaluate(double x, double epsilon) throws Exception
	{
		return evaluate(x, epsilon, Integer.MAX_VALUE);
	}

	public double evaluate(double x, int maxIterations) throws Exception
	{
		return evaluate(x, DEFAULT_EPSILON, maxIterations);
	}

	public double evaluate(double x, double epsilon, int maxIterations) throws Exception
	{
		final double small = 1e-50;
		double hPrev = getA(0, x);

		if (equals(hPrev, 0.0, small))
		{
			hPrev = small;
		}

		int n = 1;
		double dPrev = 0.0;
		double cPrev = hPrev;
		double hN = hPrev;

		while (n < maxIterations)
		{
			final double a = getA(n, x);
			final double b = getB(n, x);

			double dN = a + b * dPrev;
			if (equals(dN, 0.0, small))
			{
				dN = small;
			}
			double cN = a + b / cPrev;
			if (equals(cN, 0.0, small))
			{
				cN = small;
			}

			dN = 1 / dN;
			final double deltaN = cN * dN;
			hN = hPrev * deltaN;

			if (Double.isInfinite(hN))
			{
				throw new Exception("Convergence exception : " + x);
			}
			if (Double.isNaN(hN))
			{
				throw new Exception("Convergence exception : " + x);
			}

			if (Math.abs(deltaN - 1.0) < epsilon)
			{
				break;
			}

			dPrev = dN;
			cPrev = cN;
			hPrev = hN;
			n++;
		}

		if (n >= maxIterations)
		{
			throw new Exception("Max count exceeded: " + maxIterations);
		}

		return hN;
	}

	/*
	 * From apache.commons.math3.util.precision
	 */

	private static final long POSITIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(+0.0);
	private static final long NEGATIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(-0.0);
	private static final long SGN_MASK = 0x8000000000000000L;

	public static boolean equals(double x, double y, double eps)
	{
		return equals(x, y, 1) || Math.abs(y - x) <= eps;
	}

	/*
	 * The 1.12 port only had a float version of this method: the call above resolved to equals(double, double, double) itself and recursed forever. The 1.12 colors never reached the continued fraction, which hid the bug.
	 */
	public static boolean equals(final double x, final double y, final int maxUlps)
	{
		final long xInt = Double.doubleToRawLongBits(x);
		final long yInt = Double.doubleToRawLongBits(y);

		final boolean isEqual;
		if (((xInt ^ yInt) & SGN_MASK) == 0L)
		{
			isEqual = Math.abs(xInt - yInt) <= maxUlps;
		}
		else
		{
			final long deltaPlus;
			final long deltaMinus;
			if (xInt < yInt)
			{
				deltaPlus = yInt - POSITIVE_ZERO_DOUBLE_BITS;
				deltaMinus = xInt - NEGATIVE_ZERO_DOUBLE_BITS;
			}
			else
			{
				deltaPlus = xInt - POSITIVE_ZERO_DOUBLE_BITS;
				deltaMinus = yInt - NEGATIVE_ZERO_DOUBLE_BITS;
			}

			if (deltaPlus > maxUlps)
			{
				isEqual = false;
			}
			else
			{
				isEqual = deltaMinus <= (maxUlps - deltaPlus);
			}
		}

		return isEqual && !Double.isNaN(x) && !Double.isNaN(y);
	}
}

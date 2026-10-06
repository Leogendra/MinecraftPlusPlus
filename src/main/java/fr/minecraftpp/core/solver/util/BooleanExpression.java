package fr.minecraftpp.core.solver.util;

/**
 * Evaluates the boolean expressions of the intension constraints once their variables are replaced by their values.
 *
 * The 1.12 version evaluated them with the Nashorn JavaScript engine, which no longer exists since Java 15. Only the grammar produced by {@code Pretreatment} is supported: integers, {@code ==}, {@code &&}, {@code ||} and parentheses, with the usual precedence ({@code &&} binds tighter than {@code ||}).
 */
public class BooleanExpression
{
	private final String expression;
	private int position;

	private BooleanExpression(String expression)
	{
		this.expression = expression;
		this.position = 0;
	}

	public static boolean evaluate(String expression)
	{
		BooleanExpression parser = new BooleanExpression(expression);
		boolean result = parser.parseOr();
		parser.skipSpaces();

		if (parser.position != expression.length())
		{
			throw parser.error("unexpected character");
		}
		else
		{
			return result;
		}
	}

	private boolean parseOr()
	{
		boolean result = this.parseAnd();

		while (this.consume("||"))
		{
			boolean right = this.parseAnd();
			result = result || right;
		}

		return result;
	}

	private boolean parseAnd()
	{
		boolean result = this.parseComparison();

		while (this.consume("&&"))
		{
			boolean right = this.parseComparison();
			result = result && right;
		}

		return result;
	}

	private boolean parseComparison()
	{
		this.skipSpaces();

		if (this.peek() == '(')
		{
			return this.parseParenthesis();
		}
		else
		{
			int left = this.parseInteger();

			if (!this.consume("=="))
			{
				throw this.error("'==' expected");
			}

			int right = this.parseInteger();
			return left == right;
		}
	}

	private boolean parseParenthesis()
	{
		this.position++;
		boolean result = this.parseOr();

		if (!this.consume(")"))
		{
			throw this.error("')' expected");
		}

		return result;
	}

	private int parseInteger()
	{
		this.skipSpaces();
		int start = this.position;

		if (this.peek() == '-')
		{
			this.position++;
		}

		while (Character.isDigit(this.peek()))
		{
			this.position++;
		}

		try
		{
			return Integer.parseInt(this.expression.substring(start, this.position));
		}
		catch (NumberFormatException exception)
		{
			throw this.error("integer expected");
		}
	}

	private boolean consume(String token)
	{
		this.skipSpaces();

		if (this.expression.startsWith(token, this.position))
		{
			this.position += token.length();
			return true;
		}
		else
		{
			return false;
		}
	}

	private void skipSpaces()
	{
		while (this.position < this.expression.length() && this.expression.charAt(this.position) == ' ')
		{
			this.position++;
		}
	}

	private char peek()
	{
		if (this.position < this.expression.length())
		{
			return this.expression.charAt(this.position);
		}
		else
		{
			return '\0';
		}
	}

	private IllegalArgumentException error(String reason)
	{
		return new IllegalArgumentException("Invalid expression at position " + this.position + " (" + reason + "): " + this.expression);
	}
}

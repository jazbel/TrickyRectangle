
/**
 * The "Score" class
 * Purpose: To make and keep track of Scores
 * Scores are generated based on the time it takes a player to complete a puzzle
 * @author Sakeena Panju
 * @version January 25, 2012
 */
public class Score implements Comparable<Score>
{
	// Variable declaration
	private String name;
	private int time;

	/**
	 * Constructs a Score object
	 * @param name the name of the person who acheived this Score
	 * @param score the score achieved (in seconds)
	 */
	public Score(String name, int score)
	{
		this.name = name;
		this.time = score;
	}

	/**
	 * Gets the scorer's name
	 * @return the scorer's name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the scorer's score
	 * @return the scorer's score
	 */
	public int getScore()
	{
		return time;
	}

	/**
	 * Compares this Score to another Score based on the time taken by a player to solve a puzzle
	 * @param other the other Score to compare to this Score
	 * @return a value < 0 if this Score's time is less than the other Score's time
	 * 		   a value > 0 if this Score's time is greater than the other Score's time
	 * 		   a value equal to 0 if both times are the same
	 */
	@Override
	public int compareTo(Score other)
	{
		return (other.time - this.time);
	}

	/**
	 * Returns a String representation of this Score
	 * @return the name and time stored by this Score as a String
	 */
	public String toString() 
	{
		return String.format("%-20s %02d:%02d", this.name, (int)(this.time/60), this.time%60);
	}
}

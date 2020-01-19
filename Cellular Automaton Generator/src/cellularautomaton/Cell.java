package cellularautomaton;

/**
 * class Cell will act as a atomized building block for a CellularAutomaton.
 * Each cell will have a unique identifier, and a unique Color specified by
 * its rgbValue.
 * 
 * @author Jacob Cohen
 */
public class Cell implements Comparable<Cell>
{
	public int rgbValue;
	public String id;
	
	public Cell(String id, int rgbValue)
	{
		this.rgbValue = rgbValue;
		this.id = id;
	}
	
	public String toString()
	{
		return id;
	}

	public int compareTo(Cell o)
	{
		int val = 0;
		
		if(rgbValue < o.rgbValue)
			val = -1;
		else if(rgbValue > o.rgbValue)
			val = +1;
		
		return val;
	}
}
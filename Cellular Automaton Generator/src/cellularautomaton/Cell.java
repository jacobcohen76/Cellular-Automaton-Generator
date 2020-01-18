package cellularautomaton;

/**
 * class Cell will act as a atomized building block for a CellularAutomaton.
 * Each cell will have a unique identifier, and a unique Color specified by
 * its rgbValue.
 * 
 * @author Jacob Cohen
 */
public class Cell
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
}

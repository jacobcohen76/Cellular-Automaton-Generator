package cellularautomaton;

import java.awt.Color;

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
	
	private Color color;
	
	/**
	 * Constructs a Cell with specified identifier and rgbValue
	 * @param id the identifier to give this Cell
	 * @param rgbValue the rgbValue to make this Cell represent
	 */
	public Cell(String id, int rgbValue)
	{
		this.rgbValue = rgbValue;
		this.id = id;
		
		color = new Color(rgbValue);
	}
	
	/**
	 * Gets the RGB value that the Image Generator will use to represent this Cell
	 * @return the RGB value that the Image Generator will use to represent this Cell
	 */
	public int getRGB()
	{
		return color.getRGB();
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
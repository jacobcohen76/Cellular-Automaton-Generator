package cellularautomaton;

import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * class CellularAutomaton to model and represent a Cellular Automaton,
 * needs a starting Row, a number of Rows, and a Dictionary that defines
 * the rules of how the other Rows will be generated based off the previous row,
 * given this information will fill out an Array based data structure containing
 * rows of Cells, which can then be turned into a ".png" image depending on the
 * rgbValues of the Cells contained within this CellularAutomaton
 * 
 * @author Jacob Cohen
 */
public class CellularAutomaton implements Iterable<Row>
{
	private Row rows[];
	
	private int numRows;
	private int numColumns;
	
	private boolean isGenerated;
	
	/**
	 * constructs a new CellularAutonoma with specified starting row, row count, and rules to generate with
	 * 
	 * @param startingRow the starting Row that will be used to generate all of the other Rows in this CellularAutonoma
	 * @param numRows the number of Rows to make this CellularAutonoma have when generated
	 * @param dictionary contains the information on how to generate the other Rows in this CellularAutonoma
	 */
	public CellularAutomaton(int numRows)
	{
		rows = new Row[numRows];
		this.numRows = numRows;
		
		isGenerated = false;
	}
	
	/**
	 * Generates and sets all of the Rows and Cells within this
	 * CellularAutomaton based on the provided startingRow
	 */
	public void generate(Row startingRow, Dictionary dictionary)
	{
		rows[0] = startingRow;
		
		this.numColumns = startingRow.size();
		
		for(int i = 1; i < rows.length; i++)
			rows[i] = Row.generate(rows[i - 1], dictionary);
		
		isGenerated = true;
	}
	
	/**
	 * Returns true if the specified index is a valid Row index of this CellularAutomaton
	 * 
	 * @param index the Row index to check if it is valid
	 * @return if the Row at specified index is or is not within the boundaries of this CellularAutomaton
	 */
	private boolean insideBounds(int index)
	{
		return 0 <= index && index < rows.length;
	}
	
	/**
	 * Gets the Row at specified index. Will return null if the specified
	 * index is outside of the boundaries of this CellularAutomaton
	 * 
	 * @param index the index to get the Row of
	 * @return if index is within the boundaries of this CellularAutomaton,
	 * returns the Row contained at specified index, else returns null
	 */
	public Row getRow(int index)
	{
		Row row = null;
		
		if(insideBounds(index))
			row = rows[index];
		
		return row;
	}
	
	/**
	 * Gets the Cell at the specified row and column indices. Will return null if
	 * either the row or column index are out of bounds
	 * 
	 * @param rowIndex the row Index of the Cell to get
	 * @param colIndex the column index of the Cell to get
	 * @return if the specified row and column indexes are within the boundaries
	 * of this CellularAutomaton returns the Cell at specified indices, else
	 * returns null
	 */
	public Cell get(int rowIndex, int colIndex)
	{
		Cell cell = null;
		Row row = getRow(rowIndex);
		
		if(row != null)
			cell = row.get(colIndex);
		
		return cell;
	}
	
	/**
	 * class RowIterator to be used to iterate through
	 * all of the Rows of this CellularAutomaton
	 * 
	 * @author Jacob Cohen
	 */
	private class RowIterator implements Iterator<Row>
	{
		private int currentIndex;
		
		public RowIterator()
		{
			currentIndex = -1;
		}
		
		public boolean hasNext()
		{
			return currentIndex < numRows;
		}

		public Row next()
		{
			currentIndex++;
			return rows[currentIndex];
		}	
	}
	
	public Iterator<Row> iterator()
	{
		return new RowIterator();
	}
	
	public boolean isGenerated()
	{
		return isGenerated;
	}
	
	/**
	 * Creates and returns a BufferedImage based on the rgbValues of the Cells
	 * that this CellularAutomaton is made up of.
	 * 
	 * @return a BufferedImage that visually represents this CellularAutomaton
	 * @throws Exception if you try to get the Buffered Image of a CellularAutomaton
	 * that has not been generated yet
	 */
	public BufferedImage getBufferedImage() throws Exception
	{
		if(this.isGenerated() == false)
			throw new Exception("Error, this CellularAutomaton (" + super.toString() + ") has not been generated yet");
		
		BufferedImage image = new BufferedImage(numColumns, numRows, BufferedImage.TYPE_INT_ARGB);
		
		for(int row = 0; row < image.getWidth(); row++)
			for(int col = 0; col < image.getHeight(); col++)
				image.setRGB(row, col, get(col, row).getRGB());
		
		return image;
	}
	
	public String toString()
	{
		String str = "";
		
		for(Row row : rows)
			str += row + "\n";
		
		if(str.length() > 0)
			str.substring(0, str.length() - 1);
		
		return str;
	}
}

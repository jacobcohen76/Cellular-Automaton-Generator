package cellularautomaton;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class CellularAutomaton implements Iterable<Row>
{
	private Row rows[];
	private Dictionary dictionary;
	
	private int numRows;
	private int numColumns;
	
	public CellularAutomaton(Row startingRow, int numRows, Dictionary dictionary)
	{
		rows = new Row[numRows];
		rows[0] = startingRow;
		
		this.numRows = numRows;
		this.numColumns = startingRow.size();
		
		this.dictionary = dictionary;
	}
	
	/**
	 * Generates and sets all of the Rows in this
	 * CellularAutomaton based on the previous Row
	 */
	public void generate()
	{
		for(int i = 1; i < rows.length; i++)
			rows[i] = Row.generate(rows[i - 1], dictionary);
	}
	
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
	 * the Rows of this CellularAutomaton
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
	
	/**
	 * Creates and returns a BufferedImage based on the rgbValues of the Cells
	 * that this CellularAutomaton is made up of.
	 * 
	 * @return a BufferedImage that visually represents this CellularAutomaton
	 */
	public BufferedImage getBufferedImage()
	{
		BufferedImage image = new BufferedImage(numColumns, numRows, BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < image.getWidth(); i++)
		{
			for(int j = 0; j < image.getHeight(); j++)
			{
				image.setRGB(i, j, new Color(get(j, i).rgbValue).getRGB());
			}
		}
		
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

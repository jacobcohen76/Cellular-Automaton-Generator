package cellularautomaton;

import java.util.Iterator;

/**
 * Row is an Object that will be used to model a row of
 * cells found in a CellularAutomaton.
 * 
 * @author Jacob Cohen
 */
public class Row implements Iterable<Cell[]>
{
	private Cell[] cells;
	private int patternSize;
	
	public Row(Cell[] cells, int patternSize)
	{
		this.cells = cells;
		this.patternSize = patternSize;
	}
	
	public Row(int numColumns, int patternSize)
	{
		cells = new Cell[numColumns];
		this.patternSize = patternSize;
	}
	
	/**
	 * Gets the size of this Row
	 * 
	 * @return the size of this Row
	 */
	public int size()
	{
		return cells.length;
	}
	
	/**
	 * Given a parent row and a dictionary of rules, generates a new Row
	 * based on the parent row and the rules specified in the dictionary
	 * 
	 * @param parent the parent row of the new Row to generate
	 * @param dictionary the dictionary of rules base the generated row on.
	 * @return a new Row created generated by using the provided parent and
	 * rules defined in the given dictionary
	 */
	public static Row generate(Row parent, Dictionary dictionary)
	{
		Row generatedRow = new Row(parent.size(), parent.patternSize);
		
		int i = 0;
		for(Cell[] pattern : parent)
		{
			replaceNULL(pattern, dictionary.getNULL());
			generatedRow.set(i, dictionary.getOutput(pattern));
			i++;
		}
		
		return generatedRow;
	}
	
	private static void replaceNULL(Cell[] pattern, Cell newNULL)
	{
		for(int i = 0; i < pattern.length; i++)
		{
			if(pattern[i] == null)
				pattern[i] = newNULL;
		}
	}
	
	private boolean insideBounds(int index)
	{
		return 0 <= index && index < cells.length;
	}
	
	public Cell get(int index)
	{
		Cell tmp = null;
		
		if(insideBounds(index))
			tmp = cells[index];
		
		return tmp;
	}
	
	public Cell set(int index, Cell cell)
	{
		Cell temp = null;
		
		if(insideBounds(index))
		{
			temp = cells[index];
			cells[index] = cell;
		}
		
		return temp;
	}
	
	private class PatternIterator implements Iterator<Cell[]>
	{
		private int centerIndex;
		
		public PatternIterator()
		{
			centerIndex = -1;
		}
		
		public boolean hasNext()
		{
			return centerIndex < cells.length;
		}

		public Cell[] next()
		{
			Cell[] next = new Cell[patternSize];
			
			centerIndex++;
			int parentIndex = centerIndex - patternSize / 2;
			
			for(int i = 0; i < patternSize; i++)
				next[i] = get(parentIndex + i);
			
			return next;
		}
	}
	
	public Iterator<Cell[]> iterator()
	{
		return new PatternIterator();
	}
	
	public String toString()
	{
		String str = "";
		for(Cell cell : cells)
			str += cell + " ";
		return str;
	}
}

package cellularautomaton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * class Alphabet used to define and map identifiers to Cells with specified rgbValues
 * 
 * @author Jacob Cohen
 */
public class Alphabet
{
	/**
	 * to store the set of all the Cells contained within this Alphabet
	 */
	private ArrayList<Cell> setOfCells;
	
	/**
	 * to map an rgbValue to a Cell
	 */
	private HashMap<Integer, Cell> rgbMap;
	
	/**
	 * to map an identifier to a Cell
	 */
	private HashMap<String, Cell> idMap;
	
	public Alphabet()
	{
		setOfCells = new ArrayList<Cell>();
		rgbMap = new HashMap<Integer, Cell>();
		idMap = new HashMap<String, Cell>();
	}
	
	/**
	 * sets this Alphabet to associate NULL with the specified rgbValue
	 * 
	 * @param newNULLrgbVal the rgbValue to associate NULL values with
	 * @throws Exception 
	 */
	public void setNULL(int newNULLrgbVal) throws Exception
	{
		if(rgbMap.containsKey(newNULLrgbVal))
			idMap.put("NULL", rgbMap.get(newNULLrgbVal));
		else
			throw new Exception(""
					+ "Error, NULL must be mapped to an rgbValue that is already mapped to an identifier in this Alphabet,\n"
					+ "your defined rgbValue (" + Integer.toString(newNULLrgbVal, 16) + ") is not an element in your alphabet,\n"
					+ "to be more clear, please define the rgbValue of NULL to be one of the following Hexadecimal RGB values in\n"
					+ "the file \"settings.txt\",\n\n"
					+  this);
	}
	
	/**
	 * Adds the Cell to this Alphabet, will throw an error if a Cell with matching rgbValue or identifier
	 * already exists in this Alphabet
	 * 
	 * @param toAdd the Cell to add to this Alphabet
	 * @throws Exception throws an Exception if a Cell with a matching identifier or rgbValue already
	 * exists in this Alphabet
	 */
	public void add(Cell toAdd) throws Exception
	{
		if(rgbMap.put(toAdd.rgbValue, toAdd) != null || idMap.put(toAdd.id, toAdd) != null)
			throw new Exception("ERROR THE CELL" + toAdd.id + " HAS ALREADY BEEN ADDED TO THIS ALPHABET");
		
		setOfCells.add(toAdd);
	}
	
	/**
	 * Gets the Cell in this Alphabet with an identifier that matches the specified identifier
	 * 
	 * @param id the identifier of the Cell to get
	 * @return the Cell with an identifier that matches the specified identifier
	 */
	public Cell get(String id)
	{
		return idMap.get(id);
	}
	
	/**
	 * gets the Collection of all the Cells within this Alphabet
	 * @return the Collection of all the Cells within this Alphabet
	 */
	public ArrayList<Cell> getSetOfCells()
	{
		return setOfCells;
	}
	
	public Cell get(int index)
	{
		return setOfCells.get(index);
	}
	
	public String toString()
	{
		String str = super.toString();
		str = str.substring(str.indexOf('.') + 1) + ":\n";
		
		str += "id:" + "\t" + "rgbValue:" + "\n";
		
		for(Cell cell : setOfCells)
			str += cell.id + "\t" +  cell.rgbValue + "\n";
		
		str += "NULL" + "\t" + idMap.get("NULL").rgbValue;
		
		return str;
	}
}

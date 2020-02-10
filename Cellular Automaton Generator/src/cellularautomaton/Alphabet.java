package cellularautomaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

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
	
	private static final Random rand = new Random();
	
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
	 * Returns the number of unique Cells in this Alphabet
	 * @return the number of unique Cells in this Alphabet
	 */
	public int numCells()
	{
		return setOfCells.size();
	}
	
	private ArrayList<String> nonNULLCells = null;
	public String random()
	{
		if(nonNULLCells == null)
		{
			nonNULLCells = new ArrayList<String>();
			Set<String> ids = getIDset();
			for(String id : ids)
				if(id.contentEquals("NULL") == false)
					nonNULLCells.add(id);
		}
		return nonNULLCells.get(rand.nextInt(nonNULLCells.size()));
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
			throw new Exception( ""
					+ "Error, NULL must be mapped to an rgbValue that is already mapped to an identifier in this Alphabet,\n"
					+ "your defined rgbValue (" + Integer.toString(newNULLrgbVal, 16) + ") is not mapped to an element in your alphabet,\n"
					+ "to be more clear, please define the rgbValue of NULL to be one of the following Hexadecimal RGB values in\n"
					+ "the file \"settings.txt\", in other words place choose one of the rgbValues from the following list\n\n"
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
		Cell previouslyMappedCell = rgbMap.put(toAdd.rgbValue, toAdd);
		
		if(previouslyMappedCell != null)
			throw new Exception( ""
					+ "Error, the rgbValue " + toAdd.rgbValue + " for the Cell " + toAdd.id + " has already been mapped\n"
					+ "to the Cell " + previouslyMappedCell.id + " change the rgbValue for these Cells, or\n"
					+ "delete one of them to continue.");
		
		if(idMap.put(toAdd.id, toAdd) != null)
			throw new Exception( ""
					+ "Error, the Cell" + toAdd.id + " has already been added to your alphabet,\n"
					+ "delete this Cell or rename it from " + toAdd.id + " to something else that has not\n"
					+ "already been used in your alhpabet.");
		
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
	
	public Set<String> getIDset()
	{
		Set<String> idset = idMap.keySet();
		idset.remove("NULL");
		return idset;
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
		
		if(idMap.containsKey("NULL"))
			str += "NULL" + "\t" + idMap.get("NULL").rgbValue;
		
		return str;
	}
}

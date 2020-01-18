package cellularautomaton;

import java.util.ArrayList;
import java.util.HashMap;

public class Alphabet
{
	private ArrayList<Cell> setOfCells;
	private HashMap<Integer, Cell> rgbMap;
	private HashMap<String, Cell> idMap;
	private Cell NULL;
	
	public Alphabet()
	{
		setOfCells = new ArrayList<Cell>();
		rgbMap = new HashMap<Integer, Cell>();
		idMap = new HashMap<String, Cell>();
	}
	
	public void setNULL(int newNULLrgbVal)
	{
		if(rgbMap.containsKey(newNULLrgbVal))
		{
			NULL = rgbMap.get(newNULLrgbVal);
			idMap.put("NULL", NULL);
		}
		else
		{
			NULL = new Cell("NULL", newNULLrgbVal);
			idMap.put("NULL", NULL);
			rgbMap.put(newNULLrgbVal, NULL);
		}
	}
	
	public void add(Cell toAdd) throws Exception
	{
		if(rgbMap.put(toAdd.rgbValue, toAdd) != null || idMap.put(toAdd.id, toAdd) != null)
			throw new Exception("ERROR THE CELL" + toAdd.id + " HAS ALREADY BEEN ADDED TO THIS ALPHABET");
		
		setOfCells.add(toAdd);
	}
	
	public Cell get(String id)
	{
		return idMap.get(id);
	}
	
	public ArrayList<Cell> getSetOfCells()
	{
		return setOfCells;
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

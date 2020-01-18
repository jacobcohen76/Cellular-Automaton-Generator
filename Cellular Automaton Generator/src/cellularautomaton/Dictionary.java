package cellularautomaton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * class Dictionary to be used to map a Pattern of Cells to an Output.
 * 
 * @author Jacob Cohen
 */
public class Dictionary
{
	private Alphabet alphabet;
	private int ruleSize;
	
	private HashMap<String, Cell> outputMap;
	
	public Dictionary(Alphabet alphabet, int ruleSize)
	{
		this.alphabet = alphabet;
		this.ruleSize = ruleSize;
		
		outputMap = new HashMap<String, Cell>();
	}
	
	/**
	 * Gets the Cell that was mapped as an output to the provided pattern
	 * 
	 * @param pattern the pattern to get its mapped output
	 * @return the mapped output of pattern
	 */
	public Cell getOutput(Cell[] pattern)
	{
		return outputMap.get(getKey(pattern));
	}
	
	/**
	 * Gets the hash key for the specified pattern of Cells.
	 * Since each pattern has a unique permutation of Cells contained
	 * in our Alphabet, and each Cell in our Alphabet has unique 
	 * Identifier, combining the identifiers of the Cells in a pattern
	 * will get a unique identifier for a pattern. The hash key for
	 * a patter of Cells with identifiers as follows {W, W, B} would
	 * return the following key:<br>
	 * "W W B "
	 * 
	 * @param pattern the pattern to the unique hash-key of
	 * @return the hash-key of pattern
	 */
	private String getKey(Cell[] pattern)
	{
		String key = "";
		
		for(int i = 0; i < pattern.length; i++)
			key += pattern[i].id + " ";
		
		return key;
	}
	
	/**
	 * Gets the NULL cell defined in the Alphabet of this Dictionary
	 * 
	 * @return the NULL cell defined in the Alphabet of this Dictionary
	 */
	public Cell getNULL()
	{
		return alphabet.get("NULL");
	}
	
	/**
	 * Given a file, scans through and gets the specified patterns and outputs, and
	 * maps each pattern to its specified output.
	 * 
	 * @param file the file that contains the information to map the patterns to the outputs
	 * @throws Exception if there is a problem with reading and writing to specified file,
	 * or if the mapped patterns to outputs provided in the file is illegal.
	 */
	public void setOutputMap(File file) throws Exception
	{
		Scanner scan = new Scanner(file);
		while(scan.hasNext())
		{
			Cell[] pattern = new Cell[ruleSize];
			
			int i = 0;
			while(i < ruleSize)
			{
				pattern[i] = alphabet.get(scan.next());
				i++;
			}
			
			//skip past "->"
			scan.next();
			
			Cell output = alphabet.get(scan.next());
			outputMap.put(getKey(pattern), output);
		}
		
		scan.close();
	}
	
	/**
	 * Writes a String to a BufferedWriter
	 * 
	 * @param permutation the String to write
	 * @param bw the BufferedWriter to write to
	 */
	private static void write(String permutation, BufferedWriter bw)
	{		
		try {
			bw.write(permutation + "= NULL\n");
		} catch (Exception ex) { ex.printStackTrace(); }
	}
	
	/**
	 * Does the work necessary for method permute(File output).
	 * 
	 * @param set the set of all the identifiers for Cells contained within alphabet
	 * @param permutation a permutation of identifiers 
	 * @param n the size of set
	 * @param k the number of remaining elements to add to the current permutation
	 * @param bw the BufferedWriter to write the completed permutations to
	 */
	private static void permute(String[] set, String permutation, int n, int k, BufferedWriter bw)
	{
		if(k == 0)
			write(permutation, bw);
		else
			for(int i = 0; i < n; i++)
				permute(set, permutation + set[i] + " ", n, k - 1, bw);
	}
	
	/**
	 * Writes all the possible permutations of patterns that could occur in
	 * a CellularAutomaton with the matching Alphabet of Cells, and ruleSize. 
	 *  
	 * @param output the file to write permutations to.
	 * @throws Exception if there is a problem writing to file specified by output
	 */
	public void permute(File output) throws Exception
	{
		ArrayList<Cell> list = alphabet.getSetOfCells();
		
		String[] set = new String[list.size()];
		
		for(int i = 0; i < list.size(); i++)
			set[i] = list.get(i).toString();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		permute(set, "", set.length, ruleSize, bw);
		bw.close();
	}
	
	public String toString()
	{
		String str = super.toString();
		str = str.substring(str.indexOf('.') + 1) + ":\n";
		
		Set<String> set = outputMap.keySet();
		
		for(String key : set)
			str += key + "= " + outputMap.get(key) + "\n";
		
		if(str.length() > 0)
			str = str.substring(0, str.length() - 1);
		
		return str;
	}
}
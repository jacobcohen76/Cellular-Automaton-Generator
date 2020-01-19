package drivers;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import cellularautomaton.Cell;
import cellularautomaton.CellularAutomaton;

public class GenerateAll
{
	private static Settings settings;
	
	public static void main(String args[])
	{
		String rootDirectory = getJARdirectory();
		String outputFolder = Global.outputFolderName;
		
		settings = new Settings(rootDirectory, outputFolder);
		
		try {
			settings.setMappedRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Cell> setOfCells = settings.alphabet.getSetOfCells();
		
		int radix = setOfCells.size();
		int ruleSize = settings.ruleSize;
		
		int numOutputs = (int) Math.pow(radix, ruleSize);
		int numPossibleOutputMaps = (int) Math.pow(radix, numOutputs);
		
		ArrayList<Integer> outputMap = new ArrayList<Integer>(numOutputs);
		for(int i = 0; i < numOutputs; i++)
			outputMap.add(0);
		
		for(int i = 0; i < numPossibleOutputMaps; i++)
		{
			rebase(i, radix, outputMap);
			generate(outputMap, i);
		}
	}
	
	private static void rebase(int n, int radix, ArrayList<Integer> number)
	{
		reset(number);
		
		int i = 0;
		while(n > 0)
		{
			number.set(i, n % radix);
			n /= radix;
			i++;
		}
	}
	
	private static void reset(ArrayList<Integer> number)
	{
		for(int i = 0; i < number.size(); i++)
			number.set(i, 0);
	}
		
	private static void generate(ArrayList<Integer> ruleNumber, int base10)
	{
		try {
			settings.dictionary.remap(ruleNumber);
			CellularAutomaton automaton = settings.getCellularAutomaton();
			settings.saveImage(automaton.getBufferedImage(), base10 + ".png");
		} catch (Exception ex)
		{
			settings.updateErrorLog(ex);
		}
	}
	
	/**
	 * Returns the String path to the directory location of the Jar file this is being run on
	 * 
	 * @return the directory location of the Jar file being run from
	 */
	private static String getJARdirectory()
	{
		try {
			File f = new File(GenerateSettings.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			
			if(f.getParentFile() != null)
				f = f.getParentFile();
			
			return f.getPath() + "\\";
		} catch (URISyntaxException e) {
			return null;
		}
	}
}

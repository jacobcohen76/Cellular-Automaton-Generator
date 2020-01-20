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
		try
		{
			String rootDirectory = getJARdirectory();
			String outputFolder = Global.outputFolderName;
			
			settings = new Settings(rootDirectory, outputFolder);
			
			settings.generateRules();
			settings.setMappedRules();
			settings.setStartingRow();
			
			ArrayList<Cell> setOfCells = settings.alphabet.getSetOfCells();
			
			int radix = setOfCells.size();
			int ruleSize = settings.ruleSize;
			
			int numOutputs = (int) Math.pow(radix, ruleSize);
			long numPossibleOutputMaps = (long) Math.pow(radix, numOutputs);
			
			ArrayList<Integer> radixBasedRuleSet = new ArrayList<Integer>(numOutputs);
			for(int i = 0; i < numOutputs; i++)
				radixBasedRuleSet.add(0);
			
			for(long i = 0; i < numPossibleOutputMaps; i++)
			{
				rebase(i, radix, radixBasedRuleSet);
				generate(radixBasedRuleSet, i);
			}
		}
		catch(Exception ex)
		{
			settings.updateErrorLog(ex);
		}
	}
	
	private static void rebase(long n, int radix, ArrayList<Integer> abnormalRadixBasedNumber)
	{
		reset(abnormalRadixBasedNumber);
		
		int i = 0;
		while(n > 0)
		{
			abnormalRadixBasedNumber.set(i, (int) (n % radix));
			n /= radix;
			i++;
		}
	}
	
	private static void reset(ArrayList<Integer> abnormalRadixBasedNumber)
	{
		for(int i = 0; i < abnormalRadixBasedNumber.size(); i++)
			abnormalRadixBasedNumber.set(i, 0);
	}
		
	private static void generate(ArrayList<Integer> ruleNumber, long base10) throws Exception
	{
		settings.dictionary.remap(ruleNumber);
		CellularAutomaton automaton = settings.getCellularAutomaton();
		settings.saveImage(automaton.getBufferedImage(), base10 + ".png");
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

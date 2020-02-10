package drivers;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import cellularautomaton.Cell;
import cellularautomaton.Dictionary;
import cellularautomaton.Row;

public class GenerateRandom
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
			
			ArrayList<Cell> setOfCells = settings.alphabet.getSetOfCells();
			
			int radix = setOfCells.size();
			int ruleSize = settings.ruleSize;
			
			int numOutputs = (int) Math.pow(radix, ruleSize);
			
			ArrayList<Integer> radixBasedRuleSet = new ArrayList<Integer>(numOutputs);
			for(int i = 0; i < numOutputs; i++)
				radixBasedRuleSet.add(0);
			
			Random rand = new Random();
			for(int i = 0; i < radixBasedRuleSet.size(); i++)
				radixBasedRuleSet.set(i, rand.nextInt(radix));
			
			Cell[] row = new Cell[settings.randomizedRowSize];
			for(int i = 0; i < row.length; i++)
			{
				row[i] = settings.alphabet.get(settings.alphabet.random());
			}
			
			settings.startingRow = new Row(row, settings.ruleSize);
			generate(radixBasedRuleSet, "random");
			
			settings.shutdown();
		}
		catch(Exception ex)
		{
			settings.updateErrorLog(ex);
		}
	}
		
	private static void generate(ArrayList<Integer> ruleNumber, String filename) throws Exception
	{
		Dictionary dictionary = settings.getClonedDictionary();
		dictionary.remap(ruleNumber);
		settings.generate(dictionary, filename + ".png");
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

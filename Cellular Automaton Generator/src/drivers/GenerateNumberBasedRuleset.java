package drivers;

import java.io.File;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;

import cellularautomaton.Cell;
import cellularautomaton.Dictionary;

public class GenerateNumberBasedRuleset
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
			
			ArrayList<Integer> radixBasedRuleSet = new ArrayList<Integer>(numOutputs);
			for(int i = 0; i < numOutputs; i++)
				radixBasedRuleSet.add(0);
			
			BigInteger number = settings.getNumber();
			
			rebase(number, new BigInteger(Integer.toString(radix)), radixBasedRuleSet);
			generate(radixBasedRuleSet, number);
			
			settings.shutdown();
		}
		catch(Exception ex)
		{
			settings.updateErrorLog(ex);
		}
	}
	
	private static void rebase(BigInteger n, BigInteger radix, ArrayList<Integer> abnormalRadixBasedNumber)
	{
		reset(abnormalRadixBasedNumber);
		
		int i = 0;
		while(n.compareTo(BigInteger.ZERO) > 0)
		{
			abnormalRadixBasedNumber.set(i, n.mod(radix).intValue());
			n = n.divide(radix);
			i++;
		}
	}
	
	private static void reset(ArrayList<Integer> abnormalRadixBasedNumber)
	{
		for(int i = 0; i < abnormalRadixBasedNumber.size(); i++)
			abnormalRadixBasedNumber.set(i, 0);
	}
		
	private static void generate(ArrayList<Integer> ruleNumber, BigInteger base10) throws Exception
	{
		Dictionary dictionary = settings.getClonedDictionary();
		dictionary.remap(ruleNumber);
		
		String filename = base10.toString();
		if(base10.toString().length() > 200)
			filename = filename.substring(0, 200);
		filename += ".png";
		
		settings.generate(dictionary, filename);
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

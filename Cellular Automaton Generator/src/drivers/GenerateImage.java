package drivers;

import java.io.File;
import java.net.URISyntaxException;

import cellularautomaton.CellularAutomaton;

/**
 * class GenerateImage to act as a driver for the runnable Jar that when executed
 * will get the information from text files and generate the CellularAutomaton
 * based on user chosen specifications to the output folder. 
 * 
 * @author Jacob Cohen
 */
public class GenerateImage
{
	public static void main(String args[])
	{
		String rootDirectory = getJARdirectory();
		String outputFolder = Global.outputFolderName;
		
		Settings settings = new Settings(rootDirectory, outputFolder);
		
		try
		{
			settings.setMappedRules();
			CellularAutomaton automaton = settings.getCellularAutomaton();
			settings.saveImage(automaton.getBufferedImage());
		}
		catch (Exception ex)
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

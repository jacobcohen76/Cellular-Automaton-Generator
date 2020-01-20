package drivers;

import java.io.File;
import java.net.URISyntaxException;

/**
 * class GenerateSettings to act as a driver for the runnable Jar file that
 * generates the necessary files for a user to input and edit the settings
 * of the CellularAutomaton that they want to create
 * 
 * @author Jacob Cohen
 */
public class GenerateSettings
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
		}
		catch(Exception ex)
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

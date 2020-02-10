package drivers;

import java.io.File;
import java.net.URISyntaxException;

public class RandomizeStartingRow
{
	private static Settings settings;
	
	public static void main(String args[])
	{
		try
		{
			String rootDirectory = getJARdirectory();		
			String outputFolder = Global.outputFolderName;
			
			settings = new Settings(rootDirectory, outputFolder);
			settings.randomizeStartingRow();
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

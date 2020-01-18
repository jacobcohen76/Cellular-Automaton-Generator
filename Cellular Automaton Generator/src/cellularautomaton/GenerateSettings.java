package cellularautomaton;

import java.io.File;
import java.net.URISyntaxException;

public class GenerateSettings
{
	public static void main(String args[]) throws Exception
	{
		String rootDirectory = getJARdirectory();		
		String outputFolder = Global.outputFolderName;
		
		Settings settings = new Settings(rootDirectory, outputFolder);
		settings.generateRules();
	}
	
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

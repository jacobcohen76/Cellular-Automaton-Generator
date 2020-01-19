package drivers;

import java.io.File;
import java.net.URISyntaxException;

import cellularautomaton.CellularAutomaton;

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
			settings.saveImage(automaton.getBufferedImage(), "output.png");
		}
		catch (Exception ex)
		{
			settings.updateErrorLog(ex);
		}
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

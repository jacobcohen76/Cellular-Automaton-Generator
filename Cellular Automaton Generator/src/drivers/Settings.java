package drivers;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import cellularautomaton.Alphabet;
import cellularautomaton.Cell;
import cellularautomaton.CellularAutomaton;
import cellularautomaton.Dictionary;
import cellularautomaton.Row;

/**
 * class Settings to be used to obtain user specified data and generate
 * the appropriate corresponding CellularAutomaton
 * 
 * @author Jacob Cohen
 */
public class Settings
{
	private static final String settingsFileName = "settings.txt";
	private static final String alphabetFileName = "alphabet.txt";
	private static final String rulesFileName = "rules.txt";
	private static final String startingRowFileName = "row.txt";
	private static final String errorlogFileName = "errorlog.txt";
	private static final String numberFileName = "number.txt";
	
	private String rootDirectory;
	private String outputFolder;
	private File settingsTXT;
	private File alphabetTXT;
	private File rulesTXT;
	private File rowTXT;
	private File errorlogTXT;
	private File numberTXT;
	
	private static final String defaultAlphabet =
			"W = FFFFFF\r\n" +
			"B = 000000";
	
	private static final String defaultSettings =
			"ruleSize   = 3\r\n" + 
			"NULLrgbVal = FFFFFF\r\n" +
			"numRows    = 1000\r\n" +
			"numThreads = 1\r\n" +
			"randomizedRowSize = 1000";
	
	private static final String defaultNumber = "0";
	
	public Alphabet alphabet;
	
	public int ruleSize;
	public int nullRGBval;
	public int numThreads;
	public int randomizedRowSize;
	
	public BigInteger number;
	
	public volatile Dictionary dictionary;
	public volatile int numRows; 
	public volatile Row startingRow;
	
	private ExecutorService threadManager;
	
	public Settings(String rootDirectory, String outputFolder)
	{
		this.rootDirectory = rootDirectory;
		this.outputFolder = outputFolder;
		this.initialize();
	}
	
	public void saveImage(BufferedImage image)
	{
		String fileName = (new Timestamp(System.currentTimeMillis())).toString().replace('.', '-').replace(':', '.') + ".png";
		saveImage(image, fileName);
	}
	
	public void randomizeStartingRow()
	{
		String str = "";
		for(int i = 0; i < randomizedRowSize; i++)
		{
			str += alphabet.random() + " ";
		}
		try {
			write(str, rowTXT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			updateErrorLog(e);
		}
	}
	
	public void saveImage(BufferedImage image, String fileName)
	{
		try
		{
			File f = new File(rootDirectory + outputFolder + fileName);
			
			if(f.getParentFile() != null && !f.getParentFile().exists())
				f.getParentFile().mkdirs();
			
			ImageIO.write(image, "png", f);
		}
		catch (Exception ex)
		{
			updateErrorLog(ex);
		}
	}
	
	public Dictionary getClonedDictionary()
	{
		return dictionary.clone();
	}
	
	private void initialize()
	{
		//initializes files to be used for this class
		settingsTXT = new File(rootDirectory + settingsFileName);
		alphabetTXT = new File(rootDirectory + alphabetFileName);
		rulesTXT = new File(rootDirectory + rulesFileName);
		rowTXT = new File(rootDirectory + startingRowFileName);
		errorlogTXT = new File(rootDirectory + errorlogFileName);
		numberTXT = new File(rootDirectory + numberFileName);
		
		startingRow = null;
		
		try
		{
			//ensures the root directory and output folder exists
			File folder = new File(rootDirectory + outputFolder);
			
			if(!folder.exists())
				folder.mkdirs();
			
			//gets and sets data from text files
			getSettings();
			
			threadManager = Executors.newFixedThreadPool(numThreads);
			
			alphabet = getAlphabet();
			alphabet.setNULL(nullRGBval);
			
			number = getBigInteger();
			
			dictionary = new Dictionary(alphabet, ruleSize);
		}
		catch(Exception ex)
		{
			updateErrorLog(ex);
		}
	}
	
	/**
	 * Creates the files "rules.txt", and "startingRow.txt" if they do not already exist, and writes
	 * all the different permutations of Cells defined in the Alphabet of size equal to ruleSize specified by 
	 * the dictionary to "rules.txt".
	 * 
	 * @throws Exception if there is a problem creating and writing to "rules.txt", and "startingRow.txt".
	 */
	public void generateRules() throws Exception
	{
		if(!rulesTXT.exists())
			rulesTXT.createNewFile();
		if(!rowTXT.exists())
			rowTXT.createNewFile();
		
		dictionary.permute(rulesTXT);
	}
	
	/**
	 * Writes the detail message of throwable Exception ex to the file
	 * "errorlog.txt" located in the rootDirectory of this
	 * 
	 * @param ex the throwable Exception that will have its detail message printed to "errorlog.txt"
	 */
	public void updateErrorLog(Exception ex)
	{
		try
		{
			if(!errorlogTXT.exists())
				errorlogTXT.createNewFile();
			
			PrintWriter pw = new PrintWriter(errorlogTXT);
			pw.write(LocalDateTime.now().toString() + "\n");
			pw.write(ex.getLocalizedMessage());
			pw.close();
		}
		catch (Exception e) {}
		
		//terminates the program
		threadManager.shutdown();
		System.exit(0);
	}
	
	/**
	 * Generates a CellularAutomaton based on the settings specified by this
	 * 
	 * @return a new CellularAutomaton that matches the specifications provided by this
	 * @throws Exception throws an Exception if there is a problem when getting the startingRow specifed in file "startingRow.txt"
	 */
	public CellularAutomaton getCellularAutomaton() throws Exception
	{		
		CellularAutomaton cellularAutomaton = new CellularAutomaton(numRows);
		cellularAutomaton.generate(startingRow, dictionary);
		
		return cellularAutomaton;
	}
	
	/**
	 * Generates a CellularAutomaton based on the dictionary of rules provided in the parameter
	 * 
	 * @return a new CellularAutomaton that matches the specifications provided by this
	 * @throws Exception throws an Exception if there is a problem when getting the startingRow specifed in file "startingRow.txt"
	 */
	public CellularAutomaton getCellularAutomaton(Dictionary dictionary) throws Exception
	{		
		CellularAutomaton cellularAutomaton = new CellularAutomaton(numRows);
		cellularAutomaton.generate(startingRow, dictionary);
		
		return cellularAutomaton;
	}
	
	/**
	 * Sets the rules of dictionary based on the rules provided in the file "rules.txt"
	 * 
	 * @throws Exception occurs if the rules listed in "rules.txt" are invalid
	 */
	public void setMappedRules() throws Exception
	{
		dictionary.setOutputMap(rulesTXT);
	}
	
	/**
	 * Writes a message to a file.
	 * 
	 * @param text the message to write to a file
	 * @param f the file to write a message to
	 * @throws Exception if there is a problem writing the specified message to the specified file
	 */
	private void write(String text, File f) throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(text);
		bw.close();
	}
	
	/**
	 * Gets the Row specified in the file "row.txt"
	 * @return the Row specified in the file "row.txt"
	 * @throws Exception
	 */
	public Row getStartingRow() throws Exception
	{
		ArrayList<Cell> list = new ArrayList<Cell>();
		
		Scanner scan = new Scanner(rowTXT);
		
		while(scan.hasNext())
			list.add(alphabet.get(scan.next()));
		
		scan.close();
		
		if(list.size() <= 0)
			throw new Exception("Error, you need to initialize your starting row in the \"rows.txt\" file");
		
		Cell[] listArray = new Cell[list.size()];
		listArray = list.toArray(listArray);
		
		return new Row(listArray, ruleSize);
	}
	
	public void shutdown()
	{
		threadManager.shutdown();
	}
	
	public void setStartingRow()
	{
		try
		{
			startingRow = getStartingRow();
		}
		catch(Exception ex)
		{
			updateErrorLog(ex);
		}
	}
	
	public BigInteger getNumber()
	{
		return number;
	}
	
	private BigInteger getBigInteger() throws Exception
	{
		if(!numberTXT.exists())
		{
			numberTXT.createNewFile();
			write(defaultNumber, numberTXT);
		}
		
		Scanner scan = new Scanner(numberTXT);
		number = scan.nextBigInteger();
		scan.close();
		return number;
	}
	
	/**
	 * Gets and sets the data for the settings specified in the file "settings.txt"
	 * @throws Exception
	 */
	private void getSettings() throws Exception
	{
		if(!settingsTXT.exists())
		{
			settingsTXT.createNewFile();
			write(defaultSettings, settingsTXT);
		}
		
		try
		{
			Scanner scan = new Scanner(settingsTXT);
			
			//skip past variable name and '='
			scan.next();
			scan.next();
			
			ruleSize = scan.nextInt();
			
			//skip past variable name and '='
			scan.next();
			scan.next();
			
			nullRGBval = scan.nextInt(16);
			
			//skip past variable name and '='
			scan.next();
			scan.next();
			
			numRows = scan.nextInt();
			
			//skip past variable name and '='
			scan.next();
			scan.next();
			
			numThreads = scan.nextInt();
			
			scan.next();
			scan.next();
			
			randomizedRowSize = scan.nextInt();
			
			scan.close();
		}
		catch(Exception ex)
		{
			throw new Exception(""
					+ "Error, your \"settings.txt\" file contains invalid settings,\n"
					+ "try deleting your \"settings.txt\" and re-generating your\n"
					+ "settings files with the \"GenerateSettings.jar\" file in\n"
					+ "order to reset your settings to their default values.");
		}
	}
	
	/**
	 * Gets the Alphabet as defined in the file "alphabet.txt".
	 * 
	 * @return the Alphabet as defined in the file "alphabet.txt"
	 * @throws Exception throws an exception if the Alphabet as defined in the file "alphabet.txt" is invalid,
	 * (will be invalid if the same identifier is used more than once, or the same rgbValue is used more than once),
	 * or if there is a problem reading and accessing "alphabet.txt"
	 */
	private Alphabet getAlphabet() throws Exception
	{
		if(!alphabetTXT.exists())
		{
			alphabetTXT.createNewFile();
			write(defaultAlphabet, alphabetTXT);
		}
		
		Alphabet alphabet = new Alphabet();
		Scanner scan = new Scanner(alphabetTXT);
		
		while(scan.hasNext())
		{
			String id = scan.next();
			
			//skip past '='
			scan.next();
			
			int rgbValue = scan.nextInt(16);
			
			alphabet.add(new Cell(id, rgbValue));
		}
		
		scan.close();
		return alphabet;
	}
	
	/**
	 * Returns the number of rules defined in the "rules.txt" file
	 * 
	 * @return the number of rules defined in the "rules.txt" file
	 * @throws Exception if the file "rules.txt" is not found, but
	 * this should never occur
	 */
	public int getNumRules() throws Exception
	{
		int numRules = 0;
		
		try
		{
			Scanner scan = new Scanner(rulesTXT);
			
			while(scan.hasNextLine())
				numRules++;
			
			scan.close();
		}
		catch(Exception ex)
		{
			throw new Exception("Error, the file \"" + rulesFileName + "\" is missing from your Cellular Autonoma Generator's root directory.");
		}
		
		return numRules;
	}
	
	public void generate(Dictionary dictionary, String filename)
	{
		threadManager.execute(new GeneratorThread(dictionary, filename));
	}
	
	private class GeneratorThread implements Runnable
	{
		private Dictionary dictionary;
		private String filename;
		
		public GeneratorThread(Dictionary dictionary, String filename)
		{
			this.dictionary = dictionary;
			this.filename = filename;
		}
		
		public void run()
		{
			try {
				CellularAutomaton automaton = new CellularAutomaton(numRows);
				automaton.generate(startingRow, dictionary);
				saveImage(automaton.getBufferedImage(), filename);
			} catch (Exception e) {}
		}
	}
}

package es.ucm.fdi.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimWindow;
import es.ucm.fdi.control.exceptions.BatchModeListener;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.model.Simulator;
import es.ucm.fdi.view.MainWindow;

public class ExampleMain {

	private final static Integer _timeLimitDefaultValue = 10;
	private static Integer _timeLimit = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseStepsOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			// new Piece(...) might throw GameError exception
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Choose mode").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg()
				.desc("Ticks to execute the simulator's main loop (default value is " + _timeLimitDefaultValue + ").")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(ExampleMain.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && (_mode == null || "batch".equals(_mode))) {
			throw new ParseException("An events file is missing");
		}
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		_mode = line.getOptionValue("m");
		if(_mode != null && !"gui".equals(_mode) && !"batch".equals(_mode)) {
			throw new ParseException("Wrong mode");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
		if(_outFile == null && (_mode == null || "batch".equals(_mode))) {
			throw new ParseException("An output file is missing");
		}
	}

	private static void parseStepsOption(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", _timeLimitDefaultValue.toString());
		try {
			_timeLimit = Integer.parseInt(t);
			assert (_timeLimit < 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time limit: " + t);
		}
	}

	/**
	 * This method run the simulator on all files that ends with .ini if the given
	 * path, and compares that output to the expected output. It assumes that for
	 * example "example.ini" the expected output is stored in "example.ini.eout".
	 * The simulator's output will be stored in "example.ini.out"
	 * 
	 * @throws IOException
	 */
	private static void test(String path) throws IOException {

		File dir = new File(path);
		
		if ( !dir.exists() ) {
			throw new FileNotFoundException(path);
		}
		
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ini");
			}
		});

		for (File file : files) {
			test(file.getAbsolutePath(), file.getAbsolutePath() + ".out", file.getAbsolutePath() + ".eout",100);
		}

	}

	private static void test(String inFile, String outFile, String expectedOutFile, int timeLimit) throws IOException {
		_outFile = outFile;
		_inFile = inFile;
		_timeLimit = timeLimit;
		startBatchMode();
		boolean equalOutput = (new Ini(_outFile)).equals(new Ini(expectedOutFile));
		System.out.println("Result for: '" + _inFile + "' : "
				+ (equalOutput ? "OK!" : ("not equal to expected output +'" + expectedOutFile + "'")));
	}

	/**
	 * Run the simulator in batch mode
	 * 
	 * @throws IOException
	 */
	private static void startBatchMode(){
		try {
			Simulator simulator = new Simulator();
			File infile = new File(_inFile);
			InputStream input = new FileInputStream(infile);
			
			OutputStream output = null;
			if(_outFile.equals("System.out")) {
				output = System.out;
			} else {
				File outfile = new File(_outFile);
				output = new FileOutputStream(outfile);
			}
			
			Controller controller = new Controller(simulator, input, output);
			
			BatchModeListener listener = new BatchModeListener();
			controller.getSimulator().addSimulatorListener(listener);
			
			controller.loadEvents(input);
			controller.run();
			
			input.close();
			output.close();
			
		} catch(IOException | RuntimeException e) {
			System.err.println("ERROR:" + "\n" + e.getLocalizedMessage());
		}
	}
	
	private static void startGUIMode() { //Método de ejecución del programa con l ainterfaz gráfica
			Simulator simulator = new Simulator();
			Controller controller = new Controller(simulator, null, null);	
			File infile;

			if(_inFile != null) {
				infile = new File(_inFile);
				if ( !infile.exists() ) {
					System.err.println("El sistema no puede encontra el archivo de entrada especificado");
					System.exit(1);
				}
			} else {
				infile = null;
			}
			//Creamos la interfaz gráfica
			SwingUtilities.invokeLater(() -> new MainWindow(controller, infile));	

	}

	private static void start(String[] args) throws IOException {
			parseArgs(args);
			//Tras parsear los argumentos vemos con qué modo tenemos que ejecutar el programa
			if("gui".equals(_mode)) startGUIMode();
			else startBatchMode();
	}

	public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {

		// example command lines:
		//
		// -i resources/examples/events/basic/ex1.ini
		// -i resources/examples/events/basic/ex1.ini -o ex1.out
		// -i resources/examples/events/basic/ex1.ini -t 20
		// -i resources/examples/events/basic/ex1.ini -o ex1.out -t 20
		// --help
		//
		
		// Call test in order to test the simulator on all examples in a directory.
		//
		//test("examples/advanced");

		// Call start to start the simulator from command line, etc.
		  start(args);

	}

}


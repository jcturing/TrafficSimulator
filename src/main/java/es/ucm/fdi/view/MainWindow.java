package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.control.Stepper;
import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.Road;
import es.ucm.fdi.model.Vehicle;
import es.ucm.fdi.model.Simulator.EventType;
import es.ucm.fdi.model.Simulator.Listener;
import es.ucm.fdi.model.Simulator.UpdateEvent;

public class MainWindow extends JFrame{

	private Controller ctrl; // la vista usa el controlador
	
	
	private JPanel mainPanel;

	private JLabel statusLabel;
	private JToolBar toolBar;
	private JFileChooser fc;
	private File currentFile;
	private TextAreaOutputStream redirectOutput;
	
	private SimulatorAction loadButton;
	private SimulatorAction saveButton;
	private SimulatorAction clearEventsButton;
	private SimulatorAction checkInEventsButton;
	private SimulatorAction runButton;
	private SimulatorAction stopButton;
	private SimulatorAction resetButton;
	private SimulatorAction genReportsButton;
	private SimulatorAction clearReportsButton;
	private SimulatorAction saveReportsButton;
	private SimulatorAction quitButton;
	
	private JMenuBar menuBar;
	private JMenuItem loadEvents;
	private JMenuItem saveEvents;
	private JMenuItem saveReports;
	private JMenuItem exit;
	private JCheckBoxMenuItem cb;
	private JMenuItem runItem;
	private JMenuItem resetItem;
	private JMenuItem generateReportsItem;
	private JMenuItem cleanReportsItem;
	
	private MainWindowListener listener;
	
	private JSpinner delaySpinner;
	private JSpinner stepsSpinner;
	private JTextField timeViewer;
	private JTextArea eventsEditor;
	private TablePanel eventsTable;
	private JTextArea reportsArea;
	private TablePanel vehiclesTable;
	private TablePanel roadsTable;
	private TablePanel junctionsTable;
	private ReportDialogWindow reportDialog;
	private GraphLayout mapGraph;
	
	private PopUpMenu popUpMenu;
	
	private Stepper stepper;
	
	public MainWindow(Controller ctrl, File inFile) {
		super("Traffic Simulator");
		this.ctrl = ctrl; 
		currentFile = inFile;
		initGUI();
	}
	
	private void initGUI() {
		//Panel que contiene a todos los paneles
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);		
		setVisible(true);
		
		
		fc = new JFileChooser();
		this.listener = new MainWindowListener();
		ctrl.getSimulator().addSimulatorListener(listener);
		
		//Panel de arriba con los 3 paneles
		JPanel upPanel = new JPanel();
		upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.X_AXIS));
		mainPanel.add(upPanel);
		
		//Panel con las 3 tablas y el mapa
		JPanel tableMapPanel = new JPanel();
		tableMapPanel.setLayout(new BoxLayout(tableMapPanel, BoxLayout.X_AXIS));
		
		//Panel con las 3 tablas
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		
		//Panel del mapa
		JPanel mapPanel = new JPanel(new BorderLayout());

		tableMapPanel.add(tablesPanel);
		tableMapPanel.add(mapPanel);
		
		//Division de paneles arriba y abajo
		JSplitPane bottomSplit_1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				upPanel, tableMapPanel);
		mainPanel.add(bottomSplit_1);
		bottomSplit_1.setVisible(true);
		bottomSplit_1.setDividerLocation(mainPanel.getHeight()/3);
		bottomSplit_1.setResizeWeight(0.5);
		
		//Division tablas y mapa
		JSplitPane bottomSplit_2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				tablesPanel, mapPanel);
		tableMapPanel.add(bottomSplit_2);
		bottomSplit_2.setVisible(true);
		bottomSplit_2.setResizeWeight(.5);
		
		//Añadimos los elementos a la interfaz grafica
		addToolBar(); // barra de herramientas
		addMenuBar(); // barra de menus
		addEventsEditor(upPanel); // editor de eventos
		addEventsView(upPanel); // cola de eventos
		addReportsArea(upPanel); // zona de informes
		addVehiclesTable(tablesPanel); // tabla de vehiculos
		addRoadsTable(tablesPanel); // tabla de carreteras
		addJunctionsTable(tablesPanel); // tabla de cruces
		addMap(mapPanel); // mapa de carreteras
		addStatusBar(); //barra de estados
		addStepper();
		
	}
	/**
	 * Creamos las acciones
	 */
	private void addToolBar() {
		loadButton = new SimulatorAction(
				"Load", "open.png", "Load a file",
				KeyEvent.VK_O, "control O", 
				() -> loadFile(eventsEditor));
		
		saveButton = new SimulatorAction(
				"Save", "save.png", "Save to a file",
				KeyEvent.VK_S, "control S", 
				()-> saveFile(eventsEditor));
		
		clearEventsButton = new SimulatorAction(
				"Clean", "clear.png", "Clears events",
				KeyEvent.VK_C, "control C", 
				()-> {eventsEditor.setText("");
					statusLabel.setText("  Events cleared!");
				});
		
		checkInEventsButton = new SimulatorAction(
				"Insert", "events.png", "Inserts events",
				KeyEvent.VK_I, "control I", 
				()-> {
					loadEvents(eventsEditor.getText());
				});
		
		runButton = new SimulatorAction(
				"Run", "play.png", "Run simulator",
				KeyEvent.VK_P, "control P", 
				()-> play()); 
				
		resetButton = new SimulatorAction(
				"Reset", "reset.png", "Reset simulator",
				KeyEvent.VK_R, "control R", 
				()-> reset());
		
		stopButton = new SimulatorAction(
				"Reset", "stop.png", "Stop simulator", 
				KeyEvent.VK_STOP, "control S", 
				()-> stop());
		
		//Ciclos
		JLabel pasos = new JLabel("  Steps:   ");
		stepsSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
		stepsSpinner.setMaximumSize(new Dimension(60, 30));
		JFormattedTextField textField = ((JSpinner.NumberEditor) stepsSpinner.getEditor()).getTextField();
		((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
	
		//Delay
		JLabel delay = new JLabel("  Delay:   ");
		delaySpinner = new JSpinner(new SpinnerNumberModel(0,0,null,100));
		delaySpinner.setMaximumSize(new Dimension(60, 30));
		textField = ((JSpinner.NumberEditor) delaySpinner.getEditor()).getTextField();
		((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
		
		
		//Tiempo
		JLabel tiempo = new JLabel("  Time:   ");
		timeViewer = new JTextField(Integer.toString(0));
		timeViewer.setEditable(false);
		timeViewer.setMaximumSize(new Dimension(65, 30));
		timeViewer.setBackground(Color.WHITE);
		
		genReportsButton = new SimulatorAction(
				"Reports", "report.png", "Generate reports",
				KeyEvent.VK_G, "control G", 
				()-> generarInformes());
		
		clearReportsButton = new SimulatorAction(
				"Clear_reports", "delete_report.png", "Clear reports",
				KeyEvent.VK_D, "control D", 
				()-> eliminarInformes());
		
		saveReportsButton = new SimulatorAction(
				"Save_reports", "save_report.png", "Save reports",
				KeyEvent.VK_G, "control P", 
				()-> guardarInformes());
		
		quitButton = new SimulatorAction(
				"Salir", "exit.png", "Exit",
				KeyEvent.VK_A, "control shift X", 
				()-> System.exit(0));
			
		//Añadimos las acciones a la barra
		toolBar = new JToolBar();
		toolBar.add(loadButton);
		toolBar.add(saveButton);
		toolBar.add(clearEventsButton);
		
		toolBar.add(Box.createHorizontalStrut(5));
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		sep.setMaximumSize(new Dimension(2, sep.getMaximumSize().height));
		toolBar.add(sep);
		toolBar.add(Box.createHorizontalStrut(5));
		
		toolBar.add(checkInEventsButton);
		toolBar.add(runButton);
		runButton.setEnabled(false);
		toolBar.add(stopButton);
		stopButton.setEnabled(false);
		toolBar.add(resetButton);
		toolBar.add(delay);
		toolBar.add(delaySpinner);
		resetButton.setEnabled(false);
		toolBar.add(pasos);
		toolBar.add(stepsSpinner);
		toolBar.add(tiempo);
		toolBar.add(timeViewer);
		
		toolBar.add(Box.createHorizontalStrut(5));
		JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
		sep2.setMaximumSize(new Dimension(2, sep2.getMaximumSize().height));
		toolBar.add(sep2);
		toolBar.add(Box.createHorizontalStrut(5));
		
		toolBar.add(genReportsButton);
		genReportsButton.setEnabled(false);
		toolBar.add(clearReportsButton);
		clearReportsButton.setEnabled(false);
		toolBar.add(saveReportsButton);
		saveReportsButton.setEnabled(false);

		toolBar.add(Box.createHorizontalStrut(5));
		JSeparator sep3 = new JSeparator(JSeparator.VERTICAL);
		sep3.setMaximumSize(new Dimension(2, sep3.getMaximumSize().height));
		toolBar.add(sep3);
		toolBar.add(Box.createHorizontalStrut(5));
		
		toolBar.add(quitButton);
	
		add(toolBar, BorderLayout.NORTH);
	}
	
	/**
	 * Creamos el menu
	*/
	private void addMenuBar() {
		menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu simulator = new JMenu("Simulator");
		JMenu reports = new JMenu("Reports"); 
		
		menuBar.add(file);
		menuBar.add(simulator);
		menuBar.add(reports);
		
		//Creamos los submenus
		loadEvents = new JMenuItem("Load Events");
		loadEvents.addActionListener(loadButton);
		loadEvents.setMnemonic(KeyEvent.VK_L);
		loadEvents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		
		saveEvents = new JMenuItem("Save Events");
		saveEvents.addActionListener(saveButton);
		saveEvents.setMnemonic(KeyEvent.VK_S);
		saveEvents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		
		saveReports = new JMenuItem("Save Reports");
		saveReports.addActionListener(saveReportsButton);
		saveReports.setMnemonic(KeyEvent.VK_R);
		saveReports.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(quitButton);
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		
		file.add(loadEvents);
		file.add(saveEvents);
		file.addSeparator();
		file.add(saveReports);
		file.addSeparator();
		file.add(exit);
		
		runItem = new JMenuItem("Run");
		runItem.addActionListener(runButton);
		simulator.add(runItem);
		runItem.setEnabled(false);

		resetItem = new JMenuItem("Reset");
		resetItem.addActionListener(resetButton);
		simulator.add(resetItem);
		resetItem.setEnabled(false);

		cb = new JCheckBoxMenuItem("Redirect");
		cb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) ctrl.setOut(redirectOutput);
				else ctrl.setOut(null);
			}
		});
		simulator.add(cb);

		generateReportsItem = new JMenuItem("Generate");
		generateReportsItem.addActionListener(genReportsButton);
		generateReportsItem.setEnabled(false);
		
		cleanReportsItem = new JMenuItem("Clean");
		cleanReportsItem.addActionListener(clearReportsButton);
		cleanReportsItem.setEnabled(false);

		reports.add(generateReportsItem);
		reports.add(cleanReportsItem);

		setJMenuBar(menuBar);
	}
	
	/**
	 * Creamos la barra de estados y la colocamos abajo
	 */
	public void addStatusBar() {
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		mainPanel.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), 27));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("  Welcome to the simulator!");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
	}

	/**
	 * Creaccion del JTextArea para escribir los eventos que seran insertados al simulador
	 */
	public void addEventsEditor(JPanel content) {	
		eventsEditor = new JTextArea(18,30);
		eventsEditor.setEditable(true);	
		JScrollPane area = new JScrollPane(eventsEditor, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		area.setBorder(BorderFactory.createTitledBorder(b, "Events"));
		//Creamos un PopUpMenu
		popUpMenu = new PopUpMenu(eventsEditor, loadButton, saveButton);
		content.add(area);
		
		if(currentFile != null) {
			eventsEditor.setText(readFile(currentFile));
		}
	}

	/**
	 * A�ade panel de eventos
	 */
	public void addEventsView(JPanel content) {
		String[] title = { "#", "Time", "Type" };
		eventsTable = new TablePanel(title, new ArrayList<Event>());
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		eventsTable.setBorder(BorderFactory.createTitledBorder(b, "Event Queue"));
		content.add(eventsTable);
	}
	
	/**
	 * Creacion del panel en el que se mostrara el estado del simulador
	 */
	public void addReportsArea(JPanel content) {
		reportsArea = new JTextArea(18, 30);
		reportsArea.setEditable(false);
		JScrollPane area = new JScrollPane(reportsArea, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		area.setBorder(BorderFactory.createTitledBorder(b, "Reports"));
		content.add(area);
		
		//Creamos una zona de escritura que variara segun que opcion tengamos habilitada
		redirectOutput = new TextAreaOutputStream(reportsArea);
		//Creamos una ventana de dialogo en la que posteriormente seleccionaremos que reports queremos
		reportDialog = new ReportDialogWindow(this);
	}
	
	/**
	 * Tabla en la que se mostraran el estado de los vehiculos durante la simulacion
	 */
	public void addVehiclesTable(JPanel content) {
		String[] title = { "ID", "Road", "Location", "Speed", "Km", "Faulty Units",  "Itinerary"};
		vehiclesTable = new TablePanel(title, new ArrayList<Junction>());
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		vehiclesTable.setBorder(BorderFactory.createTitledBorder(b, "Vehicles"));
		content.add(vehiclesTable);
	}
	
	/**
	 * Tabla en la que se mostraran el estado de las carreteras durante la simulacion
	 */
	public void addRoadsTable(JPanel content) {
		String[] title = { "ID", "Source", "Target", "Length", "Max Speed", "Vehicles"};
		roadsTable = new TablePanel(title, new ArrayList<Road>());
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		roadsTable.setBorder(BorderFactory.createTitledBorder(b, "Roads"));		
		content.add(roadsTable);
	}
	
	/**
	Tabla en la que se mostraran el estado de los cruces durante la simulacion
	*/
	public void addJunctionsTable(JPanel content) {
		String[] title = { "ID", "Green", "Red"};
		junctionsTable = new TablePanel(title, new ArrayList<Junction>());
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		junctionsTable.setBorder(BorderFactory.createTitledBorder(b, "Junctions"));
		content.add(junctionsTable);
	}
	
	/**
	* Grafico del simulador
	*/
	public void addMap(JPanel content) {
		 mapGraph = new  GraphLayout(ctrl.getRoadMap());
		 content.add(mapGraph);
	}

	
	public class MainWindowListener implements Listener{
		
		/***Funcion que se encarga de actualizar el mapa cuando ocurre algo*/
		public void update(UpdateEvent ue, String error) {
			SwingUtilities.invokeLater(() -> {
				switch(ue.getEvent()) {
				case REGISTERED: 
					break;
					
				case RESET:
					refresh(ue);
					stepsSpinner.setValue(0);
					timeViewer.setText("0");
					break;
					
				case NEWEVENT: 
					eventsTable.setElements(ue.getEvenQueue());
					break;
					
				case ADVANCED:
					refresh(ue);
					break;
					
				case ERROR:
					JFrame exception = new JFrame();
					String title_error = "ERROR";		
					JOptionPane.showMessageDialog(exception, error, title_error, JOptionPane.ERROR_MESSAGE);
					System.exit(1);
					break;
				}
			});
		}
		
		public void refresh(UpdateEvent ue) {
			eventsTable.setElements(ue.getEvenQueue());
			vehiclesTable.setElements(ue.getVehicles());
			roadsTable.setElements(ue.getRoads());
			junctionsTable.setElements(ue.getJunctions());
			mapGraph.setRoadMap(ue.getRoadMap());
			reportDialog.setData(ue.getJunctions(), ue.getRoads(), ue.getVehicles());
		}
		
	};
	
	/**
	 * Funcion que sirve para cargar en el editor de textos un archivo del ordenador
	 */
	private void loadFile(JTextArea textArea) {
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String s = readFile(file);
			textArea.setText(s);
			statusLabel.setText("  Events have been loaded from the file!");
		}
	}
	
	/**
	 * Devuelve un String a partir de un archivo dado
	 */
	public static String readFile(File file) {
		String s = "";
		try {
			s = new Scanner(file).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * Funcion que crea un archivo y guarda en el la informacion contenida en el reportsEditor
	 */
	private void saveFile(JTextArea textArea) {
		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			writeFile(file, textArea.getText());
			statusLabel.setText("  Events have been saved!");
		}
	}
	
	/**
	 * Funcion que escribe un archivo un String
	 */
	public static void writeFile(File file, String content) {
		try {
			PrintWriter pw = new PrintWriter(file);
			pw.print(content);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Funcion que genera informes de lo pedido por el usuario
	*/
	public void generarInformes() {
		int status = reportDialog.open();
		if(status == 1) {//Se abre un cuadro de dialogo en que podemos seleccionar varias opciones
			redirectOutput.setStart(true);
				try {//El usuario selecciona los vehiculos, carreteras y cruces que quiere que se reporten
				List<Junction> auxJunction = new ArrayList<>();
				for(String s : reportDialog.getSelectedJunctions()) {
					auxJunction.add(ctrl.getSimulator().getRoadMap().getJunction(s));
				}
				ctrl.getSimulator().writeObjects(auxJunction, redirectOutput);
				
				List<Road> auxRoad = new ArrayList<>();
				for(String s : reportDialog.getSelectedRoads()) {
					auxRoad.add(ctrl.getSimulator().getRoadMap().getRoad(s));
				}
				ctrl.getSimulator().writeObjects(auxRoad, redirectOutput);
				
				List<Vehicle> auxVehicle = new ArrayList<>();
				for(String s : reportDialog.getSelectedVehicles()) {
					auxVehicle.add(ctrl.getSimulator().getRoadMap().getVehicle(s));
				}		
				ctrl.getSimulator().writeObjects(auxVehicle, redirectOutput);
				
				statusLabel.setText("  Reports have been generated!");
			} catch(IOException e) {
				ctrl.getSimulator().fireUpdateEvent(EventType.ERROR, "Generating reports");
			}
		}
	}
	
	/**
	 * Funcion que resetea el reportsArea
	 */
	public void eliminarInformes() {
		reportsArea.setText("");
		statusLabel.setText("  Reports cleaned!");
	}
	
	/**
	 * Funcion que crea un archivo y guarda en el la informacion contenida en el reportsArea
	 */
	public void guardarInformes() {
		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			writeFile(file, reportsArea.getText());
			statusLabel.setText("  Reports have been saved!");
		}
	}
	
	/**
	 * Funcion que carga los eventos escritos en el eventsEditor a la cola del simulador
	 */
	public void loadEvents(String events) {
		InputStream stream = new ByteArrayInputStream(events.getBytes());
		ctrl.loadEvents(stream);
		if(!(eventsEditor.getText().equals(""))) {
			runButton.setEnabled(true);
			resetButton.setEnabled(true);
			
			runItem.setEnabled(true);
			resetItem.setEnabled(true);
			
			statusLabel.setText("  Events have been loaded to the simulator!");
		}		
	}
	
	/**
	 * Funcion que ejecuta el simulador tantos pasos como se pidan
	*/
	public void play() {
		if((int) stepsSpinner.getValue() > 0) {
			stepper.start((int)stepsSpinner.getValue(), (int)delaySpinner.getValue());
			statusLabel.setText("  Running simulator!");
		}
	}
	
	/**
	 * Funcion que se encarga de resetear todo el simulador
	*/
	public void reset() {
		ctrl.setNewSimulator();
		reportsArea.setText("");
		statusLabel.setText("  Simulator reseted!");
		
		runButton.setEnabled(false);
		runItem.setEnabled(false);
		stopButton.setEnabled(false);
		
		resetButton.setEnabled(false);
		resetItem.setEnabled(false);
		
		genReportsButton.setEnabled(false);
		generateReportsItem.setEnabled(false);
		
		clearReportsButton.setEnabled(false);
		cleanReportsItem.setEnabled(false);
		
		saveReportsButton.setEnabled(false);
	}
	
	public void addStepper() {
		Runnable before = new Runnable() {
			@Override
			public void run() {
				stopButton.setEnabled(true);
				setButtons(false);
			}
		};
		
		Runnable during = new Runnable() {
			@Override
			public void run() {
				ctrl.run(1);
				Integer aux1 = Integer.parseInt(timeViewer.getText());
				timeViewer.setText(String.valueOf(aux1 + 1));
			}
		};
		
		Runnable after = new Runnable() {
			@Override
			public void run() {
				setButtons(true);	
			}
		};
		
		stepper = new Stepper(before, during, after);
	}
	
	public void setButtons(boolean option) {
		loadButton.setEnabled(option);
		saveButton.setEnabled(option);
		clearEventsButton.setEnabled(option);
		checkInEventsButton.setEnabled(option);
		runButton.setEnabled(option);
		resetButton.setEnabled(option);
		genReportsButton.setEnabled(option);
		clearReportsButton.setEnabled(option);
		saveReportsButton.setEnabled(option);
		quitButton.setEnabled(option);
		loadEvents.setEnabled(option);
		saveEvents.setEnabled(option);
		saveReports.setEnabled(option);
		exit.setEnabled(option);
		cb.setEnabled(option);
		runItem.setEnabled(option);
		resetItem.setEnabled(option);
		generateReportsItem.setEnabled(option);
		cleanReportsItem.setEnabled(option);
		popUpMenu.getMenu().setEnabled(option);
	}
	
	public void stop() {
		stepper.stop();
	}

}

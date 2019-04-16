package es.ucm.fdi.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.extra.dialog.MyListModel;
import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.Road;
import es.ucm.fdi.model.Vehicle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Clase encargada que crear la ventana de dialogo en la que seleccionamos los reports que queremos obtener
 *
 */
public class ReportDialogWindow extends JDialog {

	private MyListModel<String> itemsJunctionsModel;
	private MyListModel<String> itemsRoadsModel;
	private MyListModel<String> itemsVehiclesModel;

	private int _status;
	private JList<String> itemsJunctions;
	private JList<String> itemsRoads;
	private JList<String> itemsVehicles;

	static final private char _clearSelectionKey = 'c';
	private Border defaultBorder = BorderFactory.createLineBorder(Color.black, 2);

	public ReportDialogWindow(Frame parent) {
		super(parent, true);
		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Generate");
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		JPanel vehiclesPanel = new JPanel(new BorderLayout());
		JPanel roadsPanel = new JPanel(new BorderLayout());
		JPanel junctionsPanel = new JPanel(new BorderLayout());

		contentPanel.add(vehiclesPanel);
		contentPanel.add(roadsPanel);
		contentPanel.add(junctionsPanel);

		vehiclesPanel.setBorder(
				BorderFactory.createTitledBorder(defaultBorder, "Vehicles", TitledBorder.LEFT, TitledBorder.TOP));
		roadsPanel.setBorder(
				BorderFactory.createTitledBorder(defaultBorder, "Roads", TitledBorder.LEFT, TitledBorder.TOP));
		junctionsPanel.setBorder(
				BorderFactory.createTitledBorder(defaultBorder, "Junctions", TitledBorder.LEFT, TitledBorder.TOP));

		
		
		vehiclesPanel.setMinimumSize(new Dimension(105, 200));
		vehiclesPanel.setMaximumSize(new Dimension(105, 200));
		roadsPanel.setMinimumSize(new Dimension(105, 200));
		roadsPanel.setMaximumSize(new Dimension(105, 200));
		junctionsPanel.setMinimumSize(new Dimension(105, 200));
		junctionsPanel.setMaximumSize(new Dimension(105, 200));
		
		

		itemsJunctionsModel = new MyListModel<>();
		itemsRoadsModel = new MyListModel<>();
		itemsVehiclesModel = new MyListModel<>();

		itemsJunctions = new JList<>(itemsJunctionsModel);
		itemsRoads = new JList<>(itemsRoadsModel);
		itemsVehicles = new JList<>(itemsVehiclesModel);

		addCleanSelectionListner(itemsJunctions);
		addCleanSelectionListner(itemsRoads);
		addCleanSelectionListner(itemsVehicles);

		vehiclesPanel.add(new JScrollPane(itemsVehicles, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		roadsPanel.add(new JScrollPane(itemsRoads, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
		junctionsPanel.add(new JScrollPane(itemsJunctions, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ReportDialogWindow.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("Generate");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 1;
				ReportDialogWindow.this.setVisible(false);
			}
		});
		buttonsPanel.add(okButton);

		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		mainPanel.add(infoPanel, BorderLayout.PAGE_START);

		infoPanel.add(new JLabel("Select items for which you want to generate reports."));
		infoPanel.add(new JLabel("Use '" + _clearSelectionKey + "' to deselect all."));
		infoPanel.add(new JLabel("Use Ctrl+A to select all"));
		infoPanel.add(new JLabel(" "));

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		setVisible(false);
	}

	private void addCleanSelectionListner(JList<?> list) {
		list.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == _clearSelectionKey) {
					list.clearSelection();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

	}

	public void setData(List<Junction> junctions, List<Road> roads, List<Vehicle> vehicles) {
		List<String> auxJunctions = new ArrayList<>();
		List<String> auxRoads = new ArrayList<>();
		List<String> auxVehicles = new ArrayList<>();
		
		for(Junction j : junctions) {
			auxJunctions.add(j.getId());
		}
		for(Road r : roads) {
			auxRoads.add(r.getId());
		}
		for(Vehicle v : vehicles) {
			auxVehicles.add(v.getId());
		}
		
		itemsJunctionsModel.setList(auxJunctions);
		itemsRoadsModel.setList(auxRoads);
		itemsVehiclesModel.setList(auxVehicles);
	}

	public String[] getSelectedVehicles() {
		int[] indices = itemsVehicles.getSelectedIndices();
		String[] items = new String[indices.length];
		for(int i=0; i<items.length; i++) {
			items[i] = itemsVehiclesModel.getElementAt(indices[i]);
		}
		
		itemsVehicles.clearSelection();
		return items;
	}

	public String[] getSelectedJunctions() {
		int[] indices = itemsJunctions.getSelectedIndices();
		String[] items = new String[indices.length];
		for(int i=0; i<items.length; i++) {
			items[i] = itemsJunctionsModel.getElementAt(indices[i]);
		}
		itemsJunctions.clearSelection();
		return items;
	}

	public String[] getSelectedRoads() {
		int[] indices = itemsRoads.getSelectedIndices();
		String[] items = new String[indices.length];
		for(int i=0; i<items.length; i++) {
			items[i] = itemsRoadsModel.getElementAt(indices[i]);
		}
		itemsRoads.clearSelection();
		return items;
	}


	public int open() {
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
		pack();
		setVisible(true);
		return _status;
	}

}
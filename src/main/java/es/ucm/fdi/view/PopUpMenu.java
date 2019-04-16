package es.ucm.fdi.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import es.ucm.fdi.control.SimulatorAction;

/**
 * 
 * Clase que crea el PopUpMenu
 *
 */
public class PopUpMenu extends JFrame{
	
	JPopupMenu popUpMenu;
	JMenu templates;
	JMenuItem load;
	JMenuItem save;
	JMenuItem clear;
		
	public PopUpMenu(JTextArea eventsEditor, SimulatorAction loadButton, SimulatorAction saveButton ) {
		popUpMenu = new JPopupMenu();
		
		templates = addTemplate(eventsEditor);
		load = addLoad(eventsEditor, loadButton);
		save = addSave(eventsEditor, saveButton);
		clear = addClear(eventsEditor);
		
		popUpMenu.add(templates);
		popUpMenu.addSeparator();
		popUpMenu.add(load);
		popUpMenu.add(save);
		popUpMenu.add(clear);
			
		eventsEditor.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}

			private void showPopup(MouseEvent e) {
				if (e.isPopupTrigger() && popUpMenu.isEnabled()) {
					popUpMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		
	}
	
	public JPopupMenu getMenu() {
		return popUpMenu;
	}
	
	public JMenuItem getLoadMenu() {
		return load;
	}
	
	public JMenuItem getSaveMenu() {
		return save;
	}

	public JMenu addTemplate(JTextArea eventsEditor) {
		JMenu addTemplate = new JMenu("Add Template");
		
		addToTemplateMenu(addTemplate, eventsEditor, "New RR Junction",
			"[new_junction]\n"
			+ "time = \n"
			+ "id = \n"
			+ "type = rr\n"
			+ "max_time_slice = \n"
			+ "min_time_slice = \n\n"
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New MC Junction",
			"[new_junction]\n"
			+ "time = \n"
			+ "id = \n"
			+ "type = mc\n\n"	
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New Junction",
			"[new_junction]\n"
			+ "time = \n"
			+ "id = \n\n"
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New Dirt Road",
			"[new_road]\n"
			+ "time = \n"
			+ "id = \n"
			+ "src = \n"
			+ "dest = \n"
			+ "max_speed = \n"
			+ "length = \n"
			+ "type = dirt\n\n"			
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New Lanes Road",
			"[new_road]\n"
			+ "time = \n"
			+ "id = \n"
			+ "src = \n"
			+ "dest = \n"
			+ "max_speed = \n"
			+ "length = \n"
			+ "type = lanes\n"	
			+ "lanes = \n\n"
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New Road",
			"[new_road]\n"
			+ "time = \n"
			+ "id = \n"
			+ "src = \n"
			+ "dest = \n"
			+ "max_speed = \n"
			+ "length = \n"
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New Bike",
			"[new_vehicle]\n"
			+ "time = \n"
			+ "id = \n"
			+ "itinerary = \n"
			+ "max_speed = \n"
			+ "type = bike\n\n"
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New Car",
			"[new_vehicle]\n"
			+ "time = \n"
			+ "id = \n"
			+ "itinerary = \n"
			+ "max_speed = \n"
			+ "type = car\n"
			+ "resistance = \n"
			+ "fault_probability = \n"
			+ "max_fault_duration = \n"
			+ "seed = \n\n"			
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New Vehicle",
			"[new_vehicle]\n"
			+ "time = \n"
			+ "id = \n"
			+ "itinerary = \n"
			+ "max_speed = \n"
		);
		
		addToTemplateMenu(addTemplate, eventsEditor, "New Vehicle Faulty",
			"[make_vehicle_faulty]\n"
			+ "time = \n"
			+ "vehicles = \n"
			+ "duration = \n\n"
		);
		
		return addTemplate;
	}
	
	public void addToTemplateMenu(JMenu target, JTextArea textArea, String title, String text) {
		JMenuItem item = new JMenuItem(title);
		item.addActionListener((ae) -> textArea.append(text));
		target.add(item);
	}
	
	public JMenuItem addLoad(JTextArea eventsEditor, SimulatorAction loadButton) {//Implementa como archivo
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(loadButton);
		
		return load;
	}
	
	public JMenuItem addSave(JTextArea eventsEditor, SimulatorAction saveButton) {//Implementa como guardar un archivo
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(saveButton);
	
		return save;
	}

	/***Limpia el eventsEditor*/
	public JMenuItem addClear(JTextArea eventsEditor) {
		// Crea y configura el JMenuItem de Clear
		JMenuItem clear = new JMenuItem("Clear");
		clear.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				eventsEditor.setText("");
			}
		});
		return clear;
	}
	
}

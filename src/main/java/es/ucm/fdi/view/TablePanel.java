package es.ucm.fdi.view;

import java.awt.BorderLayout;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


class TablePanel extends JPanel {

		private JTable tabla;
		private ListOfMapsTableModel modelo = new ListOfMapsTableModel();
		private String[] fieldNames;
		private List<? extends Describable> elements;
		
		public TablePanel(String[] newFieldNames, List<? extends Describable> newElements) {
			super(new BorderLayout());
			fieldNames = newFieldNames;
			elements = newElements;
			tabla = new JTable(modelo);
			add(new JScrollPane(tabla,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		}
		public void setElements( List<? extends Describable> elements) {
			this.elements=elements;
			modelo.fireTableDataChanged();
		}
		
		private class ListOfMapsTableModel extends AbstractTableModel {
			@Override // fieldNames es un String[] con nombrs de col.
			public String getColumnName(int columnIndex) {
				return fieldNames[columnIndex];
			}
			@Override // elements contiene la lista de elementos
			public int getRowCount() {
				return elements.size();
			}
			@Override
			public int getColumnCount() {
				return fieldNames.length;
			}
			@Override // ineficiente: ¿puedes mejorarlo?
			public Object getValueAt(int rowIndex, int columnIndex) {
				if(fieldNames[columnIndex].equals("#")) {
					return ""+rowIndex;
				}
				else {
					return elements.get(rowIndex).describe().get(fieldNames[columnIndex]);
				}
			}
	
		public JTable getTabla() {
			return tabla;
		}
	}
	
}

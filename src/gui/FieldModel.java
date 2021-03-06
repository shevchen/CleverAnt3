package gui;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

class FieldModel extends DefaultTableModel {
	public FieldModel(JPanel[][] data, String[] columnNames) {
		for (String s : columnNames) {
			super.addColumn(s);
		}
		for (JPanel[] ls : data) {
			super.addRow(ls);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return JPanel.class;
	}
}

package gui;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

public class AutomataTableModel extends DefaultTableModel {
	public AutomataTableModel(Object[][] data, String[] columnNames) {
		for (String s : columnNames) {
			super.addColumn(s);
		}
		for (Object[] ls : data) {
			super.addRow(ls);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 2;
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex < 2 ? JLabel.class : CenteredButton.class;
	}
}

package gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

public class AutomataModel extends DefaultTableModel {
	public AutomataModel(Object[][] data, String[] columnNames) {
		for (String s : columnNames) {
			super.addColumn(s);
		}
		for (Object[] ls : data) {
			super.addRow(ls);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex < 2 ? JLabel.class : JButton.class;
	}
}

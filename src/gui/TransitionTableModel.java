package gui;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

public class TransitionTableModel extends DefaultTableModel {
	public TransitionTableModel(Object[][] data, String[] columnNames) {
		for (String s : columnNames) {
			super.addColumn(s);
		}
		for (Object[] ls : data) {
			super.addRow(ls);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return JLabel.class;
	}
}

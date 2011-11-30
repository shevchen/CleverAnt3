package gui;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

class SignificantPanelModel extends DefaultTableModel {
	public SignificantPanelModel(JLabel[][] data, String[] columnNames) {
		for (String s : columnNames) {
			super.addColumn(s);
		}
		for (JLabel[] ls : data) {
			super.addRow(ls);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return JLabel.class;
	}
}
